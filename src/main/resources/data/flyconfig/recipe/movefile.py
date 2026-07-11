import os
import json
import random
from pathlib import Path

# 合法方块列表
LEGAL_ITEMS = [
    "metal_scrap",
    "synthetic_motoroil",
    "flight_manager",
    "germany_developed",
    "ultimate_healthy_nightmare",
    "what_the_dog_doing",
    "music_disc_sieg_heil",
    "music_disc_hitler",
    "nuclear_spawn_egg",
    "nuclear_disarm_tool",
    "uranium_ingot"
]

# 非法方块列表
ILLEGAL_ITEMS = [
    "snowy_grass_block",
    "nether_portal",
    "end_portal",
    "end_gateway",
    "water",
    "lava",
    "flowing_water",
    "flowing_lava",
    "bubble_column_up",
    "bubble_column_down",
    "lit_furnace",
    "lit_blast_furnace",
    "lit_smoker",
    "activated_observer",
    "six_sided_piston",
    "headless_piston",
    "piston_arm",
    "wet_farmland",
    "fire",
    "soul_fire",
    "wheat_crop_0", "wheat_crop_1", "wheat_crop_2", "wheat_crop_3",
    "wheat_crop_4", "wheat_crop_5", "wheat_crop_6", "wheat_crop_7",
    "carrot_crop_0", "carrot_crop_1", "carrot_crop_2", "carrot_crop_3",
    "carrot_crop_4", "carrot_crop_5", "carrot_crop_6", "carrot_crop_7",
    "potato_crop_0", "potato_crop_1", "potato_crop_2", "potato_crop_3",
    "potato_crop_4", "potato_crop_5", "potato_crop_6", "potato_crop_7",
    "beetroot_crop_0", "beetroot_crop_1", "beetroot_crop_2", "beetroot_crop_3",
    "melon_stem_0", "melon_stem_1", "melon_stem_2", "melon_stem_3",
    "melon_stem_4", "melon_stem_5", "melon_stem_6", "melon_stem_7",
    "pumpkin_stem_0", "pumpkin_stem_1", "pumpkin_stem_2", "pumpkin_stem_3",
    "pumpkin_stem_4", "pumpkin_stem_5", "pumpkin_stem_6", "pumpkin_stem_7",
    "sugar_cane_0", "sugar_cane_1", "sugar_cane_2", "sugar_cane_3",
    "sugar_cane_4", "sugar_cane_5", "sugar_cane_6", "sugar_cane_7",
    "sugar_cane_8", "sugar_cane_9", "sugar_cane_10", "sugar_cane_11",
    "sugar_cane_12", "sugar_cane_13", "sugar_cane_14", "sugar_cane_15"
]

def generate_recipe(illegal_item, legal1, legal2, extra_ingredient=None):
    """生成配方JSON"""
    ingredients = [
        f"flyconfig:{legal1}",
        f"flyconfig:{legal2}"
    ]
    
    # 如果需要额外的材料来区分
    if extra_ingredient:
        ingredients.append(f"flyconfig:{extra_ingredient}")
    
    # 随机生成count (1-5)
    count = random.randint(1, 5)
    
    # 生成group名称（使用物品名）
    group_name = illegal_item.replace('_', ' ')
    
    recipe = {
        "type": "crafting_shapeless",
        "category": "miscellaneous",
        "group": group_name,
        "ingredients": ingredients,
        "result": {
            "id": f"flyconfig:{illegal_item}",
            "count": count
        },
        "show_notification": True
    }
    
    return recipe

def create_recipes():
    # 创建a文件夹
    a_dir = Path("a")
    a_dir.mkdir(exist_ok=True)
    
    # 为每个非法方块生成配方
    for i, illegal_item in enumerate(ILLEGAL_ITEMS):
        # 选择两个不同的合法方块
        legal1 = LEGAL_ITEMS[i % len(LEGAL_ITEMS)]
        legal2 = LEGAL_ITEMS[(i + 1) % len(LEGAL_ITEMS)]
        
        # 检查是否与其他非法方块配方重复
        # 如果重复，添加第三个合法方块作为区分
        extra = None
        # 简单检查：如果两个合法方块相同，或者可能会造成歧义
        if legal1 == legal2:
            extra = LEGAL_ITEMS[(i + 2) % len(LEGAL_ITEMS)]
        
        # 生成配方
        recipe = generate_recipe(illegal_item, legal1, legal2, extra)
        
        # 写入文件
        file_path = a_dir / f"{illegal_item}.json"
        with open(file_path, 'w', encoding='utf-8') as f:
            json.dump(recipe, f, indent=4, ensure_ascii=False)
        
        print(f"已生成: {illegal_item}.json")
    
    print(f"\n完成！共生成 {len(ILLEGAL_ITEMS)} 个配方文件到 a 文件夹")

if __name__ == "__main__":
    create_recipes()
