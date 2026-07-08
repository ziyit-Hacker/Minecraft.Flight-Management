package io.minecraft.flyconfig.item;

import io.minecraft.flyconfig.FlightManagement;
import io.minecraft.flyconfig.entity.ModEntities;
import io.minecraft.flyconfig.sound.ModJukeboxSongs;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ConsumableComponents;
import net.minecraft.component.type.DeathProtectionComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.world.World;
import net.minecraft.entity.EquipmentSlot;
import org.jetbrains.annotations.Nullable;

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
    public static final Item MUSIC_DISC_HITLER = registerItems("music_disc_hitler", Item::new, new Item.Settings()
            .maxCount(1).rarity(Rarity.RARE).jukeboxPlayable(ModJukeboxSongs.HITLER));
    public static final Item NUCLEAR_SPAWN_EGG = registerItems("nuclear_spawn_egg",
            settings -> new SpawnEggItem(ModEntities.NUCLEAR, settings) {
                @Override
                public void inventoryTick(ItemStack stack, ServerWorld world, Entity entity, @Nullable EquipmentSlot slot) {
                    if (entity instanceof LivingEntity living) {
                        if (slot == EquipmentSlot.MAINHAND || slot == EquipmentSlot.OFFHAND) {
                            living.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 40, 0));
                        }
                    }
                    super.inventoryTick(stack, world, entity, slot);
                }
            },
            new Item.Settings().maxCount(1));

    public static Item registerItems(String path, Function<Item.Settings, Item> factory, Item.Settings settings) {
        Identifier itemId = Identifier.of("flyconfig", path);
        RegistryKey<Item> registryKey = RegistryKey.of(RegistryKeys.ITEM, itemId);
        Item item = factory.apply(settings.registryKey(registryKey));

        if (item instanceof BlockItem blockItem) {
            blockItem.appendBlocks(Item.BLOCK_ITEMS, item);
        }

        return Registry.register(Registries.ITEM, registryKey, item);
    }

    public static void initialize() {
    }

    public static void registerModItems() {
        FlightManagement.LOGGER.info("Registering ModItems.");
    }
}