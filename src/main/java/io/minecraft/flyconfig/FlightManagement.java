package io.minecraft.flyconfig;

import io.minecraft.flyconfig.entity.DancingSkeletonEntity;
import io.minecraft.flyconfig.item.ModItemGroups;
import io.minecraft.flyconfig.item.ModItems;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlightManagement implements ModInitializer {
	public static final String MOD_ID = "flight-management";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final RegistryKey<EntityType<?>> DANCING_SKELETON_KEY = RegistryKey.of(
			RegistryKeys.ENTITY_TYPE,
			Identifier.of(MOD_ID, "dancing_skeleton")
	);

	public static final EntityType<DancingSkeletonEntity> DANCING_SKELETON =
			FabricEntityTypeBuilder.create(SpawnGroup.MONSTER,
							(EntityType.EntityFactory<DancingSkeletonEntity>) (type, world) ->
									new DancingSkeletonEntity(type, world))
					.dimensions(EntityDimensions.fixed(0.6F, 1.99F))
					.build(DANCING_SKELETON_KEY);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		Registry.register(Registries.ENTITY_TYPE, DANCING_SKELETON_KEY.getValue(), DANCING_SKELETON);

		ModItems.registerModItems();
		ModItemGroups.registerModItemGroups();

		FabricDefaultAttributeRegistry.register(DANCING_SKELETON, DancingSkeletonEntity.createSkeletonAttributes());

		LOGGER.info("[" + MOD_ID + "] Item registration completed");
		LOGGER.info("[" + MOD_ID + "] Entity registration completed");
	}
}
