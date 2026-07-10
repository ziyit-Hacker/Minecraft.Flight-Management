package io.minecraft.flyconfig.block;

import io.minecraft.flyconfig.FlightManagement;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ModBlocks {

    public static final Block URANIUM_ORE;
    public static final Block DEEPSLATE_URANIUM_ORE;
    public static final Block RADIATED_URANIUM_ORE;
    public static final Block RADIATED_DEEPSLATE_URANIUM_ORE;

    static {
        URANIUM_ORE = register("uranium_ore",
                new UraniumOreBlock(Block.Settings.create()
                        .strength(40.0f, 6.0f)
                        .requiresTool()
                        .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(FlightManagement.MOD_ID, "uranium_ore")))));

        DEEPSLATE_URANIUM_ORE = register("deepslate_uranium_ore",
                new UraniumOreBlock(Block.Settings.create()
                        .strength(45.0f, 6.0f)
                        .requiresTool()
                        .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(FlightManagement.MOD_ID, "deepslate_uranium_ore")))));

        RADIATED_URANIUM_ORE = registerNoItem("radiated_uranium_ore",
                new UraniumOreBlock(Block.Settings.create()
                        .strength(40.0f, 6.0f)
                        .requiresTool()
                        .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(FlightManagement.MOD_ID, "radiated_uranium_ore")))));

        RADIATED_DEEPSLATE_URANIUM_ORE = registerNoItem("radiated_deepslate_uranium_ore",
                new UraniumOreBlock(Block.Settings.create()
                        .strength(45.0f, 6.0f)
                        .requiresTool()
                        .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(FlightManagement.MOD_ID, "radiated_deepslate_uranium_ore")))));
    }

    private static Block register(String path, Block block) {
        Identifier id = Identifier.of(FlightManagement.MOD_ID, path);
        RegistryKey<Block> key = RegistryKey.of(RegistryKeys.BLOCK, id);
        Block registered = Registry.register(Registries.BLOCK, key, block);
        Registry.register(Registries.ITEM, id, new BlockItem(registered, new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, id))));
        return registered;
    }

    private static Block registerNoItem(String path, Block block) {
        Identifier id = Identifier.of(FlightManagement.MOD_ID, path);
        RegistryKey<Block> key = RegistryKey.of(RegistryKeys.BLOCK, id);
        return Registry.register(Registries.BLOCK, key, block);
    }

    public static void initialize() {
        FlightManagement.LOGGER.info("[" + FlightManagement.MOD_ID + "] Blocks registered");
    }
}