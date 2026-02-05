package io.minecraft.flyconfig.item;

import io.minecraft.flyconfig.FlightManagement;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DeathProtectionComponent;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import static io.minecraft.flyconfig.FlightManagement.MOD_ID;

public class ModItems {
    public static final Item METAL_SCRAP = registerItems("metal_scrap", new Item(new Item.Settings()
            .maxCount(99).food(new FoodComponent.Builder().saturationModifier(0.3F).alwaysEdible().build()).rarity(Rarity.COMMON)));
    public static final Item AIRCRAFT_OIL = registerItems("aircraft_oil", new Item(new Item.Settings()
            .maxCount(16).rarity(Rarity.UNCOMMON)));
    public static final Item FLIGHT_MANAGER = registerItems("flight_manager", new Item(new Item.Settings()
            .maxCount(1).rarity(Rarity.EPIC).component(DataComponentTypes.DEATH_PROTECTION, DeathProtectionComponent.TOTEM_OF_UNDYING)));

    private static Item registerItems(String id, Item item) {
        // return Registry.register(Registries.ITEM, RegistryKey.of(Registries.ITEM.getKey(), Identifier.of(MOD_ID, id)), item);
        return Registry.register(Registries.ITEM, Identifier.of(MOD_ID, id), item);
    }

    public static void registerModItems() {
        FlightManagement.LOGGER.info("Registering ModItems.");
    }

    { I Am Error =( }
}
