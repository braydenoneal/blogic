package com.braydenoneal.blogic.block;

import com.braydenoneal.blogic.BlogicMod;
import com.braydenoneal.blogic.item.ModItemGroup;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockView;

public class ModBlocks {
	public static final Block DIODE = registerBlock("diode", new DiodeBlock(fullLogicBlock(MapColor.LIGHT_GRAY)));
	public static final Block LIGHT_EMITTER = registerBlock("light_emitter", new LightEmitterBlock(fullLogicBlock(MapColor.DARK_GREEN)));
	public static final Block TOGGLE = registerBlock("toggle", new ToggleBlock(fullLogicBlock(MapColor.DARK_GREEN)));
	public static final Block AND_GATE = registerBlock("and_gate", new AndGateBlock(fullLogicBlock(MapColor.LIGHT_GRAY)));
	public static final Block OR_GATE = registerBlock("or_gate", new OrGateBlock(fullLogicBlock(MapColor.LIGHT_GRAY)));
	public static final Block XOR_GATE = registerBlock("xor_gate", new XorGateBlock(fullLogicBlock(MapColor.LIGHT_GRAY)));
	public static final Block INVERTER = registerBlock("inverter", new InverterBlock(fullLogicBlock(MapColor.LIGHT_GRAY)));
	public static final Block CABLE = registerBlock("cable", new CableBlock(powerTransmitter(MapColor.DARK_RED)));
	public static final Block ORANGE_CABLE = registerBlock("orange_cable", new OrangeCableBlock(powerTransmitter(MapColor.ORANGE)));
	public static final Block WHITE_CABLE = registerBlock("white_cable", new WhiteCableBlock(powerTransmitter(MapColor.WHITE)));
	public static final Block MAGENTA_CABLE = registerBlock("magenta_cable", new MagentaCableBlock(powerTransmitter(MapColor.MAGENTA)));
	public static final Block LIGHT_BLUE_CABLE = registerBlock("light_blue_cable", new LightBlueCableBlock(powerTransmitter(MapColor.LIGHT_BLUE)));
	public static final Block YELLOW_CABLE = registerBlock("yellow_cable", new YellowCableBlock(powerTransmitter(MapColor.YELLOW)));
	public static final Block LIME_CABLE = registerBlock("lime_cable", new LimeCableBlock(powerTransmitter(MapColor.LIME)));
	public static final Block PINK_CABLE = registerBlock("pink_cable", new PinkCableBlock(powerTransmitter(MapColor.PINK)));
	public static final Block GRAY_CABLE = registerBlock("gray_cable", new GrayCableBlock(powerTransmitter(MapColor.GRAY)));
	public static final Block LIGHT_GRAY_CABLE = registerBlock("light_gray_cable", new LightGrayCableBlock(powerTransmitter(MapColor.LIGHT_GRAY)));
	public static final Block CYAN_CABLE = registerBlock("cyan_cable", new CyanCableBlock(powerTransmitter(MapColor.CYAN)));
	public static final Block PURPLE_CABLE = registerBlock("purple_cable", new PurpleCableBlock(powerTransmitter(MapColor.PURPLE)));
	public static final Block BLUE_CABLE = registerBlock("blue_cable", new BlueCableBlock(powerTransmitter(MapColor.BLUE)));
	public static final Block BROWN_CABLE = registerBlock("brown_cable", new BrownCableBlock(powerTransmitter(MapColor.BROWN)));
	public static final Block GREEN_CABLE = registerBlock("green_cable", new GreenCableBlock(powerTransmitter(MapColor.GREEN)));
	public static final Block RED_CABLE = registerBlock("red_cable", new RedCableBlock(powerTransmitter(MapColor.RED)));
	public static final Block BLACK_CABLE = registerBlock("black_cable", new BlackCableBlock(powerTransmitter(MapColor.BLACK)));

	private static Block registerBlock(String name, Block block) {
		registerBlockItem(name, block);
		return Registry.register(Registry.BLOCK, new Identifier(BlogicMod.MOD_ID, name), block);
	}

	private static Item registerBlockItem(String name, Block block) {
		return Registry.register(Registry.ITEM, new Identifier(BlogicMod.MOD_ID, name), new BlockItem(block, new FabricItemSettings().group(ModItemGroup.BLOGIC)));
	}

	public static boolean never(BlockState state, BlockView world, BlockPos pos) {
		return false;
	}

	public static FabricBlockSettings fullLogicBlock(MapColor mapColor) {
		return FabricBlockSettings.of(new Material(mapColor, false, true, true, true, false, false, PistonBehavior.NORMAL)).strength(1f).solidBlock(ModBlocks::never);
	}

	public static FabricBlockSettings powerTransmitter(MapColor mapColor) {
		return FabricBlockSettings.of(new Material(mapColor, false, false, true, false, false, false, PistonBehavior.NORMAL)).strength(1f).solidBlock(ModBlocks::never).suffocates(ModBlocks::never).blockVision(ModBlocks::never).nonOpaque();
	}

	public static void registerModBlocks() {}
}
