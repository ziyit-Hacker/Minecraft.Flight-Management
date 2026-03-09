package io.minecraft.flyconfig.villager;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import io.minecraft.flyconfig.block.ModBlock;
import io.minecraft.flyconfig.item.NODATA;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.poi.PointOfInterestType;

import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class ModPointOfInterestTypes {
    public static final RegistryKey<PointOfInterestType> FUHRER_LECTERN = of("fuhrer_lectern");

    private static final Map<BlockState, RegistryEntry<PointOfInterestType>> POI_STATES_TO_TYPE = Maps.<BlockState, RegistryEntry<PointOfInterestType>>newHashMap();

    private static Set<BlockState> getStatesOfBlock(Block block) {
        return ImmutableSet.copyOf(block.getStateManager().getStates());
    }

    private static RegistryKey<PointOfInterestType> of(String id) {
        return RegistryKey.of(RegistryKeys.POINT_OF_INTEREST_TYPE, Identifier.of("flyconfig", id));
    }

    private static void registerStates(RegistryEntry<PointOfInterestType> poiTypeEntry, Set<BlockState> states) {
        states.forEach(state -> {
            RegistryEntry<PointOfInterestType> registryEntry2 = (RegistryEntry<PointOfInterestType>)POI_STATES_TO_TYPE.put(state, poiTypeEntry);
            if (registryEntry2 != null) {
                throw (IllegalStateException) Util.getFatalOrPause(new IllegalStateException(String.format(Locale.ROOT, "%s is defined in more than one PoI type", state)));
            }
        });
    }

    private static PointOfInterestType register(
            Registry<PointOfInterestType> registry, RegistryKey<PointOfInterestType> key, Set<BlockState> states, int ticketCount, int searchDistance
    ) {
        PointOfInterestType pointOfInterestType = new PointOfInterestType(states, ticketCount, searchDistance);
        Registry.register(registry, key, pointOfInterestType);
        registerStates(registry.getOrThrow(key), states);
        return pointOfInterestType;
    }

    public static PointOfInterestType registerAndGetDefault(Registry<PointOfInterestType> registry) {
        return register(registry, FUHRER_LECTERN, getStatesOfBlock(ModBlock.FUHRER_LECTERN), 1, 1);
    }

    public static void init() {
    }
}
