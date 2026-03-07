package io.minecraft.flyconfig.sound;

import io.minecraft.flyconfig.FlightManagement;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSoundEvents {
    public static final SoundEvent sb = register("skeleton_notfound");
    public static final SoundEvent donk = register("skeleton_found");

    private static SoundEvent register(String name) {
        Identifier id = Identifier.of("flyconfig", name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static final RegistryEntry.Reference<SoundEvent> MUSIC_DISC_SIEG_HEIL = registerReference("music_disc.siegHeil");

    private static RegistryEntry.Reference<SoundEvent> registerReference(String name) {
        Identifier id = Identifier.of("flyconfig", name);
        return Registry.registerReference(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void init() {
        FlightManagement.LOGGER.info("Sound/Music Initialization Complete");
    }
}
