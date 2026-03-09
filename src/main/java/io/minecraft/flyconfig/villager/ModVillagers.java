package io.minecraft.flyconfig.villager;

import com.google.common.collect.ImmutableSet;
import io.minecraft.flyconfig.sound.ModSoundEvents;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.poi.PointOfInterestType;
import net.minecraft.world.poi.PointOfInterestTypes;
import org.jetbrains.annotations.Nullable;

public class ModVillagers {
    public static final RegistryKey<VillagerProfession> FUHRER_LECTERN = of("fuhrer_lectern");

    private static RegistryKey<VillagerProfession> of(String id) {
        return RegistryKey.of(RegistryKeys.VILLAGER_PROFESSION, Identifier.of("flyconfig", id));
    }

    private static VillagerProfession register(String id, RegistryKey<PointOfInterestType> heldWorkstation, @Nullable SoundEvent workSound) {
        return Registry.register(Registries.VILLAGER_PROFESSION, Identifier.of("flyconfig", id),
                new VillagerProfession(
                        Text.translatable("entity.flyconfig." + id),
                        entry -> entry.matchesKey(heldWorkstation),
                        entry -> entry.matchesKey(heldWorkstation),
                        ImmutableSet.of(), ImmutableSet.of(), workSound
                ));
    }

    public static VillagerProfession registerAndGetDefault(Registry<VillagerProfession> registry) {
        return register("fuhrer_lectern", ModPointOfInterestTypes.FUHRER_LECTERN, ModSoundEvents.FUHRER_LECTERN);
    }

    public static void init() {
    }
}
