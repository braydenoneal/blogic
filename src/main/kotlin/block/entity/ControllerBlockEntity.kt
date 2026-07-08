package block.entity

import blang.BlogicProgram
import blang.Context
import blang.codec.Codecs
import block.CableBlock
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.LockableContainerBlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper.WrapperLookup
import net.minecraft.screen.ScreenHandler
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.state.property.Properties
import net.minecraft.storage.ReadView
import net.minecraft.storage.WriteView
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.RedstoneView
import net.minecraft.world.World
import parser.Program
import parser.Program.Companion.log
import java.util.*

class ControllerBlockEntity(pos: BlockPos, state: BlockState) : BlockEntity(ModBlockEntities.CONTROLLER_BLOCK_ENTITY, pos, state), ExtendedScreenHandlerFactory<BlockPos> {
    var program: BlogicProgram = BlogicProgram(Context(pos, this), "name;")
    var initializing = true

    fun setSource(source: String) {
        program.source = source

        if (!world!!.isClient) {
            program.parse()
            initializing = true
        }

        markDirty()
    }

    override fun readData(view: ReadView) {
        super.readData(view)
        val rawProgram = view.read("raw_program", Codecs.PROGRAM_CODEC).get()//.getOrNull() ?: Program("name;")

        program = BlogicProgram(
            Context(pos, this),
            rawProgram.source,
            rawProgram.parsed,
            rawProgram.name,
            rawProgram.imports,
            rawProgram.statements,
            rawProgram.functions,
            rawProgram.scopes,
        )
    }

    override fun writeData(view: WriteView) {
        super.writeData(view)
        val rawProgram = Program(
            program.source,
            program.parsed,
            program.name,
            program.imports,
            program.statements,
            program.functions,
            program.scopes,
        )

        view.put("raw_program", Codecs.PROGRAM_CODEC, rawProgram)
    }

    override fun toInitialChunkDataNbt(registryLookup: WrapperLookup): NbtCompound {
        return createNbt(registryLookup)
    }

    val facing: Direction get() = cachedState.get(Properties.FACING)

    override fun getDisplayName(): Text {
        return Text.translatable(cachedState.block.getTranslationKey())
    }

    override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler {
        return ControllerScreenHandler(syncId, playerInventory, this)
    }

    override fun getScreenOpeningData(player: ServerPlayerEntity): BlockPos {
        return pos
    }

    fun getConnectedControllerBlockEntities(): MutableList<ControllerBlockEntity> {
        val cables: MutableSet<BlockPos> = HashSet()
        val networkBlocks: MutableList<BlockPos> = ArrayList()

        val stack = Stack<BlockPos>()

        for (direction in RedstoneView.DIRECTIONS) {
            val adjacentPos = pos.offset(direction)

            val adjacentBlock = world!!.getBlockState(adjacentPos).block

            if (adjacentBlock is CableBlock) {
                stack.push(adjacentPos)
                cables.add(adjacentPos)
            }
        }

        while (!stack.isEmpty()) {
            val pos = stack.pop()

            for (direction in RedstoneView.DIRECTIONS) {
                val adjacentPos = pos.offset(direction)

                if (cables.contains(adjacentPos)) {
                    continue
                }

                val adjacentBlock = world!!.getBlockState(adjacentPos).block

                if (adjacentBlock is CableBlock) {
                    stack.push(adjacentPos)
                    cables.add(adjacentPos)
                }

                if (networkBlocks.contains(adjacentPos)) {
                    continue
                }

                val adjacentBlockEntity = world!!.getBlockEntity(adjacentPos)

                if (adjacentBlockEntity is ControllerBlockEntity) {
                    networkBlocks.add(adjacentPos)
                }
            }
        }

        val controllers: MutableList<ControllerBlockEntity> = ArrayList()

        for (pos in networkBlocks) {
            val adjacentBlockEntity = world!!.getBlockEntity(pos)

            if (adjacentBlockEntity is ControllerBlockEntity) {
                controllers.add(adjacentBlockEntity)
            }
        }

        return controllers
    }

    fun getConnectedContainers(): MutableList<LockableContainerBlockEntity> {
        val cables: MutableSet<BlockPos> = HashSet()
        val networkBlocks: MutableList<BlockPos> = ArrayList()

        val stack = Stack<BlockPos>()

        for (direction in RedstoneView.DIRECTIONS) {
            val adjacentPos = pos.offset(direction)

            val adjacentBlock = world!!.getBlockState(adjacentPos).block

            if (adjacentBlock is CableBlock) {
                stack.push(adjacentPos)
                cables.add(adjacentPos)
            }
        }

        while (!stack.isEmpty()) {
            val pos = stack.pop()

            for (direction in RedstoneView.DIRECTIONS) {
                val adjacentPos = pos.offset(direction)

                if (cables.contains(adjacentPos)) {
                    continue
                }

                val adjacentBlock = world!!.getBlockState(adjacentPos).block

                if (adjacentBlock is CableBlock) {
                    stack.push(adjacentPos)
                    cables.add(adjacentPos)
                }

                if (networkBlocks.contains(adjacentPos)) {
                    continue
                }

                val adjacentBlockEntity = world!!.getBlockEntity(adjacentPos)

                if (adjacentBlockEntity is LockableContainerBlockEntity) {
                    networkBlocks.add(adjacentPos)
                }
            }
        }

        val controllers: MutableList<LockableContainerBlockEntity> = ArrayList()

        for (pos in networkBlocks) {
            val adjacentBlockEntity = world!!.getBlockEntity(pos)

            if (adjacentBlockEntity is LockableContainerBlockEntity) {
                controllers.add(adjacentBlockEntity)
            }
        }

        return controllers
    }

    companion object {
        fun tick(world: World, blockPos: BlockPos, ignoredBlockState: BlockState, entity: ControllerBlockEntity) {
            if (!entity.program.parsed) {
                entity.program.parse()
                entity.markDirty()
            }

            if (entity.initializing) {
                try {
                    val result = entity.program.tick()

                    if (result != null) {
                        entity.initializing = false
                    }

                    entity.markDirty()
                } catch (e: Exception) {
                    // TODO: Fix this such that it doesn't try to re-run the code every tick when it has an exception
                    log.error("Run main error", e)
                }

                return
            }

            if (world.isReceivingRedstonePower(blockPos)) {
                entity.program.runMain()
                entity.markDirty()
            }
        }
    }
}
