package net.assassinreport.copperrails.block;

import net.assassinreport.copperrails.CopperRails;
import net.assassinreport.copperrails.block.custom.CrossingRailBlock;
import net.assassinreport.copperrails.block.custom.GenericCopperRailBlock;
import net.minecraft.block.AbstractBlock;
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
            new GenericCopperRailBlock(AbstractBlock.Settings.copy(Blocks.POWERED_RAIL).mapColor(MapColor.ORANGE)));

    public static final Block RAIL_CROSSING= registerBlock("rail_crossing",
            new CrossingRailBlock(AbstractBlock.Settings.copy(Blocks.POWERED_RAIL)));

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(CopperRails.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, Identifier.of(CopperRails.MOD_ID, name),
                new BlockItem(block, new Item.Settings()));
    }

    public static void registerModBlocks() {
        CopperRails.LOGGER.info("Registering ModBlocks for " + CopperRails.MOD_ID);
    }
}