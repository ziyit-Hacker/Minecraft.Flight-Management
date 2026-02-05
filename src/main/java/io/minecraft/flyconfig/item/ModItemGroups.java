package io.minecraft.flyconfig.item;

import io.minecraft.flyconfig.FlightManagement;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {
//    public static final RegistryKey<ItemGroup> FLYCOMFIG_GROUP = register("flyconfig_group");
//
//    private static RegistryKey<ItemGroup> register(String id) {
//        return RegistryKey.of(RegistryKeys.ITEM_GROUP, Identifier.of(FlightManagement.MOD_ID, id));
//    }
//
//    public static void registerItemGroups() {
//        Registry.register(Registries.ITEM_GROUP, FLYCOMFIG_GROUP,
//                ItemGroup.create(ItemGroup.Row.TOP, 7)
//                        .displayName(Text.translatable("itemGroup.flyconfig_group"))
//                        .icon(() -> new ItemStack(ModItems.FLIGHT_MANAGER))
//                        .entries((displayContext, entries) -> {
//                            entries.add(ModItems.METAL_SCRAP);
//                            entries.add(ModItems.AIRCRAFT_OIL);
//                            entries.add(ModItems.FLIGHT_MANAGER);
//                        }).build());
//
//        FlightManagement.LOGGER.info("Registering Item Groups for " + FlightManagement.MOD_ID);
//    }

    public static final ItemGroup FLYCOMFIG_GROUP = Registry.register(Registries.ITEM_GROUP, Identifier.of(FlightManagement.MOD_ID, "flyconfig-group"),
            ItemGroup.create(null, -1).displayName(Text.translatable("itemGroup.flyconfig_group"))
                    .icon(() -> new ItemStack(ModItems.FLIGHT_MANAGER))
                    .entries((displayContext, entries) -> {
                        entries.add(ModItems.METAL_SCRAP);
                        entries.add(ModItems.AIRCRAFT_OIL);
                        entries.add(ModItems.FLIGHT_MANAGER);
                    }).build());

    public static void registerModItemGroups() {
        FlightManagement.LOGGER.info("Registering Mod Item Groups for " + FlightManagement.MOD_ID);
    }
}
