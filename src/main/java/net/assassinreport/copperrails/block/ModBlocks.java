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
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ModBlocks {

    public static final Block COPPER_RAIL = registerBlock("copper_rail",
            new GenericCopperRailBlock(AbstractBlock.Settings.copy(Blocks.POWERED_RAIL)
                    .mapColor(MapColor.ORANGE)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(CopperRails.MOD_ID, "copper_rail")))));

    public static final Block RAIL_CROSSING = registerBlock("rail_crossing",
            new CrossingRailBlock(AbstractBlock.Settings.copy(Blocks.POWERED_RAIL)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(CopperRails.MOD_ID, "rail_crossing")))));

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(CopperRails.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, Identifier.of(CopperRails.MOD_ID, name),
                new BlockItem(block, new Item.Settings()
                        .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(CopperRails.MOD_ID, name)))));
    }

    public static void registerModBlocks() {
        CopperRails.LOGGER.info("Registering ModBlocks for " + CopperRails.MOD_ID);
    }
}