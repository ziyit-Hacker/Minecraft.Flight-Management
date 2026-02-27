import os
import json

RECIPE_FOLDER_PATH = r"H:\\Code\\java\\Minecraft_mods\\Flight-Management\\src\\main\\resources\\data\\flyconfig\\recipe\\"

DELETE_FILES = [
    "banner", "banner_pattern", "bed", "boat", "bottle_o_enchanting", 
    "brick_block", "chest_boat", "copper_nugget", "coral", "cotton", 
    "crimson_boat", "crimson_chest_boat", "dragon_banner_pattern", 
    "empty_bucket", "eye_of_ender", "glazed_terracotta", "gradient_banner_pattern", 
    "lava_cauldron", "parchment", "powder_snow_cauldron", "redstone_wire", 
    "sign", "stripes_banner_pattern", "sweet_berry_bush", "tall_seagrass", 
    "tripwire", "warped_boat", "warped_chest_boat", "water_cauldron", 
    "weighted_pressure_plate_heavy", "weighted_pressure_plate_light", 
    "zombified_piglin_head", "stained_glass", "stained_glass_pane"
]

# 2. 需要修改count为1的文件（stack size 16>1报错）
MODIFY_FILES = [
    "acacia_boat", "acacia_chest_boat", "beetroot_soup", "birch_boat", 
    "birch_chest_boat", "black_bed", "black_shulker_box", "blue_bed", 
    "blue_shulker_box", "bow", "brown_bed", "brown_shulker_box", "cake", 
    "carrot_on_a_stick", "cherry_boat", "cherry_chest_boat", "chest_minecart", 
    "command_block_minecart", "creeper_banner_pattern", "crossbow", "cyan_bed", 
    "cyan_shulker_box", "dark_oak_boat", "dark_oak_chest_boat", "diamond_axe", 
    "diamond_boots", "diamond_chestplate", "diamond_helmet", "diamond_hoe", 
    "diamond_leggings", "diamond_pickaxe", "diamond_shovel", "diamond_sword", 
    "fishing_rod", "flower_banner_pattern", "furnace_minecart", "golden_axe", 
    "golden_boots", "golden_chestplate", "golden_helmet", "golden_hoe", 
    "golden_leggings", "golden_pickaxe", "golden_shovel", "golden_sword", 
    "gray_bed", "gray_shulker_box", "green_bed", "green_shulker_box", 
    "hopper_minecart", "iron_axe", "iron_boots", "iron_chestplate", 
    "iron_helmet", "iron_hoe", "iron_leggings", "iron_pickaxe", "iron_shovel", 
    "iron_sword", "jungle_boat", "jungle_chest_boat", "leather_boots", 
    "leather_chestplate", "leather_helmet", "leather_leggings", "light_blue_bed", 
    "light_blue_shulker_box", "light_gray_bed", "light_gray_shulker_box", 
    "lime_bed", "lime_shulker_box", "lingering_potion", "magenta_bed", 
    "magenta_shulker_box", "mangrove_boat", "mangrove_chest_boat", "minecart", 
    "mojang_banner_pattern", "mushroom_stew", "netherite_axe", "netherite_boots", 
    "netherite_chestplate", "netherite_helmet", "netherite_hoe", "netherite_leggings", 
    "netherite_pickaxe", "netherite_shovel", "netherite_sword", "oak_boat", 
    "oak_chest_boat", "orange_bed", "orange_shulker_box", "piglin_banner_pattern", 
    "pink_bed", "pink_shulker_box", "powder_snow_bucket", "purple_bed", 
    "purple_shulker_box", "rabbit_stew", "red_bed", "red_shulker_box", "shield", 
    "shulker_box", "skull_banner_pattern", "splash_potion", "spruce_boat", 
    "spruce_chest_boat", "stone_axe", "stone_hoe", "stone_pickaxe", 
    "stone_shovel", "stone_sword", "suspicious_stew", "tnt_minecart", 
    "totem_of_undying", "trident", "warped_fungus_on_a_stick", "white_bed", 
    "white_shulker_box", "wooden_axe", "wooden_hoe", "wooden_pickaxe", 
    "wooden_shovel", "wooden_sword", "yellow_bed", "yellow_shulker_box"
]

# ===================== 核心逻辑 =====================
def process_recipe_files():
    # 检查文件夹是否存在
    if not os.path.exists(RECIPE_FOLDER_PATH):
        print(f"❌ 错误：配方文件夹路径不存在 → {RECIPE_FOLDER_PATH}")
        return

    # 第一步：删除Unknown registry key报错的文件
    print("\n===== 开始删除无效ID的配方文件 =====")
    deleted_count = 0
    for filename in DELETE_FILES:
        file_path = os.path.join(RECIPE_FOLDER_PATH, f"{filename}.json")
        if os.path.exists(file_path):
            try:
                os.remove(file_path)
                print(f"✅ 删除成功：{filename}.json")
                deleted_count += 1
            except Exception as e:
                print(f"❌ 删除失败：{filename}.json → {str(e)}")
        else:
            print(f"⚠️ 文件不存在：{filename}.json")
    print(f"📊 共删除 {deleted_count} 个文件\n")

    # 第二步：修改count为1，并统一配方格式
    print("===== 开始修改配方文件count值 =====")
    modified_count = 0
    for filename in MODIFY_FILES:
        file_path = os.path.join(RECIPE_FOLDER_PATH, f"{filename}.json")
        if not os.path.exists(file_path):
            print(f"⚠️ 文件不存在：{filename}.json，跳过")
            continue

        try:
            # 读取原文件
            with open(file_path, "r", encoding="utf-8") as f:
                data = json.load(f)

            # 强制统一为你指定的格式（crafting_shapeless）
            # 保留核心字段，覆盖count为1
            new_recipe = {
                "type": "crafting_shapeless",
                "category": data.get("category", "redstone"),
                "group": data.get("group", f"{filename}"),
                "ingredients": data.get("ingredients", []),
                "result": {
                    "id": data.get("result", {}).get("id", f"minecraft:{filename}"),
                    "count": 1  # 强制改为1
                },
                "show_notification": data.get("show_notification", True)
            }

            # 写入修改后的内容
            with open(file_path, "w", encoding="utf-8") as f:
                json.dump(new_recipe, f, indent=4, ensure_ascii=False)

            print(f"✅ 修改成功：{filename}.json（count已改为1）")
            modified_count += 1

        except json.JSONDecodeError:
            print(f"❌ 解析失败：{filename}.json → 文件不是有效JSON")
        except Exception as e:
            print(f"❌ 修改失败：{filename}.json → {str(e)}")

    print(f"\n📊 共修改 {modified_count} 个文件")
    print("\n✅ 所有操作完成！")

# 执行脚本
if __name__ == "__main__":
    process_recipe_files()
