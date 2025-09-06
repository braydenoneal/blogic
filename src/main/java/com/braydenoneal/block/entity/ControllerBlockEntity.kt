package com.braydenoneal.block.entity

import com.braydenoneal.blang.Context
import com.braydenoneal.blang.parser.Program
import com.braydenoneal.blang.parser.Scope
import com.braydenoneal.blang.parser.expression.value.Value
import com.braydenoneal.block.CableBlock
import com.mojang.serialization.Codec
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
import java.util.*
import java.util.Map

class ControllerBlockEntity(pos: BlockPos, state: BlockState) :
    BlockEntity(ModBlockEntities.CONTROLLER_BLOCK_ENTITY, pos, state), ExtendedScreenHandlerFactory<BlockPos> {
    private var program: Program
    private var variables: MutableMap<String, Value<*>>
    private var source: String = "name;"

    init {
        program = Program(source, Context(pos, this))
        variables = Map.of()
    }

    fun source(): String {
        return source
    }

    fun program(): Program {
        return program
    }

    fun setSource(source: String) {
        this.source = source

        if (!world!!.isClient) {
            program = Program(source, Context(pos, this))
            program.run()
            variables = program.topScope().variables()
        }

        markDirty()
    }

    override fun readData(view: ReadView) {
        super.readData(view)
        variables = view.read("variables", Scope.VARIABLES_CODEC).orElse(Map.of())
        source = view.read("source", Codec.STRING).orElse("name;")

        program = Program(source, Context(pos, this))
        program.topScope().setVariables(HashMap(variables))
    }

    override fun writeData(view: WriteView) {
        super.writeData(view)
        view.put("variables", Scope.VARIABLES_CODEC, variables)
        view.put("source", Codec.STRING, source)
    }

    override fun toInitialChunkDataNbt(registryLookup: WrapperLookup): NbtCompound {
        return createNbt(registryLookup)
    }

    val facing: Direction get() = cachedState.get(Properties.FACING)

    override fun getDisplayName(): Text {
        return Text.translatable(cachedState.block.getTranslationKey())
    }

    override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler {
        return ControllerScreenHandler(syncId, playerInventory, pos)
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
            if (world.isReceivingRedstonePower(blockPos)) {
                entity.program.runMain()
                entity.variables = entity.program.topScope().variables()
                entity.markDirty()
            }
        }
    }
}
