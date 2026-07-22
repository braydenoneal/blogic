package block.entity

import Blogic
import blang.BlogicProgram
import blang.Context
import blang.codec.Codecs
import block.CableBlock
import com.mojang.serialization.Codec
import net.fabricmc.fabric.api.menu.v1.ExtendedMenuProvider
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup.Provider
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.level.Level
import net.minecraft.world.level.SignalGetter
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import networking.ControllerPayload
import program.Program
import program.statement.IncompleteException
import java.util.*
import kotlin.jvm.optionals.getOrNull

class ControllerBlockEntity(pos: BlockPos, state: BlockState) : BlockEntity(ModBlockEntities.CONTROLLER_BLOCK_ENTITY, pos, state), ExtendedMenuProvider<BlockPos> {
    var program: BlogicProgram = BlogicProgram(Context(pos, this))
    var initializing = true

    fun setSource(payload: ControllerPayload) {
        program.name = payload.name
        program.cursorPosition = payload.cursorPosition
        program.draft = payload.source

        if (payload.isDraft) {
            setChanged()
            return
        }

        program.source = payload.source
        initializing = true
        program.hasError = false

        if (!level!!.isClientSide) {
            try {
                program.parse()
            } catch (exception: Exception) {
                Blogic.LOGGER.error(exception.message)
                program.hasError = true
            }
        }

        setChanged()
    }

    override fun loadAdditional(view: ValueInput) {
        super.loadAdditional(view)

        initializing = view.read("initializing", Codec.BOOL).getOrNull() ?: true

        val draft = view.read("draft", Codec.STRING).getOrNull() ?: ""
        val cursorPosition = view.read("cursor_position", Codec.INT).getOrNull() ?: 0
        val rawProgram = view.read("raw_program", Codecs.PROGRAM_CODEC).getOrNull() ?: Program()

        program = BlogicProgram(
            Context(worldPosition, this),
            rawProgram.source,
            rawProgram.parsed,
            rawProgram.name,
            rawProgram.imports,
            rawProgram.statements,
            rawProgram.functions,
            rawProgram.scopes,
            draft,
            cursorPosition,
        )
    }

    override fun saveAdditional(view: ValueOutput) {
        super.saveAdditional(view)

        view.store("initializing", Codec.BOOL, initializing)
        view.store("draft", Codec.STRING, program.draft)
        view.store("cursor_position", Codec.INT, program.cursorPosition)

        val rawProgram = Program(
            program.source,
            program.parsed,
            program.name,
            program.imports,
            program.statements,
            program.functions,
            program.scopes,
        )

        view.store("raw_program", Codecs.PROGRAM_CODEC, rawProgram)
    }

    override fun getUpdateTag(registryLookup: Provider): CompoundTag {
        return saveWithoutMetadata(registryLookup)
    }

    val facing: Direction get() = blockState.getValue(BlockStateProperties.FACING)

    override fun getDisplayName(): Component {
        return Component.translatable(blockState.block.descriptionId)
    }

    override fun createMenu(syncId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu {
        return ControllerScreenHandler(syncId, playerInventory, this)
    }

    override fun getScreenOpeningData(player: ServerPlayer): BlockPos {
        return worldPosition
    }

    fun getConnectedControllerBlockEntities(): MutableList<ControllerBlockEntity> {
        val cables: MutableSet<BlockPos> = HashSet()
        val networkBlocks: MutableList<BlockPos> = mutableListOf()

        val stack = Stack<BlockPos>()

        for (direction in SignalGetter.DIRECTIONS) {
            val adjacentPos = worldPosition.relative(direction)

            val adjacentBlock = level!!.getBlockState(adjacentPos).block

            if (adjacentBlock is CableBlock) {
                stack.push(adjacentPos)
                cables.add(adjacentPos)
            }
        }

        while (!stack.isEmpty()) {
            val pos = stack.pop()

            for (direction in SignalGetter.DIRECTIONS) {
                val adjacentPos = pos.relative(direction)

                if (cables.contains(adjacentPos)) {
                    continue
                }

                val adjacentBlock = level!!.getBlockState(adjacentPos).block

                if (adjacentBlock is CableBlock) {
                    stack.push(adjacentPos)
                    cables.add(adjacentPos)
                }

                if (networkBlocks.contains(adjacentPos)) {
                    continue
                }

                val adjacentBlockEntity = level!!.getBlockEntity(adjacentPos)

                if (adjacentBlockEntity is ControllerBlockEntity) {
                    networkBlocks.add(adjacentPos)
                }
            }
        }

        val controllers: MutableList<ControllerBlockEntity> = mutableListOf()

        for (pos in networkBlocks) {
            val adjacentBlockEntity = level!!.getBlockEntity(pos)

            if (adjacentBlockEntity is ControllerBlockEntity) {
                controllers.add(adjacentBlockEntity)
            }
        }

        return controllers
    }

    fun getConnectedContainers(): MutableList<BaseContainerBlockEntity> {
        val cables: MutableSet<BlockPos> = HashSet()
        val networkBlocks: MutableList<BlockPos> = mutableListOf()

        val stack = Stack<BlockPos>()

        for (direction in SignalGetter.DIRECTIONS) {
            val adjacentPos = worldPosition.relative(direction)

            val adjacentBlock = level!!.getBlockState(adjacentPos).block

            if (adjacentBlock is CableBlock) {
                stack.push(adjacentPos)
                cables.add(adjacentPos)
            }
        }

        while (!stack.isEmpty()) {
            val pos = stack.pop()

            for (direction in SignalGetter.DIRECTIONS) {
                val adjacentPos = pos.relative(direction)

                if (cables.contains(adjacentPos)) {
                    continue
                }

                val adjacentBlock = level!!.getBlockState(adjacentPos).block

                if (adjacentBlock is CableBlock) {
                    stack.push(adjacentPos)
                    cables.add(adjacentPos)
                }

                if (networkBlocks.contains(adjacentPos)) {
                    continue
                }

                val adjacentBlockEntity = level!!.getBlockEntity(adjacentPos)

                if (adjacentBlockEntity is BaseContainerBlockEntity) {
                    networkBlocks.add(adjacentPos)
                }
            }
        }

        val controllers: MutableList<BaseContainerBlockEntity> = mutableListOf()

        for (pos in networkBlocks) {
            val adjacentBlockEntity = level!!.getBlockEntity(pos)

            if (adjacentBlockEntity is BaseContainerBlockEntity) {
                controllers.add(adjacentBlockEntity)
            }
        }

        return controllers
    }

    companion object {
        fun tick(
            world: Level,
            blockPos: BlockPos,
            @Suppress("unused")
            ignoredBlockState: BlockState,
            entity: ControllerBlockEntity,
        ) {
            if (!entity.program.parsed) {
                entity.program.parse()
                entity.program.hasError = false
                entity.setChanged()
            }

            if (entity.program.hasError) {
                return
            }

            try {
                if (entity.initializing) {
                    try {
                        if (entity.program.tick()) {
                            entity.initializing = false
                        }
                    } catch (_: IncompleteException) {
                    }

                    entity.setChanged()
                } else if (world.hasNeighborSignal(blockPos)) {
                    entity.program.runMain()
                    entity.setChanged()
                }
            } catch (exception: Exception) {
                Blogic.LOGGER.error(exception.message)
                entity.program.hasError = true
            }
        }
    }
}
