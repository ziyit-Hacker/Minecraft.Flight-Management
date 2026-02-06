package io.minecraft.flyconfig.enchantment;

import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import io.minecraft.flyconfig.FlightManagement;

public final class ModEnchantments {
    public static final RegistryKey<net.minecraft.enchantment.Enchantment> LOUIS = of("louis");

    private static RegistryKey<net.minecraft.enchantment.Enchantment> of(String path) {
        return RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(FlightManagement.MOD_ID, path));
    }

    public static void initialize() {
        FlightManagement.LOGGER.info("[" + FlightManagement.MOD_ID + "] Registering ModEnchantments.");
    }

    private ModEnchantments() {}
}