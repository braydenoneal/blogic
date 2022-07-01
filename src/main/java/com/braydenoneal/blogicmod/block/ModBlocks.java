package com.braydenoneal.blogicmod.block;

import com.braydenoneal.blogicmod.BlogicMod;
import com.braydenoneal.blogicmod.item.ModItemGroup;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModBlocks {
	public static final Block DIODE = registerBlock("diode",
			new DiodeBlock(FabricBlockSettings.of(Material.STONE).strength(1f)
					.solidBlock((state, world, pos) -> false)),
			ModItemGroup.BLOGIC);

	public static final Block LIGHT_EMITTER = registerBlock("light_emitter",
			new LightEmitterBlock(FabricBlockSettings.of(Material.STONE).strength(1f)
					.solidBlock((state, world, pos) -> false)),
			ModItemGroup.BLOGIC);

	public static final Block TOGGLE = registerBlock("toggle",
			new ToggleBlock(FabricBlockSettings.of(Material.STONE).strength(1f)
					.solidBlock((state, world, pos) -> false)),
			ModItemGroup.BLOGIC);

	public static final Block AND_GATE = registerBlock("and_gate",
			new AndGateBlock(FabricBlockSettings.of(Material.STONE).strength(1f)
					.solidBlock((state, world, pos) -> false)),
			ModItemGroup.BLOGIC);

	public static final Block OR_GATE = registerBlock("or_gate",
			new OrGateBlock(FabricBlockSettings.of(Material.STONE).strength(1f)
					.solidBlock((state, world, pos) -> false)),
			ModItemGroup.BLOGIC);

	public static final Block XOR_GATE = registerBlock("xor_gate",
			new XorGateBlock(FabricBlockSettings.of(Material.STONE).strength(1f)
					.solidBlock((state, world, pos) -> false)),
			ModItemGroup.BLOGIC);

	public static final Block INVERTER = registerBlock("inverter",
			new InverterBlock(FabricBlockSettings.of(Material.STONE).strength(1f)
					.solidBlock((state, world, pos) -> false)),
			ModItemGroup.BLOGIC);

	public static final Block CABLE = registerBlock("cable",
			new Block(FabricBlockSettings.of(Material.STONE).strength(1f)
					.solidBlock((state, world, pos) -> false)
					.suffocates((state, world, pos) -> false)
					.blockVision((state, world, pos) -> false)
					//.noOcclusion() equivalent
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

	public static void registerModBlocks() {}
}
