package net.assassinreport.copperrails.item;

import net.assassinreport.copperrails.CopperRails;
import net.assassinreport.copperrails.block.ModBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroups;

public class ModItemGroups {
    public static void registerItemsToVanillaGroups() {

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE).register(entries -> {
            entries.add(ModBlocks.RAIL_CROSSING);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE).register(entries -> {
            entries.add(ModBlocks.COPPER_RAIL);
        });
    }


    public static void registerItemGroups() {
        CopperRails.LOGGER.info("Registering Item Groups for " + CopperRails.MOD_ID);
    }
}
