package net.assassinreport.copperrails;

import net.assassinreport.copperrails.item.ModItemGroups;
import net.fabricmc.api.ModInitializer;

import net.assassinreport.copperrails.block.ModBlocks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CopperRails implements ModInitializer {
	public static final String MOD_ID = "copperrails";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModBlocks.registerModBlocks();
		ModItemGroups.registerItemsToVanillaGroups();
		LOGGER.info("Initializing Copper Rails !");
	}
}