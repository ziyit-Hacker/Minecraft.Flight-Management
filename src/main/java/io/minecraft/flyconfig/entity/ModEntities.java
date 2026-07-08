package io.minecraft.flyconfig.entity;

import io.minecraft.flyconfig.FlightManagement;
import io.minecraft.flyconfig.entity.custom.NuclearEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class ModEntities {

    public static final EntityType<NuclearEntity> NUCLEAR = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of("flyconfig", "nuclear"),
            EntityType.Builder.<NuclearEntity>create(NuclearEntity::new, SpawnGroup.MISC)
                    .dimensions(0.8F, 1.5F)
                    .build(RegistryKey.of(Registries.ENTITY_TYPE.getKey(), Identifier.of("flyconfig", "nuclear")))
    );

    public static void register() {
    }
}