package block.entity

import blang.BlogicProgram
import blang.Context
import blang.codec.Codecs
import block.CableBlock
import com.mojang.serialization.Codec
import net.fabricmc.fabric.api.menu.v1.ExtendedMenuProvider
import net.fabricmc.fabric.api.networking.v1.PlayerLookup.level
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup.Provider
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
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

class ControllerBlockEntity(pos: BlockPos, state: BlockState) :
    BlockEntity(ModBlockEntities.CONTROLLER_BLOCK_ENTITY, pos, state), ExtendedMenuProvider<ControllerPayload> {
    var program: BlogicProgram = BlogicProgram(Context(this))
    var initializing = true

    fun setSource(payload: ControllerPayload) {
        program.name = payload.name
        program.cursorPosition = payload.cursor
        program.draft = payload.draft

        if (payload.isDraft) {
            setChanged()
            return
        }

        program.source = payload.draft

        if (payload.run) {
            initializing = true
            program.hasError = false
            program.console = ""
        }

        if (!level!!.isClientSide) {
            try {
                program.parse()
            } catch (exception: Exception) {
                program.console += "${exception.message}\n"
                program.hasError = true

                for (player in level((level as ServerLevel))) {
                    ServerPlayNetworking.send(player, getPayload())
                }
            }
        }

        setChanged()
    }

    override fun loadAdditional(view: ValueInput) {
        super.loadAdditional(view)

        initializing = view.read("initializing", Codec.BOOL).getOrNull() ?: true

        val draft = view.read("draft", Codec.STRING).getOrNull() ?: ""
        val console = view.read("console", Codec.STRING).getOrNull() ?: ""
        val cursorPosition = view.read("cursor_position", Codec.INT).getOrNull() ?: 0
        val rawProgram = view.read("raw_program", Codecs.PROGRAM_CODEC).getOrNull() ?: Program()
        val hasError = view.read("has_error", Codec.BOOL).getOrNull() ?: false

        program = BlogicProgram(
            Context(this),
            rawProgram.source,
            rawProgram.parsed,
            rawProgram.name,
            rawProgram.imports,
            rawProgram.statements,
            rawProgram.functions,
            rawProgram.scopes,
            draft,
            cursorPosition,
            console,
            hasError,
        )
    }

    override fun saveAdditional(view: ValueOutput) {
        super.saveAdditional(view)

        view.store("initializing", Codec.BOOL, initializing)
        view.store("draft", Codec.STRING, program.draft)
        view.store("console", Codec.STRING, program.console)
        view.store("cursor_position", Codec.INT, program.cursorPosition)
        view.store("has_error", Codec.BOOL, program.hasError)

        val rawProgram = Program(
            program.source,
            program.parsed,
            program.name,
            program.imports,
            program.statements,
            program.functions,
            program.structs,
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

    fun getPayload(): ControllerPayload {
        return ControllerPayload(
            blockPos,
            program.name,
            program.source,
            program.draft,
            program.console,
            program.cursorPosition,
            isDraft = false,
            run = false,
        )
    }

    override fun createMenu(syncId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu {
        return ControllerScreenHandler(syncId, playerInventory, getPayload())
    }

    override fun getScreenOpeningData(player: ServerPlayer): ControllerPayload {
        return getPayload()
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
            @Suppress("unused") blockState: BlockState,
            entity: ControllerBlockEntity
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
                entity.program.console += "${exception.message}\n"
                entity.program.hasError = true

                for (player in level((entity.level as ServerLevel))) {
                    ServerPlayNetworking.send(player, entity.getPayload())
                }
            }
        }
    }
}
