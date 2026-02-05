package io.minecraft.flyconfig.item;

import io.minecraft.flyconfig.FlightManagement;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DeathProtectionComponent;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import java.lang.reflect.Field;

public class ModItems {
    private static final Item.Settings METAL_SCRAP_SETTINGS = new Item.Settings()
            .maxCount(99)
            .food(new FoodComponent.Builder().saturationModifier(0.3F).alwaysEdible().build())
            .rarity(Rarity.COMMON);

    private static final Item.Settings AIRCRAFT_OIL_SETTINGS = new Item.Settings()
            .maxCount(16)
            .rarity(Rarity.UNCOMMON);

    private static final Item.Settings FLIGHT_MANAGER_SETTINGS = new Item.Settings()
            .maxCount(1)
            .rarity(Rarity.EPIC)
            .component(DataComponentTypes.DEATH_PROTECTION, DeathProtectionComponent.TOTEM_OF_UNDYING);

    public static final Item METAL_SCRAP = registerItems("metal_scrap", new Item(METAL_SCRAP_SETTINGS), METAL_SCRAP_SETTINGS);
    public static final Item AIRCRAFT_OIL = registerItems("aircraft_oil", new Item(AIRCRAFT_OIL_SETTINGS), AIRCRAFT_OIL_SETTINGS);
    public static final Item FLIGHT_MANAGER = registerItems("flight_manager", new Item(FLIGHT_MANAGER_SETTINGS), FLIGHT_MANAGER_SETTINGS);

    private static Item registerItems(String id, Item item, Item.Settings settings) {
        RegistryKey<Item> registryKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of("flyconfig", id));

        try {
            Field keyField = Item.Settings.class.getDeclaredField("registryKey");
            keyField.setAccessible(true);
            keyField.set(settings, registryKey);
        } catch (NoSuchFieldException e) {
            FlightManagement.LOGGER.error("registryKey Not Find!", e);
        } catch (IllegalAccessException e) {
            FlightManagement.LOGGER.error("registryKey Not Find!", e);
        } catch (IllegalArgumentException e) {
            FlightManagement.LOGGER.error("RegistryKey NaN", e);
        }

        // 4. 标准注册：用 RegistryKey 注册物品（解决 Item id not set 异常）
        return Registry.register(Registries.ITEM, registryKey, item);
    }

    public static void registerModItems() {
        FlightManagement.LOGGER.info("Registering ModItems for flyconfig");
    }
}
