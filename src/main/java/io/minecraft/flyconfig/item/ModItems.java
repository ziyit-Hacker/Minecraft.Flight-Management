package io.minecraft.flyconfig.item;

import io.minecraft.flyconfig.FlightManagement;
import io.minecraft.flyconfig.block.ModBlock;
import io.minecraft.flyconfig.sound.ModJukeboxSongs;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ConsumableComponents;
import net.minecraft.component.type.DeathProtectionComponent;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import java.util.function.BiFunction;
import java.util.function.Function;

public class ModItems {
    private ModItems() {
    }

    public static final Item METAL_SCRAP = registerItems("metal_scrap", MetalScrapItem::new, new Item.Settings()
            .maxCount(99).food(ModFoodComponents.METAL_SCRAP, ConsumableComponents.FOOD));
    public static final Item AIRCRAFT_OIL = registerItems("synthetic_motoroil", Item::new, new Item.Settings()
            .maxCount(16).rarity(Rarity.UNCOMMON));
    public static final Item FLIGHT_MANAGER = registerItems("flight_manager", Item::new, new Item.Settings()
            .maxCount(1).rarity(Rarity.RARE).component(DataComponentTypes.DEATH_PROTECTION, DeathProtectionComponent.TOTEM_OF_UNDYING));
    public static final Item GERMANY_DEVELOPED = registerItems("germany_developed", Item::new, new Item.Settings()
            .maxCount(2).rarity(Rarity.EPIC));
    public static final Item ULHEALN = registerItems("ultimate_healthy_nightmare", ULHEALN::new, new Item.Settings()
            .maxCount(1).food(ModFoodComponents.ULHEALN, ConsumableComponents.FOOD).rarity(Rarity.EPIC));
    public static final Item NODATA = registerItems("what_the_dog_doing", NODATA::new, new Item.Settings()
            .maxCount(1).food(ModFoodComponents.NODATA, ConsumableComponents.FOOD).rarity(Rarity.EPIC));
    public static final Item MUSIC_DISC_SIEG_HEIL = registerItems("music_disc_sieg_heil", Item::new, new Item.Settings()
            .maxCount(1).rarity(Rarity.RARE).jukeboxPlayable(ModJukeboxSongs.SIEG_HEIL));

    public static final Item FUHRER_LECTERN = register(ModBlock.FUHRER_LECTERN);

    public static Item registerItems(String path, Function<Item.Settings, Item> factory, Item.Settings settings) {
        final RegistryKey<Item> registryKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of("flyconfig", path));
        return Items.register(registryKey, factory, settings);
    }

    public static Item register(Block block) {
        return register(block, BlockItem::new);
    }

    public static Item register(Block block, BiFunction<Block, Item.Settings, Item> factory) {
        return register(block, factory, new Item.Settings());
    }

    public static Item register(Block block, BiFunction<Block, Item.Settings, Item> factory, Item.Settings settings) {
        return register(
                keyOf(block.getRegistryEntry().registryKey()), itemSettings -> (Item)factory.apply(block, itemSettings), settings.useBlockPrefixedTranslationKey()
        );
    }

    public static Item register(RegistryKey<Item> key, Function<Item.Settings, Item> factory, Item.Settings settings) {
        Item item = (Item)factory.apply(settings.registryKey(key));
        if (item instanceof BlockItem blockItem) {
            blockItem.appendBlocks(Item.BLOCK_ITEMS, item);
        }

        return Registry.register(Registries.ITEM, key, item);
    }

    private static RegistryKey<Item> keyOf(RegistryKey<Block> blockKey) {
        return RegistryKey.of(RegistryKeys.ITEM, blockKey.getValue());
    }

    public static void initialize() {
    }

    public static void registerModItems() {
        FlightManagement.LOGGER.info("Registering ModItems.");
    }
}
