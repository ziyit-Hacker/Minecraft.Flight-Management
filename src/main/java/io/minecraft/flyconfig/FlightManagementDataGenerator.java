package io.minecraft.flyconfig;

import io.minecraft.flyconfig.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.client.data.ItemModelGenerator;
import net.minecraft.client.data.Models;

public class FlightManagementDataGenerator implements DataGeneratorEntrypoint {
	public void generateItemModels(ItemModelGenerator itemModelGenerator) {
		itemModelGenerator.register(ModItems.METAL_SCRAP, Models.GENERATED);
		itemModelGenerator.register(ModItems.AIRCRAFT_OIL, Models.GENERATED);
		itemModelGenerator.register(ModItems.FLIGHT_MANAGER, Models.GENERATED);
	}

	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {

	}
}
