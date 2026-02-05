package io.minecraft.flyconfig.item;

import io.minecraft.flyconfig.FlightManagement;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DeathProtectionComponent;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import java.util.function.Function;

public class ModItems {
    private ModItems() {
    }

    public static final Item METAL_SCRAP = registerItems("metal_scrap", Item::new, new Item(new Item.Settings()
            .maxCount(99).food(new FoodComponent.Builder().saturationModifier(0.3F).alwaysEdible().build()).rarity(Rarity.COMMON)));

    public static final Item AIRCRAFT_OIL = registerItems("aircraft_oil", Item::new, new Item(new Item.Settings()
            .maxCount(16).rarity(Rarity.UNCOMMON)));
    public static final Item FLIGHT_MANAGER = registerItems("flight_manager", Item::new, new Item(new Item.Settings()
            .maxCount(1).rarity(Rarity.EPIC).component(DataComponentTypes.DEATH_PROTECTION, DeathProtectionComponent.TOTEM_OF_UNDYING)));

    public static Item registerItems(String path, Function<Item.Settings, Item> factory, Item.Settings settings) {
        final RegistryKey<Item> registryKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of("tutorial", path));
        return Items.register(registryKey, factory, settings);
    }

    private static Item registerItems(String metalScrap, Function<Item.Settings, Item> aNew, Item item) {
        return item;
    }

    public static void registerModItems() {
        FlightManagement.LOGGER.info("Registering ModItems.");
    }
}
