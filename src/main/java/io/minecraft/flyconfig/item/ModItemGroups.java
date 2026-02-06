package io.minecraft.flyconfig.item;

import io.minecraft.flyconfig.FlightManagement;
import io.minecraft.flyconfig.enchantment.ModEnchantments;
import io.minecraft.flyconfig.item.ModItems;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.entity.ai.brain.MemoryQuery;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Optional;
import java.util.stream.IntStream;

import static io.minecraft.flyconfig.FlightManagement.MOD_ID;

public class ModItemGroups {
    public static final ItemGroup FLYCOMFIG_GROUP = Registry.register(Registries.ITEM_GROUP, Identifier.of(MOD_ID, "flyconfig-group"),
            ItemGroup.create(null, -1).displayName(Text.translatable("itemGroup.flyconfig-group"))
                    .icon(() -> new ItemStack(ModItems.FLIGHT_MANAGER))
                    .entries((displayContext, entries) -> {
                        entries.add(ModItems.METAL_SCRAP);
                        entries.add(ModItems.AIRCRAFT_OIL);
                        entries.add(ModItems.FLIGHT_MANAGER);
                        entries.add(ModItems.GERMANY_DEVELOPED);

                        displayContext.lookup().getOptional(RegistryKeys.ENCHANTMENT).ifPresent(registryWrapper -> {
                            addCustomLevelEnchantedBooks(entries, registryWrapper, ModEnchantments.LOUIS,
                                    ItemGroup.StackVisibility.PARENT_TAB_ONLY,
                                    1, 3, 5, 6, 10, 16, 32, 64, 128, 255);
                        });
                    }).build());

    private static void addCustomLevelEnchantedBooks(
            ItemGroup.Entries entries,
            RegistryWrapper<Enchantment> registryWrapper,
            RegistryKey<Enchantment> enchantmentKey,
            ItemGroup.StackVisibility stackVisibility,
            int... levels
    ) {
        Optional<RegistryEntry.Reference<Enchantment>> enchantmentEntryOpt = registryWrapper.getOptional(enchantmentKey);
        enchantmentEntryOpt.ifPresent(enchantmentEntry -> {
            for (int level : levels) {
                ItemStack enchantedBook = EnchantmentHelper.getEnchantedBookWith(
                        new EnchantmentLevelEntry(enchantmentEntry, level)
                );
                entries.add(enchantedBook, stackVisibility);
            }
        });
    }

    public static void registerModItemGroups() {
        FlightManagement.LOGGER.info("[" + MOD_ID + "] Item Group registration completed");
    }
}