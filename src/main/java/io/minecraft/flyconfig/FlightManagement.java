package io.minecraft.flyconfig;

import io.minecraft.flyconfig.block.ModBlocks;
import io.minecraft.flyconfig.entity.ModEntities;
import io.minecraft.flyconfig.entity.custom.NuclearEntity;
import io.minecraft.flyconfig.item.ModItemGroups;
import io.minecraft.flyconfig.item.ModItems;
import io.minecraft.flyconfig.sound.ModJukeboxSongs;
import io.minecraft.flyconfig.sound.ModSoundEvents;
import io.minecraft.flyconfig.util.ModCustomTrades;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.GenerationStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlightManagement implements ModInitializer {
	public static final String MOD_ID = "flight-management";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		ModBlocks.initialize();

		ModItems.registerModItems();
		ModItemGroups.registerModItemGroups();

		ModCustomTrades.registerModCustomTrades();

		ModSoundEvents.init();
		ModJukeboxSongs.init();

		ModEntities.register();
		FabricDefaultAttributeRegistry.register(ModEntities.NUCLEAR, NuclearEntity.createNuclearAttributes());

		BiomeModifications.addFeature(
				BiomeSelectors.foundInOverworld(),
				GenerationStep.Feature.UNDERGROUND_ORES,
				RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of("flyconfig", "ore_uranium"))
		);

		LOGGER.info("[" + MOD_ID + "] Item registration completed");
	}
}
