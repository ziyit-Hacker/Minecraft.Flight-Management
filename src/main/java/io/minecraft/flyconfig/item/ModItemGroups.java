package io.minecraft.flyconfig.item;

import io.minecraft.flyconfig.FlightManagement;
import io.minecraft.flyconfig.block.ModBlocks;
import io.minecraft.flyconfig.item.ModItems;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.entity.ai.brain.MemoryQuery;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Optional;
import java.util.stream.IntStream;

import static io.minecraft.flyconfig.FlightManagement.MOD_ID;

public class ModItemGroups {
    private static ItemStack createNamedItem(Item item, String name, int id) {
        ItemStack stack = new ItemStack(item, 1);
        stack.set(DataComponentTypes.ITEM_NAME, Text.literal(name));

        NbtCompound nbt = new NbtCompound();
        nbt.putInt("placeholder_id", id);
        stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(nbt));

        return stack;
    }

    public static final ItemGroup FLYCONFIG_GROUP = Registry.register(Registries.ITEM_GROUP, Identifier.of(MOD_ID, "flyconfig-group"),
            ItemGroup.create(null, -1).displayName(Text.translatable("itemGroup.flyconfig-group"))
                    .icon(() -> new ItemStack(ModItems.FLIGHT_MANAGER))
                    .entries((displayContext, entries) -> {
                        entries.add(ModItems.METAL_SCRAP);
                        entries.add(ModItems.AIRCRAFT_OIL);
                        entries.add(ModItems.FLIGHT_MANAGER);
                        entries.add(ModItems.GERMANY_DEVELOPED);
                        entries.add(ModItems.ULHEALN);
                        entries.add(ModItems.NODATA);
                        entries.add(ModItems.MUSIC_DISC_SIEG_HEIL);
                        entries.add(ModItems.MUSIC_DISC_HITLER);
                        entries.add(ModItems.NUCLEAR_SPAWN_EGG);
                        entries.add(ModItems.NUCLEAR_DISARM_TOOL);
                        entries.add(ModItems.URANIUM_INGOT);
                        entries.add(ModBlocks.URANIUM_ORE.asItem());
                        entries.add(ModBlocks.DEEPSLATE_URANIUM_ORE.asItem());

                        //分割线
                        entries.add(createNamedItem(Items.LIGHT_GRAY_STAINED_GLASS_PANE, " ", 1));
                        entries.add(createNamedItem(Items.LIGHT_GRAY_STAINED_GLASS_PANE, " ", 2));
                        entries.add(createNamedItem(Items.LIGHT_GRAY_STAINED_GLASS_PANE, " ", 3));
                        entries.add(createNamedItem(Items.LIGHT_GRAY_STAINED_GLASS_PANE, " ", 4));
                        entries.add(createNamedItem(Items.LIGHT_GRAY_STAINED_GLASS_PANE, " ", 5));

                        entries.add(createNamedItem(Items.LIGHT_GRAY_STAINED_GLASS_PANE, " ", 11));
                        entries.add(createNamedItem(Items.LIGHT_GRAY_STAINED_GLASS_PANE, " ", 12));
                        entries.add(createNamedItem(Items.LIGHT_GRAY_STAINED_GLASS_PANE, " ", 13));
                        entries.add(createNamedItem(Items.LIGHT_GRAY_STAINED_GLASS_PANE, " ", 14));
                        entries.add(createNamedItem(Items.LIGHT_GRAY_STAINED_GLASS_PANE, " ", 15));
                        entries.add(createNamedItem(Items.LIGHT_GRAY_STAINED_GLASS_PANE, " ", 16));
                        entries.add(createNamedItem(Items.LIGHT_GRAY_STAINED_GLASS_PANE, " ", 17));
                        entries.add(createNamedItem(Items.LIGHT_GRAY_STAINED_GLASS_PANE, " ", 18));
                        entries.add(createNamedItem(Items.LIGHT_GRAY_STAINED_GLASS_PANE, " ", 19));

                        // 违规方块
                        entries.add(ModItems.SNOWY_GRASS_BLOCK);
                        entries.add(ModItems.NETHER_PORTAL);
                        entries.add(ModItems.END_PORTAL);
                        entries.add(ModItems.END_GATEWAY);
                        entries.add(ModItems.WATER);
                        entries.add(ModItems.LAVA);
                        entries.add(ModItems.FLOWING_WATER);
                        entries.add(ModItems.FLOWING_LAVA);
                        entries.add(ModItems.BUBBLE_COLUMN_UP);
                        entries.add(ModItems.BUBBLE_COLUMN_DOWN);
                        entries.add(ModItems.LIT_FURNACE);
                        entries.add(ModItems.LIT_BLAST_FURNACE);
                        entries.add(ModItems.LIT_SMOKER);
                        entries.add(ModItems.ACTIVATED_OBSERVER);
                        entries.add(ModItems.SIX_SIDED_PISTON);
                        entries.add(ModItems.HEADLESS_PISTON);
                        entries.add(ModItems.PISTON_ARM);
                        entries.add(ModItems.WET_FARMLAND);
                        entries.add(ModItems.FIRE);
                        entries.add(ModItems.SOUL_FIRE);
                        // 在违规方块部分之后添加
                        // ===== 农作物（所有生长阶段） =====
                        // 小麦
                        entries.add(ModItems.WHEAT_CROP_0);
                        entries.add(ModItems.WHEAT_CROP_1);
                        entries.add(ModItems.WHEAT_CROP_2);
                        entries.add(ModItems.WHEAT_CROP_3);
                        entries.add(ModItems.WHEAT_CROP_4);
                        entries.add(ModItems.WHEAT_CROP_5);
                        entries.add(ModItems.WHEAT_CROP_6);
                        entries.add(ModItems.WHEAT_CROP_7);

                        // 胡萝卜
                        entries.add(ModItems.CARROT_CROP_0);
                        entries.add(ModItems.CARROT_CROP_1);
                        entries.add(ModItems.CARROT_CROP_2);
                        entries.add(ModItems.CARROT_CROP_3);
                        entries.add(ModItems.CARROT_CROP_4);
                        entries.add(ModItems.CARROT_CROP_5);
                        entries.add(ModItems.CARROT_CROP_6);
                        entries.add(ModItems.CARROT_CROP_7);

                        // 马铃薯
                        entries.add(ModItems.POTATO_CROP_0);
                        entries.add(ModItems.POTATO_CROP_1);
                        entries.add(ModItems.POTATO_CROP_2);
                        entries.add(ModItems.POTATO_CROP_3);
                        entries.add(ModItems.POTATO_CROP_4);
                        entries.add(ModItems.POTATO_CROP_5);
                        entries.add(ModItems.POTATO_CROP_6);
                        entries.add(ModItems.POTATO_CROP_7);

                        // 甜菜根
                        entries.add(ModItems.BEETROOT_CROP_0);
                        entries.add(ModItems.BEETROOT_CROP_1);
                        entries.add(ModItems.BEETROOT_CROP_2);
                        entries.add(ModItems.BEETROOT_CROP_3);

                        // 西瓜茎
                        entries.add(ModItems.MELON_STEM_0);
                        entries.add(ModItems.MELON_STEM_1);
                        entries.add(ModItems.MELON_STEM_2);
                        entries.add(ModItems.MELON_STEM_3);
                        entries.add(ModItems.MELON_STEM_4);
                        entries.add(ModItems.MELON_STEM_5);
                        entries.add(ModItems.MELON_STEM_6);
                        entries.add(ModItems.MELON_STEM_7);

                        // 南瓜茎
                        entries.add(ModItems.PUMPKIN_STEM_0);
                        entries.add(ModItems.PUMPKIN_STEM_1);
                        entries.add(ModItems.PUMPKIN_STEM_2);
                        entries.add(ModItems.PUMPKIN_STEM_3);
                        entries.add(ModItems.PUMPKIN_STEM_4);
                        entries.add(ModItems.PUMPKIN_STEM_5);
                        entries.add(ModItems.PUMPKIN_STEM_6);
                        entries.add(ModItems.PUMPKIN_STEM_7);

                        // 甘蔗
                        entries.add(ModItems.SUGAR_CANE_0);
                        entries.add(ModItems.SUGAR_CANE_1);
                        entries.add(ModItems.SUGAR_CANE_2);
                        entries.add(ModItems.SUGAR_CANE_3);
                        entries.add(ModItems.SUGAR_CANE_4);
                        entries.add(ModItems.SUGAR_CANE_5);
                        entries.add(ModItems.SUGAR_CANE_6);
                        entries.add(ModItems.SUGAR_CANE_7);
                        entries.add(ModItems.SUGAR_CANE_8);
                        entries.add(ModItems.SUGAR_CANE_9);
                        entries.add(ModItems.SUGAR_CANE_10);
                        entries.add(ModItems.SUGAR_CANE_11);
                        entries.add(ModItems.SUGAR_CANE_12);
                        entries.add(ModItems.SUGAR_CANE_13);
                        entries.add(ModItems.SUGAR_CANE_14);
                        entries.add(ModItems.SUGAR_CANE_15);
                    }).build());

    public static void registerModItemGroups() {
        FlightManagement.LOGGER.info("[" + MOD_ID + "] Item Group registration completed");
    }
}