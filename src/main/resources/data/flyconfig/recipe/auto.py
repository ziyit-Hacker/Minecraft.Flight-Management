import json
import os

# -------------------------- 配置项（根据你的模组调整） --------------------------
# 模组命名空间（对应你的 flyconfig）
MOD_NAMESPACE = "flyconfig"
# 德意志帝国研发的物品ID（对应你的 germany_developed）
GERMANY_DEVELOPED_ID = f"{MOD_NAMESPACE}:germany_developed"
# 输出配方文件的目录（生成后直接复制到模组的 data/flyconfig/recipes 目录）
OUTPUT_DIR = "generated_recipes"
# 要排除的无效物品（避免生成异常配方）
EXCLUDED_ITEMS = [
    # 空气
    "minecraft:air",
    # 流体桶（避免无限刷岩浆/水）
    "minecraft:water_bucket",
    "minecraft:lava_bucket",
    "minecraft:milk_bucket",
    # 特殊方块/物品
    "minecraft:command_block",
    "minecraft:chain_command_block",
    "minecraft:repeating_command_block",
    "minecraft:structure_block",
    "minecraft:jigsaw",
    # 其他不需要复制的物品（可自行添加）
    "minecraft:barrier",
    "minecraft:bedrock",
    "minecraft:end_portal_frame",
    "elytra"
]

# -------------------------- 原版核心物品列表（可自行补充） --------------------------
# 覆盖原版绝大多数可合成/可堆叠物品，按类别分类，便于维护
MINECRAFT_ITEMS = [
    # 基础材料
    "iron_ingot", "gold_ingot", "diamond", "emerald", "coal", "charcoal",
    "redstone", "lapis_lazuli", "quartz", "netherite_ingot", "copper_ingot",
    # 矿石
    "iron_ore", "gold_ore", "diamond_ore", "emerald_ore", "coal_ore", "redstone_ore",
    "lapis_ore", "nether_quartz_ore", "ancient_debris", "copper_ore",
    # 木材/植物
    "oak_planks", "spruce_planks", "birch_planks", "jungle_planks", "acacia_planks",
    "dark_oak_planks", "mangrove_planks", "bamboo_planks", "oak_log", "spruce_log",
    "birch_log", "jungle_log", "acacia_log", "dark_oak_log", "mangrove_log",
    "stick", "wooden_slab", "wooden_stairs", "wooden_fence", "wooden_door",
    # 工具/武器/盔甲
    "wooden_sword", "wooden_pickaxe", "wooden_axe", "wooden_shovel", "wooden_hoe",
    "stone_sword", "stone_pickaxe", "stone_axe", "stone_shovel", "stone_hoe",
    "iron_sword", "iron_pickaxe", "iron_axe", "iron_shovel", "iron_hoe",
    "golden_sword", "golden_pickaxe", "golden_axe", "golden_shovel", "golden_hoe",
    "diamond_sword", "diamond_pickaxe", "diamond_axe", "diamond_shovel", "diamond_hoe",
    "netherite_sword", "netherite_pickaxe", "netherite_axe", "netherite_shovel", "netherite_hoe",
    "iron_helmet", "iron_chestplate", "iron_leggings", "iron_boots",
    "diamond_helmet", "diamond_chestplate", "diamond_leggings", "diamond_boots",
    "netherite_helmet", "netherite_chestplate", "netherite_leggings", "netherite_boots",
    # 食物
    "bread", "apple", "cooked_porkchop", "cooked_beef", "cooked_chicken",
    "cooked_fish", "cookie", "melon_slice", "carrot", "potato", "baked_potato",
    # 红石组件
    "redstone_torch", "redstone_lamp", "comparator", "repeater", "lever",
    "button", "pressure_plate", "tripwire_hook", "piston", "sticky_piston",
    # 其他常用物品
    "glass", "glass_pane", "brick", "brick_block", "cobblestone", "stone",
    "sand", "gravel", "clay", "terracotta", "wool", "cotton", "string",
    "feather", "leather", "bone", "arrow", "bow", "crossbow", "elytra"
]

# -------------------------- 生成配方文件 --------------------------
def generate_recipe(item_id):
    """生成单个物品的复制配方 JSON"""
    # 完整物品ID（如 minecraft:iron_ingot）
    full_item_id = f"minecraft:{item_id}"
    # 跳过排除的物品
    if full_item_id in EXCLUDED_ITEMS:
        print(f"跳过无效物品: {full_item_id}")
        return
    
    # 配方JSON模板
    recipe = {
        "type": "crafting_shapeless",
        "category": "redstone",
        "group": MOD_NAMESPACE,
        "ingredients": [
            GERMANY_DEVELOPED_ID,
            full_item_id
        ],
        "result": {
            "id": full_item_id,
            "count": 16
        },
        "show_notification": True  # 合成时显示提示（可选）
    }
    
    # 生成配方文件名（如 iron_ingot.json）
    recipe_filename = f"{item_id}.json"
    recipe_path = os.path.join(OUTPUT_DIR, recipe_filename)
    
    # 写入JSON文件（格式化输出，便于阅读）
    with open(recipe_path, "w", encoding="utf-8") as f:
        json.dump(recipe, f, indent=4, ensure_ascii=False)
    
    print(f"生成配方文件: {recipe_filename}")

def main():
    if not os.path.exists(OUTPUT_DIR):
        os.makedirs(OUTPUT_DIR)
        print(f"创建输出目录: {OUTPUT_DIR}")
    
    # 批量生成所有物品的配方
    for item in MINECRAFT_ITEMS:
        generate_recipe(item)
    
    print(f"\n✅ 配方生成完成！共生成 {len(MINECRAFT_ITEMS) - len(EXCLUDED_ITEMS)} 个配方文件")
    print(f"📁 输出目录: {os.path.abspath(OUTPUT_DIR)}")
    print(f"💡 请将生成的JSON文件复制到模组的 data/{MOD_NAMESPACE}/recipes 目录下")

if __name__ == "__main__":
    main()
    
