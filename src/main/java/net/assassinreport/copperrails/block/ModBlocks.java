package net.assassinreport.copperrails.block;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.assassinreport.copperrails.CopperRails;
import net.assassinreport.copperrails.block.custom.CrossingRailBlock;
import net.assassinreport.copperrails.block.custom.GenericCopperRailBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlocks {

    public static final Block COPPER_RAIL = registerBlock("copper_rail",
            new GenericCopperRailBlock(FabricBlockSettings.copyOf(Blocks.POWERED_RAIL).mapColor(MapColor.ORANGE)));

    public static final Block RAIL_CROSSING= registerBlock("rail_crossing",
            new CrossingRailBlock(FabricBlockSettings.copyOf(Blocks.POWERED_RAIL)));

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, new Identifier(CopperRails.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block) {
        return Registry.register(Registries.ITEM, new Identifier(CopperRails.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings()));
    }

    public static void registerModBlocks() {
        CopperRails.LOGGER.info("Registering ModBlocks for " + CopperRails.MOD_ID);
    }
}