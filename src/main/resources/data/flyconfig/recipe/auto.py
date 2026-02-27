import json
import os

MOD_NAMESPACE = "flyconfig"
GERMANY_DEVELOPED_ID = f"{MOD_NAMESPACE}:germany_developed"
OUTPUT_DIR = "generated_recipes"
EXCLUDED_ITEMS = [
    "minecraft:air",
    "minecraft:water_bucket",
    "minecraft:lava_bucket",
    "minecraft:milk_bucket",
    "minecraft:command_block",
    "minecraft:chain_command_block",
    "minecraft:repeating_command_block",
    "minecraft:structure_block",
    "minecraft:jigsaw",
    "minecraft:barrier",
    "minecraft:bedrock",
    "minecraft:end_portal_frame"
]

MINECRAFT_ITEMS = [
    "iron_ingot","gold_ingot","diamond","emerald","coal","charcoal","redstone","lapis_lazuli","quartz","netherite_ingot","copper_ingot","raw_iron","raw_gold","raw_copper","iron_nugget","gold_nugget","copper_nugget",
    "iron_ore","deepslate_iron_ore","gold_ore","deepslate_gold_ore","diamond_ore","deepslate_diamond_ore","emerald_ore","deepslate_emerald_ore","coal_ore","deepslate_coal_ore","redstone_ore","deepslate_redstone_ore","lapis_ore","deepslate_lapis_ore","nether_quartz_ore","ancient_debris","copper_ore","deepslate_copper_ore","raw_iron_block","raw_gold_block","raw_copper_block",
    "oak_planks","spruce_planks","birch_planks","jungle_planks","acacia_planks","dark_oak_planks","mangrove_planks","bamboo_planks","cherry_planks","crimson_planks","warped_planks","oak_log","spruce_log","birch_log","jungle_log","acacia_log","dark_oak_log","mangrove_log","cherry_log","crimson_stem","warped_stem","stripped_oak_log","stripped_spruce_log","stripped_birch_log","stripped_jungle_log","stripped_acacia_log","stripped_dark_oak_log","stripped_mangrove_log","stripped_cherry_log","stripped_crimson_stem","stripped_warped_stem","oak_wood","spruce_wood","birch_wood","jungle_wood","acacia_wood","dark_oak_wood","mangrove_wood","cherry_wood","crimson_hyphae","warped_hyphae","stripped_oak_wood","stripped_spruce_wood","stripped_birch_wood","stripped_jungle_wood","stripped_acacia_wood","stripped_dark_oak_wood","stripped_mangrove_wood","stripped_cherry_wood","stripped_crimson_hyphae","stripped_warped_hyphae",
    "stick","oak_slab","spruce_slab","birch_slab","jungle_slab","acacia_slab","dark_oak_slab","mangrove_slab","cherry_slab","crimson_slab","warped_slab","oak_stairs","spruce_stairs","birch_stairs","jungle_stairs","acacia_stairs","dark_oak_stairs","mangrove_stairs","cherry_stairs","crimson_stairs","warped_stairs","oak_fence","spruce_fence","birch_fence","jungle_fence","acacia_fence","dark_oak_fence","mangrove_fence","cherry_fence","crimson_fence","warped_fence","oak_fence_gate","spruce_fence_gate","birch_fence_gate","jungle_fence_gate","acacia_fence_gate","dark_oak_fence_gate","mangrove_fence_gate","cherry_fence_gate","crimson_fence_gate","warped_fence_gate",
    "oak_door","spruce_door","birch_door","jungle_door","acacia_door","dark_oak_door","mangrove_door","cherry_door","crimson_door","warped_door","oak_trapdoor","spruce_trapdoor","birch_trapdoor","jungle_trapdoor","acacia_trapdoor","dark_oak_trapdoor","mangrove_trapdoor","cherry_trapdoor","crimson_trapdoor","warped_trapdoor",
    "wooden_sword","wooden_pickaxe","wooden_axe","wooden_shovel","wooden_hoe","stone_sword","stone_pickaxe","stone_axe","stone_shovel","stone_hoe","iron_sword","iron_pickaxe","iron_axe","iron_shovel","iron_hoe","golden_sword","golden_pickaxe","golden_axe","golden_shovel","golden_hoe","diamond_sword","diamond_pickaxe","diamond_axe","diamond_shovel","diamond_hoe","netherite_sword","netherite_pickaxe","netherite_axe","netherite_shovel","netherite_hoe",
    "leather_helmet","leather_chestplate","leather_leggings","leather_boots","iron_helmet","iron_chestplate","iron_leggings","iron_boots","golden_helmet","golden_chestplate","golden_leggings","golden_boots","diamond_helmet","diamond_chestplate","diamond_leggings","diamond_boots","netherite_helmet","netherite_chestplate","netherite_leggings","netherite_boots",
    "bread","apple","golden_apple","enchanted_golden_apple","porkchop","cooked_porkchop","beef","cooked_beef","chicken","cooked_chicken","rabbit","cooked_rabbit","mutton","cooked_mutton","cod","cooked_cod","salmon","cooked_salmon","tropical_fish","pufferfish","cookie","melon_slice","glistering_melon_slice","carrot","golden_carrot","potato","baked_potato","poisonous_potato","beetroot","beetroot_soup","sweet_berries","glow_berries","honey_bottle","milk_bucket","cake","pumpkin_pie","mushroom_stew","rabbit_stew","suspicious_stew",
    "redstone_torch","redstone_lamp","redstone_block","comparator","repeater","lever","stone_button","oak_button","spruce_button","birch_button","jungle_button","acacia_button","dark_oak_button","mangrove_button","cherry_button","crimson_button","warped_button","stone_pressure_plate","oak_pressure_plate","spruce_pressure_plate","birch_pressure_plate","jungle_pressure_plate","acacia_pressure_plate","dark_oak_pressure_plate","mangrove_pressure_plate","cherry_pressure_plate","crimson_pressure_plate","warped_pressure_plate","weighted_pressure_plate_light","weighted_pressure_plate_heavy","tripwire_hook","tripwire","piston","sticky_piston","observer","dispenser","dropper","hopper","redstone_torch","redstone_wire",
    "glass","glass_pane","stained_glass","stained_glass_pane","white_stained_glass","orange_stained_glass","magenta_stained_glass","light_blue_stained_glass","yellow_stained_glass","lime_stained_glass","pink_stained_glass","gray_stained_glass","light_gray_stained_glass","cyan_stained_glass","purple_stained_glass","blue_stained_glass","brown_stained_glass","green_stained_glass","red_stained_glass","black_stained_glass",
    "brick","brick_block","cobblestone","stone","smooth_stone","granite","polished_granite","diorite","polished_diorite","andesite","polished_andesite","prismarine","prismarine_bricks","dark_prismarine","end_stone","end_stone_bricks","netherrack","nether_bricks","red_nether_bricks","polished_blackstone","polished_blackstone_bricks","blackstone","cobbled_deepslate","deepslate","polished_deepslate","deepslate_bricks","cracked_deepslate_bricks","deepslate_tiles","cracked_deepslate_tiles","calcite","tuff","amethyst_block","calibrated_sculk_sensor",
    "sand","red_sand","gravel","clay","terracotta","white_terracotta","orange_terracotta","magenta_terracotta","light_blue_terracotta","yellow_terracotta","lime_terracotta","pink_terracotta","gray_terracotta","light_gray_terracotta","cyan_terracotta","purple_terracotta","blue_terracotta","brown_terracotta","green_terracotta","red_terracotta","black_terracotta","glazed_terracotta","white_glazed_terracotta","orange_glazed_terracotta","magenta_glazed_terracotta","light_blue_glazed_terracotta","yellow_glazed_terracotta","lime_glazed_terracotta","pink_glazed_terracotta","gray_glazed_terracotta","light_gray_glazed_terracotta","cyan_glazed_terracotta","purple_glazed_terracotta","blue_glazed_terracotta","brown_glazed_terracotta","green_glazed_terracotta","red_glazed_terracotta","black_glazed_terracotta",
    "white_wool","orange_wool","magenta_wool","light_blue_wool","yellow_wool","lime_wool","pink_wool","gray_wool","light_gray_wool","cyan_wool","purple_wool","blue_wool","brown_wool","green_wool","red_wool","black_wool","cotton","string","feather","leather","bone","arrow","tipped_arrow","spectral_arrow","bow","crossbow","trident","elytra","shield","totem_of_undying",
    "torch","soul_torch","lantern","soul_lantern","campfire","soul_campfire","candle","white_candle","orange_candle","magenta_candle","light_blue_candle","yellow_candle","lime_candle","pink_candle","gray_candle","light_gray_candle","cyan_candle","purple_candle","blue_candle","brown_candle","green_candle","red_candle","black_candle",
    "compass","clock","map","filled_map","glass_bottle","bottle_o_enchanting","experience_bottle","bucket","empty_bucket","water_bucket","lava_bucket","milk_bucket","powder_snow_bucket","shulker_shell","shulker_box","white_shulker_box","orange_shulker_box","magenta_shulker_box","light_blue_shulker_box","yellow_shulker_box","lime_shulker_box","pink_shulker_box","gray_shulker_box","light_gray_shulker_box","cyan_shulker_box","purple_shulker_box","blue_shulker_box","brown_shulker_box","green_shulker_box","red_shulker_box","black_shulker_box",
    "book","written_book","enchanting_table","bookshelf","chiseled_bookshelf","lectern","paper","parchment","ink_sac","black_dye","red_dye","green_dye","brown_dye","blue_dye","purple_dye","cyan_dye","light_gray_dye","gray_dye","pink_dye","lime_dye","yellow_dye","light_blue_dye","magenta_dye","orange_dye","white_dye",
    "slime_ball","magma_cream","ghast_tear","blaze_rod","blaze_powder","ender_pearl","eye_of_ender","ender_eye","nether_star","dragon_breath","phantom_membrane","creeper_head","dragon_head","player_head","zombie_head","skeleton_skull","wither_skeleton_skull","piglin_head","zombified_piglin_head",
    "boat","oak_boat","spruce_boat","birch_boat","jungle_boat","acacia_boat","dark_oak_boat","mangrove_boat","cherry_boat","crimson_boat","warped_boat","chest_boat","oak_chest_boat","spruce_chest_boat","birch_chest_boat","jungle_chest_boat","acacia_chest_boat","dark_oak_chest_boat","mangrove_chest_boat","cherry_chest_boat","crimson_chest_boat","warped_chest_boat",
    "minecart","chest_minecart","furnace_minecart","hopper_minecart","tnt_minecart","command_block_minecart",
    "egg","snowball","ender_pearl","experience_bottle","fire_charge","trident","splash_potion","lingering_potion","potion","arrow","tipped_arrow","spectral_arrow",
    "nether_wart","glow_ink_sac","glow_item_frame","item_frame","painting","lead","name_tag","tripwire_hook","fishing_rod","carrot_on_a_stick","warped_fungus_on_a_stick","brewing_stand","cauldron","water_cauldron","lava_cauldron","powder_snow_cauldron",
    "cactus","sugar_cane","bamboo","sweet_berry_bush","glow_lichen","vine","kelp","dried_kelp","dried_kelp_block","seagrass","tall_seagrass","coral","tube_coral","brain_coral","bubble_coral","fire_coral","horn_coral","tube_coral_block","brain_coral_block","bubble_coral_block","fire_coral_block","horn_coral_block","dead_tube_coral_block","dead_brain_coral_block","dead_bubble_coral_block","dead_fire_coral_block","dead_horn_coral_block",
    "pumpkin","carved_pumpkin","jack_o_lantern","melon","melon_slice","pumpkin_seeds","melon_seeds","wheat_seeds","wheat","beetroot_seeds","beetroot","potato","carrot","cocoa_beans","apple","oak_sapling","spruce_sapling","birch_sapling","jungle_sapling","acacia_sapling","dark_oak_sapling","mangrove_propagule","cherry_sapling","azalea","flowering_azalea","oak_leaves","spruce_leaves","birch_leaves","jungle_leaves","acacia_leaves","dark_oak_leaves","mangrove_leaves","cherry_leaves","azalea_leaves","flowering_azalea_leaves",
    "dandelion","poppy","blue_orchid","allium","azure_bluet","red_tulip","orange_tulip","white_tulip","pink_tulip","oxeye_daisy","cornflower","lily_of_the_valley","wither_rose","sunflower","lilac","rose_bush","peony","poisonous_potato","sweet_berries","glow_berries","honeycomb","honey_bottle","bee_nest","beehive",
    "iron_bars","chain","anvil","chipped_anvil","damaged_anvil","grindstone","smithing_table","fletching_table","cartography_table","loom","composter","barrel","blast_furnace","smoker","cauldron","stonecutter","jukebox","note_block","daylight_detector","respawn_anchor","end_crystal","jigsaw","target","sculk_sensor","sculk_shrieker","sculk_catalyst","sculk_vein","calibrated_sculk_sensor",
    "bed","white_bed","orange_bed","magenta_bed","light_blue_bed","yellow_bed","lime_bed","pink_bed","gray_bed","light_gray_bed","cyan_bed","purple_bed","blue_bed","brown_bed","green_bed","red_bed","black_bed",
    "banner","white_banner","orange_banner","magenta_banner","light_blue_banner","yellow_banner","lime_banner","pink_banner","gray_banner","light_gray_banner","cyan_banner","purple_banner","blue_banner","brown_banner","green_banner","red_banner","black_banner","shield","banner_pattern","creeper_banner_pattern","dragon_banner_pattern","flower_banner_pattern","gradient_banner_pattern","mojang_banner_pattern","piglin_banner_pattern","skull_banner_pattern","stripes_banner_pattern",
    "sign","oak_sign","spruce_sign","birch_sign","jungle_sign","acacia_sign","dark_oak_sign","mangrove_sign","cherry_sign","crimson_sign","warped_sign","hanging_sign","oak_hanging_sign","spruce_hanging_sign","birch_hanging_sign","jungle_hanging_sign","acacia_hanging_sign","dark_oak_hanging_sign","mangrove_hanging_sign","cherry_hanging_sign","crimson_hanging_sign","warped_hanging_sign"
]

def generate_recipe(item_id):
    full_item_id = f"minecraft:{item_id}"
    if full_item_id in EXCLUDED_ITEMS:
        print(f"跳过无效物品: {full_item_id}")
        return
    recipe = {
        "type": "crafting_shapeless",
        "category": "redstone",
        "group": item_id,
        "ingredients": [
            GERMANY_DEVELOPED_ID,
            full_item_id
        ],
        "result": {
            "id": full_item_id,
            "count": 16
        },
        "show_notification": True
    }
    recipe_filename = f"{item_id}.json"
    recipe_path = os.path.join(OUTPUT_DIR, recipe_filename)
    with open(recipe_path, "w", encoding="utf-8") as f:
        json.dump(recipe, f, indent=4, ensure_ascii=False)
    print(f"生成配方文件: {recipe_filename}")

def main():
    if not os.path.exists(OUTPUT_DIR):
        os.makedirs(OUTPUT_DIR)
        print(f"创建输出目录: {OUTPUT_DIR}")
    for item in MINECRAFT_ITEMS:
        generate_recipe(item)
    print(f"\n✅ 配方生成完成！共生成 {len(MINECRAFT_ITEMS) - len(EXCLUDED_ITEMS)} 个配方文件")
    print(f"📁 输出目录: {os.path.abspath(OUTPUT_DIR)}")
    print(f"💡 请将生成的JSON文件复制到模组的 data/{MOD_NAMESPACE}/recipes 目录下")

if __name__ == "__main__":
    main()
