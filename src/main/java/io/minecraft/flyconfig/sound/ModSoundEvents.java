package io.minecraft.flyconfig.sound;

import io.minecraft.flyconfig.FlightManagement;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSoundEvents {
    public static final SoundEvent SB = register("skeleton_notfound");
    public static final SoundEvent DONK = register("skeleton_found");
    public static final SoundEvent BLOCK_FUHRER_BREAK = register("block_fuhrer_break");
    public static final SoundEvent BLOCK_FUHRER_STEP = register("block_fuhrer_step");
    public static final SoundEvent BLOCK_FUHRER_PLACE = register("block_fuhrer_place");
    public static final SoundEvent BLOCK_FUHRER_HIT = register("block_fuhrer_hit");
    public static final SoundEvent BLOCK_FUHRER_FALL = register("block_fuhrer_fall");
    public static final SoundEvent FUHRER_LECTERN = register("fuhrer_lectern");

    public static final BlockSoundGroup FUHRER = new BlockSoundGroup(1.0f, 1.0f,
            BLOCK_FUHRER_BREAK, BLOCK_FUHRER_STEP, BLOCK_FUHRER_PLACE, BLOCK_FUHRER_HIT, BLOCK_FUHRER_FALL);

    private static SoundEvent register(String name) {
        Identifier id = Identifier.of("flyconfig", name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static final RegistryEntry.Reference<SoundEvent> MUSIC_DISC_SIEG_HEIL = registerReference("music_disc.siegheil");

    private static RegistryEntry.Reference<SoundEvent> registerReference(String name) {
        Identifier id = Identifier.of("flyconfig", name);
        return Registry.registerReference(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void init() {
        FlightManagement.LOGGER.info("Sound/Music Initialization Complete");
    }
}
