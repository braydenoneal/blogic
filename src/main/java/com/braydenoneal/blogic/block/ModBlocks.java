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
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockView;

public class ModBlocks {
	public static final Block DIODE = registerBlock("diode",
			new DiodeBlock(FabricBlockSettings.of(Material.STONE).strength(1f)
					.solidBlock(ModBlocks::never)),
			ModItemGroup.BLOGIC);

	public static final Block LIGHT_EMITTER = registerBlock("light_emitter",
			new LightEmitterBlock(FabricBlockSettings.of(Material.STONE).strength(1f)
					.solidBlock(ModBlocks::never)),
			ModItemGroup.BLOGIC);

	public static final Block TOGGLE = registerBlock("toggle",
			new ToggleBlock(FabricBlockSettings.of(Material.STONE).strength(1f)
					.solidBlock(ModBlocks::never)),
			ModItemGroup.BLOGIC);

	public static final Block AND_GATE = registerBlock("and_gate",
			new AndGateBlock(FabricBlockSettings.of(Material.STONE).strength(1f)
					.solidBlock(ModBlocks::never)),
			ModItemGroup.BLOGIC);

	public static final Block OR_GATE = registerBlock("or_gate",
			new OrGateBlock(FabricBlockSettings.of(Material.STONE).strength(1f)
					.solidBlock(ModBlocks::never)),
			ModItemGroup.BLOGIC);

	public static final Block XOR_GATE = registerBlock("xor_gate",
			new XorGateBlock(FabricBlockSettings.of(Material.STONE).strength(1f)
					.solidBlock(ModBlocks::never)),
			ModItemGroup.BLOGIC);

	public static final Block INVERTER = registerBlock("inverter",
			new InverterBlock(FabricBlockSettings.of(Material.STONE).strength(1f)
					.solidBlock(ModBlocks::never)),
			ModItemGroup.BLOGIC);

	public static final Block CABLE = registerBlock("cable",
			new CableBlock(FabricBlockSettings.of(new Material(MapColor.DARK_RED, false, false, true, false, false, false, PistonBehavior.NORMAL)).strength(1f)
					.solidBlock(ModBlocks::never)
					.suffocates(ModBlocks::never)
					.blockVision(ModBlocks::never)
					.nonOpaque()
			),
			ModItemGroup.BLOGIC);

	public static final Block ORANGE_CABLE = registerBlock("orange_cable",
			new OrangeCableBlock(FabricBlockSettings.of(new Material(MapColor.ORANGE, false, false, true, false, false, false, PistonBehavior.NORMAL)).strength(1f)
					.solidBlock(ModBlocks::never)
					.suffocates(ModBlocks::never)
					.blockVision(ModBlocks::never)
					.nonOpaque()
			),
			ModItemGroup.BLOGIC);

	public static final Block WHITE_CABLE = registerBlock("white_cable",
			new WhiteCableBlock(FabricBlockSettings.of(new Material(MapColor.WHITE, false, false, true, false, false, false, PistonBehavior.NORMAL)).strength(1f)
					.solidBlock(ModBlocks::never)
					.suffocates(ModBlocks::never)
					.blockVision(ModBlocks::never)
					.nonOpaque()
			),
			ModItemGroup.BLOGIC);

	public static final Block MAGENTA_CABLE = registerBlock("magenta_cable",
			new MagentaCableBlock(FabricBlockSettings.of(new Material(MapColor.DARK_RED, false, false, true, false, false, false, PistonBehavior.NORMAL)).strength(1f)
					.solidBlock(ModBlocks::never)
					.suffocates(ModBlocks::never)
					.blockVision(ModBlocks::never)
					.nonOpaque()
			),
			ModItemGroup.BLOGIC);

	public static final Block LIGHT_BLUE_CABLE = registerBlock("light_blue_cable",
			new LightBlueCableBlock(FabricBlockSettings.of(new Material(MapColor.DARK_RED, false, false, true, false, false, false, PistonBehavior.NORMAL)).strength(1f)
					.solidBlock(ModBlocks::never)
					.suffocates(ModBlocks::never)
					.blockVision(ModBlocks::never)
					.nonOpaque()
			),
			ModItemGroup.BLOGIC);

	public static final Block YELLOW_CABLE = registerBlock("yellow_cable",
			new YellowCableBlock(FabricBlockSettings.of(new Material(MapColor.DARK_RED, false, false, true, false, false, false, PistonBehavior.NORMAL)).strength(1f)
					.solidBlock(ModBlocks::never)
					.suffocates(ModBlocks::never)
					.blockVision(ModBlocks::never)
					.nonOpaque()
			),
			ModItemGroup.BLOGIC);

	public static final Block LIME_CABLE = registerBlock("lime_cable",
			new LimeCableBlock(FabricBlockSettings.of(new Material(MapColor.DARK_RED, false, false, true, false, false, false, PistonBehavior.NORMAL)).strength(1f)
					.solidBlock(ModBlocks::never)
					.suffocates(ModBlocks::never)
					.blockVision(ModBlocks::never)
					.nonOpaque()
			),
			ModItemGroup.BLOGIC);

	public static final Block PINK_CABLE = registerBlock("pink_cable",
			new PinkCableBlock(FabricBlockSettings.of(new Material(MapColor.DARK_RED, false, false, true, false, false, false, PistonBehavior.NORMAL)).strength(1f)
					.solidBlock(ModBlocks::never)
					.suffocates(ModBlocks::never)
					.blockVision(ModBlocks::never)
					.nonOpaque()
			),
			ModItemGroup.BLOGIC);

	public static final Block GRAY_CABLE = registerBlock("gray_cable",
			new GrayCableBlock(FabricBlockSettings.of(new Material(MapColor.DARK_RED, false, false, true, false, false, false, PistonBehavior.NORMAL)).strength(1f)
					.solidBlock(ModBlocks::never)
					.suffocates(ModBlocks::never)
					.blockVision(ModBlocks::never)
					.nonOpaque()
			),
			ModItemGroup.BLOGIC);

	public static final Block LIGHT_GRAY_CABLE = registerBlock("light_gray_cable",
			new LightGrayCableBlock(FabricBlockSettings.of(new Material(MapColor.DARK_RED, false, false, true, false, false, false, PistonBehavior.NORMAL)).strength(1f)
					.solidBlock(ModBlocks::never)
					.suffocates(ModBlocks::never)
					.blockVision(ModBlocks::never)
					.nonOpaque()
			),
			ModItemGroup.BLOGIC);

	public static final Block CYAN_CABLE = registerBlock("cyan_cable",
			new CyanCableBlock(FabricBlockSettings.of(new Material(MapColor.DARK_RED, false, false, true, false, false, false, PistonBehavior.NORMAL)).strength(1f)
					.solidBlock(ModBlocks::never)
					.suffocates(ModBlocks::never)
					.blockVision(ModBlocks::never)
					.nonOpaque()
			),
			ModItemGroup.BLOGIC);

	public static final Block PURPLE_CABLE = registerBlock("purple_cable",
			new PurpleCableBlock(FabricBlockSettings.of(new Material(MapColor.DARK_RED, false, false, true, false, false, false, PistonBehavior.NORMAL)).strength(1f)
					.solidBlock(ModBlocks::never)
					.suffocates(ModBlocks::never)
					.blockVision(ModBlocks::never)
					.nonOpaque()
			),
			ModItemGroup.BLOGIC);

	public static final Block BLUE_CABLE = registerBlock("blue_cable",
			new BlueCableBlock(FabricBlockSettings.of(new Material(MapColor.DARK_RED, false, false, true, false, false, false, PistonBehavior.NORMAL)).strength(1f)
					.solidBlock(ModBlocks::never)
					.suffocates(ModBlocks::never)
					.blockVision(ModBlocks::never)
					.nonOpaque()
			),
			ModItemGroup.BLOGIC);

	public static final Block BROWN_CABLE = registerBlock("brown_cable",
			new BrownCableBlock(FabricBlockSettings.of(new Material(MapColor.DARK_RED, false, false, true, false, false, false, PistonBehavior.NORMAL)).strength(1f)
					.solidBlock(ModBlocks::never)
					.suffocates(ModBlocks::never)
					.blockVision(ModBlocks::never)
					.nonOpaque()
			),
			ModItemGroup.BLOGIC);

	public static final Block GREEN_CABLE = registerBlock("green_cable",
			new GreenCableBlock(FabricBlockSettings.of(new Material(MapColor.DARK_RED, false, false, true, false, false, false, PistonBehavior.NORMAL)).strength(1f)
					.solidBlock(ModBlocks::never)
					.suffocates(ModBlocks::never)
					.blockVision(ModBlocks::never)
					.nonOpaque()
			),
			ModItemGroup.BLOGIC);

	public static final Block RED_CABLE = registerBlock("red_cable",
			new RedCableBlock(FabricBlockSettings.of(new Material(MapColor.DARK_RED, false, false, true, false, false, false, PistonBehavior.NORMAL)).strength(1f)
					.solidBlock(ModBlocks::never)
					.suffocates(ModBlocks::never)
					.blockVision(ModBlocks::never)
					.nonOpaque()
			),
			ModItemGroup.BLOGIC);

	public static final Block BLACK_CABLE = registerBlock("black_cable",
			new BlackCableBlock(FabricBlockSettings.of(new Material(MapColor.DARK_RED, false, false, true, false, false, false, PistonBehavior.NORMAL)).strength(1f)
					.solidBlock(ModBlocks::never)
					.suffocates(ModBlocks::never)
					.blockVision(ModBlocks::never)
					.nonOpaque()
			),
			ModItemGroup.BLOGIC);

	private static Block registerBlock(String name, Block block, ItemGroup itemGroup) {
		registerBlockItem(name, block, itemGroup);
		return Registry.register(Registry.BLOCK, new Identifier(BlogicMod.MOD_ID, name), block);
	}

	private static Item registerBlockItem(String name, Block block, ItemGroup itemGroup) {
		return Registry.register(
				Registry.ITEM, new Identifier(BlogicMod.MOD_ID, name),
				new BlockItem(block, new FabricItemSettings().group(itemGroup)));
	}

	private static boolean never(BlockState state, BlockView world, BlockPos pos) {
		return false;
	}

	public static void registerModBlocks() {}
}
