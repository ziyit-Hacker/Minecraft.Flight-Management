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

import java.util.function.Function;

public class ModBlocks {

    public static final Block URANIUM_ORE;
    public static final Block DEEPSLATE_URANIUM_ORE;

    static {
        URANIUM_ORE = register("uranium_ore",
                UraniumOreBlock::new,
                Block.Settings.create().strength(40.0f, 6.0f).requiresTool());
        DEEPSLATE_URANIUM_ORE = register("deepslate_uranium_ore",
                UraniumOreBlock::new,
                Block.Settings.create().strength(45.0f, 6.0f).requiresTool());
    }

    private static Block register(String path, Function<Block.Settings, Block> factory, Block.Settings settings) {
        Identifier id = Identifier.of("flyconfig", path);
        RegistryKey<Block> key = RegistryKey.of(RegistryKeys.BLOCK, id);
        Block block = Blocks.register(key, factory, settings);
        Registry.register(Registries.ITEM, id, new BlockItem(block, new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, id))));
        return block;
    }

    public static void initialize() {
    }
}