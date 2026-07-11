import os
import json

# 配置 - 需要手动绘制贴图的物品（仅保留需要手绘的）
CUSTOM_TEXTURE_ITEMS = [
    "nether_portal",
    "end_portal",
    "end_gateway",
    "water",
    "lava",
    "flowing_water",
    "flowing_lava"
]

# 配置 - 使用方块贴图的物品
BLOCK_TEXTURE_ITEMS = {
    "snowy_grass_block": "grass_block",
    "lit_furnace": "furnace",
    "lit_blast_furnace": "blast_furnace",
    "lit_smoker": "smoker",
    "activated_observer": "observer",
    "six_sided_piston": "piston",
    "headless_piston": "piston",
    "piston_arm": "piston_head",
    "bubble_column_up": "bubble_column",
    "bubble_column_down": "bubble_column",
    "wet_farmland": "farmland",
    "fire": "fire_0",
    "soul_fire": "soul_fire_0"
}

# 农作物 - 使用方块贴图（每个阶段对应不同的方块状态）
CROP_TEXTURE_ITEMS = {
    # 小麦
    "wheat_crop_0": "wheat_stage0",
    "wheat_crop_1": "wheat_stage1",
    "wheat_crop_2": "wheat_stage2",
    "wheat_crop_3": "wheat_stage3",
    "wheat_crop_4": "wheat_stage4",
    "wheat_crop_5": "wheat_stage5",
    "wheat_crop_6": "wheat_stage6",
    "wheat_crop_7": "wheat_stage7",
    # 胡萝卜
    "carrot_crop_0": "carrots_stage0",
    "carrot_crop_1": "carrots_stage1",
    "carrot_crop_2": "carrots_stage2",
    "carrot_crop_3": "carrots_stage3",
    "carrot_crop_4": "carrots_stage4",
    "carrot_crop_5": "carrots_stage5",
    "carrot_crop_6": "carrots_stage6",
    "carrot_crop_7": "carrots_stage7",
    # 马铃薯
    "potato_crop_0": "potatoes_stage0",
    "potato_crop_1": "potatoes_stage1",
    "potato_crop_2": "potatoes_stage2",
    "potato_crop_3": "potatoes_stage3",
    "potato_crop_4": "potatoes_stage4",
    "potato_crop_5": "potatoes_stage5",
    "potato_crop_6": "potatoes_stage6",
    "potato_crop_7": "potatoes_stage7",
    # 甜菜根
    "beetroot_crop_0": "beetroots_stage0",
    "beetroot_crop_1": "beetroots_stage1",
    "beetroot_crop_2": "beetroots_stage2",
    "beetroot_crop_3": "beetroots_stage3",
    # 西瓜茎
    "melon_stem_0": "melon_stem_stage0",
    "melon_stem_1": "melon_stem_stage1",
    "melon_stem_2": "melon_stem_stage2",
    "melon_stem_3": "melon_stem_stage3",
    "melon_stem_4": "melon_stem_stage4",
    "melon_stem_5": "melon_stem_stage5",
    "melon_stem_6": "melon_stem_stage6",
    "melon_stem_7": "melon_stem_stage7",
    # 南瓜茎
    "pumpkin_stem_0": "pumpkin_stem_stage0",
    "pumpkin_stem_1": "pumpkin_stem_stage1",
    "pumpkin_stem_2": "pumpkin_stem_stage2",
    "pumpkin_stem_3": "pumpkin_stem_stage3",
    "pumpkin_stem_4": "pumpkin_stem_stage4",
    "pumpkin_stem_5": "pumpkin_stem_stage5",
    "pumpkin_stem_6": "pumpkin_stem_stage6",
    "pumpkin_stem_7": "pumpkin_stem_stage7",
    # 甘蔗
    "sugar_cane_0": "sugar_cane_stage0",
    "sugar_cane_1": "sugar_cane_stage1",
    "sugar_cane_2": "sugar_cane_stage2",
    "sugar_cane_3": "sugar_cane_stage3",
    "sugar_cane_4": "sugar_cane_stage4",
    "sugar_cane_5": "sugar_cane_stage5",
    "sugar_cane_6": "sugar_cane_stage6",
    "sugar_cane_7": "sugar_cane_stage7",
    "sugar_cane_8": "sugar_cane_stage8",
    "sugar_cane_9": "sugar_cane_stage9",
    "sugar_cane_10": "sugar_cane_stage10",
    "sugar_cane_11": "sugar_cane_stage11",
    "sugar_cane_12": "sugar_cane_stage12",
    "sugar_cane_13": "sugar_cane_stage13",
    "sugar_cane_14": "sugar_cane_stage14",
    "sugar_cane_15": "sugar_cane_stage15",
}

# 脚本所在目录就是 assets/flyconfig
BASE_PATH = "."

def create_items_json():
    """创建 items 目录下的物品模型引用文件"""
    items_dir = os.path.join(BASE_PATH, "items")
    os.makedirs(items_dir, exist_ok=True)
    
    # 手动绘制贴图的物品
    for item in CUSTOM_TEXTURE_ITEMS:
        file_path = os.path.join(items_dir, f"{item}.json")
        data = {
            "model": {
                "type": "minecraft:model",
                "model": f"flyconfig:item/{item}"
            }
        }
        with open(file_path, "w", encoding="utf-8") as f:
            json.dump(data, f, indent=2)
        print(f"Created (custom texture): {file_path}")
    
    # 使用方块贴图的物品
    for item, block in BLOCK_TEXTURE_ITEMS.items():
        file_path = os.path.join(items_dir, f"{item}.json")
        data = {
            "model": {
                "type": "minecraft:model",
                "model": f"minecraft:block/{block}"
            }
        }
        with open(file_path, "w", encoding="utf-8") as f:
            json.dump(data, f, indent=2)
        print(f"Created (block texture): {file_path}")
    
    # 农作物 - 使用方块贴图
    for item, crop_stage in CROP_TEXTURE_ITEMS.items():
        file_path = os.path.join(items_dir, f"{item}.json")
        data = {
            "model": {
                "type": "minecraft:model",
                "model": f"minecraft:block/{crop_stage}"
            }
        }
        with open(file_path, "w", encoding="utf-8") as f:
            json.dump(data, f, indent=2)
        print(f"Created (crop texture): {file_path}")

def create_models_item_json():
    """创建 models/item 目录下的物品模型文件"""
    models_dir = os.path.join(BASE_PATH, "models", "item")
    os.makedirs(models_dir, exist_ok=True)
    
    # 手动绘制贴图的物品 - 使用 generated 模型
    for item in CUSTOM_TEXTURE_ITEMS:
        file_path = os.path.join(models_dir, f"{item}.json")
        data = {
            "parent": "minecraft:item/generated",
            "textures": {
                "layer0": f"flyconfig:item/{item}"
            }
        }
        with open(file_path, "w", encoding="utf-8") as f:
            json.dump(data, f, indent=2)
        print(f"Created model (custom): {file_path}")
    
    # 使用方块贴图的物品 - 直接引用方块模型
    for item, block in BLOCK_TEXTURE_ITEMS.items():
        file_path = os.path.join(models_dir, f"{item}.json")
        data = {
            "parent": f"minecraft:block/{block}"
        }
        with open(file_path, "w", encoding="utf-8") as f:
            json.dump(data, f, indent=2)
        print(f"Created model (block): {file_path}")
    
    # 农作物 - 直接引用方块模型
    for item, crop_stage in CROP_TEXTURE_ITEMS.items():
        file_path = os.path.join(models_dir, f"{item}.json")
        data = {
            "parent": f"minecraft:block/{crop_stage}"
        }
        with open(file_path, "w", encoding="utf-8") as f:
            json.dump(data, f, indent=2)
        print(f"Created model (crop): {file_path}")

def create_textures_item_dir():
    """创建 textures/item 目录，用于存放手动绘制的贴图文件"""
    textures_dir = os.path.join(BASE_PATH, "textures", "item")
    os.makedirs(textures_dir, exist_ok=True)
    print(f"\nCreated textures directory: {textures_dir}")
    print("\nPlease place your custom textures in this directory as PNG files:")
    for item in CUSTOM_TEXTURE_ITEMS:
        print(f"  - {item}.png")

if __name__ == "__main__":
    print("Creating item model JSON files...")
    create_items_json()
    create_models_item_json()
    create_textures_item_dir()
    print("\nDone!")
