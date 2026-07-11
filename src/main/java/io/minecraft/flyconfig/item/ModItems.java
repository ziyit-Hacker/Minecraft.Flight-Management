package io.minecraft.flyconfig.item;

import io.minecraft.flyconfig.FlightManagement;
import io.minecraft.flyconfig.block.ModBlocks;
import io.minecraft.flyconfig.entity.ModEntities;
import io.minecraft.flyconfig.sound.ModJukeboxSongs;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.BlockState;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ConsumableComponents;
import net.minecraft.component.type.DeathProtectionComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class ModItems {
    private ModItems() {
    }

    // MOD 物品
    public static final Item METAL_SCRAP = registerItems("metal_scrap", MetalScrapItem::new, new Item.Settings()
            .maxCount(99).food(ModFoodComponents.METAL_SCRAP, ConsumableComponents.FOOD));
    public static final Item AIRCRAFT_OIL = registerItems("synthetic_motoroil", Item::new, new Item.Settings()
            .maxCount(16).rarity(Rarity.UNCOMMON));
    public static final Item FLIGHT_MANAGER = registerItems("flight_manager", Item::new, new Item.Settings()
            .maxCount(1).rarity(Rarity.RARE).component(DataComponentTypes.DEATH_PROTECTION, DeathProtectionComponent.TOTEM_OF_UNDYING));
    public static final Item GERMANY_DEVELOPED = registerItems("germany_developed", Item::new, new Item.Settings()
            .maxCount(2).rarity(Rarity.EPIC));
    public static final Item ULHEALN = registerItems("ultimate_healthy_nightmare", ULHEALN::new, new Item.Settings()
            .maxCount(1).food(ModFoodComponents.ULHEALN, ConsumableComponents.FOOD).rarity(Rarity.EPIC));
    public static final Item NODATA = registerItems("what_the_dog_doing", NODATA::new, new Item.Settings()
            .maxCount(1).food(ModFoodComponents.NODATA, ConsumableComponents.FOOD).rarity(Rarity.EPIC));
    public static final Item MUSIC_DISC_SIEG_HEIL = registerItems("music_disc_sieg_heil", Item::new, new Item.Settings()
            .maxCount(1).rarity(Rarity.RARE).jukeboxPlayable(ModJukeboxSongs.SIEG_HEIL));
    public static final Item MUSIC_DISC_HITLER = registerItems("music_disc_hitler", Item::new, new Item.Settings()
            .maxCount(1).rarity(Rarity.RARE).jukeboxPlayable(ModJukeboxSongs.HITLER));
    public static final Item NUCLEAR_SPAWN_EGG = registerItems("nuclear_spawn_egg",
            settings -> new SpawnEggItem(ModEntities.NUCLEAR, settings) {
                @Override
                public void inventoryTick(ItemStack stack, ServerWorld world, Entity entity, @Nullable EquipmentSlot slot) {
                    if (entity instanceof LivingEntity living) {
                        if (slot == EquipmentSlot.MAINHAND || slot == EquipmentSlot.OFFHAND) {
                            living.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 40, 0));
                        }
                    }
                    super.inventoryTick(stack, world, entity, slot);
                }
            },
            new Item.Settings().maxCount(1));
    public static final Item NUCLEAR_DISARM_TOOL = registerItems("nuclear_disarm_tool",
            NuclearDisarmToolItem::new,
            new Item.Settings().maxCount(1).rarity(Rarity.EPIC));

    public static final Item URANIUM_INGOT = registerItems("uranium_ingot",
            settings -> new Item(settings.maxCount(64).rarity(Rarity.UNCOMMON)) {
                @Override
                public void inventoryTick(ItemStack stack, ServerWorld world, Entity entity, @Nullable EquipmentSlot slot) {
                    super.inventoryTick(stack, world, entity, slot);
                    if (entity instanceof PlayerEntity player) {
                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 100, 0));
                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 100, 0));
                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 100, 0));
                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 100, 0));
                    }
                }
            });

    // 违禁物品（创造模式也无法通过正常途径获取的方块）
    // 这些方块只能通过指令或本模组的特殊方式获得
    public static final Item SNOWY_GRASS_BLOCK = registerItems("snowy_grass_block",
            settings -> new BlockItem(Blocks.GRASS_BLOCK, settings) {
                @Override
                protected boolean place(ItemPlacementContext context, BlockState state) {
                    return super.place(context, state.with(Properties.SNOWY, true));
                }
            },
            new Item.Settings().maxCount(64));

    public static final Item NETHER_PORTAL = registerBlockItem("nether_portal",
            Blocks.NETHER_PORTAL);  // 下界传送门方块，放置后不会自然生成传送门框架

    public static final Item END_PORTAL = registerBlockItem("end_portal",
            Blocks.END_PORTAL);  // 末地传送门方块，放置后不会自然生成传送门框架

    public static final Item END_GATEWAY = registerBlockItem("end_gateway",
            Blocks.END_GATEWAY);  // 末地折跃门方块，用于主岛与副岛之间传送

    public static final Item WATER = registerItems("water",
            settings -> new BlockItem(Blocks.WATER, settings) {
                @Override
                protected boolean place(ItemPlacementContext context, BlockState state) {
                    boolean placed = super.place(context, state.with(Properties.LEVEL_15, 15));
                    if (placed) {
                        World world = context.getWorld();
                        BlockPos pos = context.getBlockPos();
                        world.setBlockState(pos, state.with(Properties.LEVEL_15, 15), Block.FORCE_STATE | Block.SKIP_REDSTONE_WIRE_STATE_REPLACEMENT);
                    }
                    return placed;
                }
            },
            new Item.Settings().maxCount(64));

    public static final Item LAVA = registerItems("lava",
            settings -> new BlockItem(Blocks.LAVA, settings) {
                @Override
                protected boolean place(ItemPlacementContext context, BlockState state) {
                    boolean placed = super.place(context, state.with(Properties.LEVEL_15, 15));
                    if (placed) {
                        World world = context.getWorld();
                        BlockPos pos = context.getBlockPos();
                        world.setBlockState(pos, state.with(Properties.LEVEL_15, 15), Block.FORCE_STATE | Block.SKIP_REDSTONE_WIRE_STATE_REPLACEMENT);
                    }
                    return placed;
                }
            },
            new Item.Settings().maxCount(64));

    public static final Item FLOWING_WATER = registerBlockItem("flowing_water",
            Blocks.WATER);  // 流动水方块，放置后会流动

    public static final Item FLOWING_LAVA = registerBlockItem("flowing_lava",
            Blocks.LAVA);  // 流动岩浆方块，放置后会流动

    public static final Item BUBBLE_COLUMN_UP = registerItems("bubble_column_up",
            settings -> new BlockItem(Blocks.BUBBLE_COLUMN, settings) {
                @Override
                protected boolean place(ItemPlacementContext context, BlockState state) {
                    return super.place(context, state.with(Properties.DRAG, false));
                }
            },
            new Item.Settings().maxCount(64));  // 上行气泡柱，将实体向上推动

    public static final Item BUBBLE_COLUMN_DOWN = registerItems("bubble_column_down",
            settings -> new BlockItem(Blocks.BUBBLE_COLUMN, settings) {
                @Override
                protected boolean place(ItemPlacementContext context, BlockState state) {
                    return super.place(context, state.with(Properties.DRAG, true));
                }
            },
            new Item.Settings().maxCount(64));  // 下行气泡柱，将实体向下拉动

    public static final Item LIT_FURNACE = registerItems("lit_furnace",
            settings -> new BlockItem(Blocks.FURNACE, settings) {
                @Override
                protected boolean place(ItemPlacementContext context, BlockState state) {
                    return super.place(context, state.with(Properties.LIT, true));
                }
            },
            new Item.Settings().maxCount(64));  // 燃烧状态的熔炉，外观亮起但无需燃料

    public static final Item LIT_BLAST_FURNACE = registerItems("lit_blast_furnace",
            settings -> new BlockItem(Blocks.BLAST_FURNACE, settings) {
                @Override
                protected boolean place(ItemPlacementContext context, BlockState state) {
                    return super.place(context, state.with(Properties.LIT, true));
                }
            },
            new Item.Settings().maxCount(64));  // 燃烧状态的高炉，外观亮起但无需燃料

    public static final Item LIT_SMOKER = registerItems("lit_smoker",
            settings -> new BlockItem(Blocks.SMOKER, settings) {
                @Override
                protected boolean place(ItemPlacementContext context, BlockState state) {
                    return super.place(context, state.with(Properties.LIT, true));
                }
            },
            new Item.Settings().maxCount(64));  // 燃烧状态的烟熏炉，外观亮起但无需燃料

    public static final Item ACTIVATED_OBSERVER = registerItems("activated_observer",
            settings -> new BlockItem(Blocks.OBSERVER, settings) {
                @Override
                protected boolean place(ItemPlacementContext context, BlockState state) {
                    Direction facing = context.getPlayer() == null ? Direction.NORTH : context.getPlayer().getHorizontalFacing().getOpposite();
                    return super.place(context, state.with(Properties.FACING, facing).with(Properties.POWERED, true));
                }
            },
            new Item.Settings().maxCount(64));  // 激活状态的侦测器，持续输出红石信号

    // 无头活塞：根本没有活塞臂（使用普通活塞头，SHORT = false，显示完整活塞头）
    public static final Item HEADLESS_PISTON = registerItems("headless_piston",
            settings -> new BlockItem(Blocks.PISTON_HEAD, settings) {
                @Override
                protected boolean place(ItemPlacementContext context, BlockState state) {
                    Direction facing = context.getPlayer() == null ? Direction.NORTH : context.getPlayer().getHorizontalFacing().getOpposite();
                    return super.place(context, state.with(Properties.FACING, facing).with(Properties.SHORT, false));
                }
            },
            new Item.Settings().maxCount(64));

    // 活塞臂：本质是普通活塞，但放置后无法呈现“活塞臂”的视觉效果
    public static final Item PISTON_ARM = registerItems("piston_arm",

    settings -> new BlockItem(Blocks.PISTON_HEAD, settings) {
        @Override
        protected boolean place(ItemPlacementContext context, BlockState state) {
            return super.place(context, state.with(Properties.SHORT, true));
            }
        }, new Item.Settings().maxCount(64));

    // 六面活塞：同样只是普通活塞
    // 在1.13+无法真正获得，保留物品仅作为收藏或占位符
    public static final Item SIX_SIDED_PISTON = registerItems("six_sided_piston",
            settings -> new BlockItem(Blocks.PISTON, settings),
            new Item.Settings().maxCount(64));

    // 火焰 - 可放置的火焰方块
    public static final Item FIRE = registerItems("fire",
            settings -> new BlockItem(Blocks.FIRE, settings),
            new Item.Settings().maxCount(64));

    // 灵魂火 - 可放置的灵魂火方块
    public static final Item SOUL_FIRE = registerItems("soul_fire",
            settings -> new BlockItem(Blocks.SOUL_FIRE, settings),
            new Item.Settings().maxCount(64));

    // 湿润的耕地 - 始终保持湿润状态的耕地
    public static final Item WET_FARMLAND = registerItems("wet_farmland",
            settings -> new BlockItem(Blocks.FARMLAND, settings) {
                @Override
                protected boolean place(ItemPlacementContext context, BlockState state) {
                    return super.place(context, state.with(Properties.MOISTURE, 7));
                }
            },
            new Item.Settings().maxCount(64));

    // ===== 农作物（所有生长阶段） =====

    // 小麦 - 8个生长阶段 (0-7)
    public static final Item WHEAT_CROP_0 = registerItems("wheat_crop_0",
            settings -> new BlockItem(Blocks.WHEAT, settings) {
                @Override
                protected boolean place(ItemPlacementContext context, BlockState state) {
                    return super.place(context, state.with(Properties.AGE_7, 0));
                }
            }, new Item.Settings().maxCount(64));

    public static final Item WHEAT_CROP_1 = registerItems("wheat_crop_1",
            settings -> new BlockItem(Blocks.WHEAT, settings) {
                @Override
                protected boolean place(ItemPlacementContext context, BlockState state) {
                    return super.place(context, state.with(Properties.AGE_7, 1));
                }
            }, new Item.Settings().maxCount(64));

    public static final Item WHEAT_CROP_2 = registerItems("wheat_crop_2",
            settings -> new BlockItem(Blocks.WHEAT, settings) {
                @Override
                protected boolean place(ItemPlacementContext context, BlockState state) {
                    return super.place(context, state.with(Properties.AGE_7, 2));
                }
            }, new Item.Settings().maxCount(64));

    public static final Item WHEAT_CROP_3 = registerItems("wheat_crop_3",
            settings -> new BlockItem(Blocks.WHEAT, settings) {
                @Override
                protected boolean place(ItemPlacementContext context, BlockState state) {
                    return super.place(context, state.with(Properties.AGE_7, 3));
                }
            }, new Item.Settings().maxCount(64));

    public static final Item WHEAT_CROP_4 = registerItems("wheat_crop_4",
            settings -> new BlockItem(Blocks.WHEAT, settings) {
                @Override
                protected boolean place(ItemPlacementContext context, BlockState state) {
                    return super.place(context, state.with(Properties.AGE_7, 4));
                }
            }, new Item.Settings().maxCount(64));

    public static final Item WHEAT_CROP_5 = registerItems("wheat_crop_5",
            settings -> new BlockItem(Blocks.WHEAT, settings) {
                @Override
                protected boolean place(ItemPlacementContext context, BlockState state) {
                    return super.place(context, state.with(Properties.AGE_7, 5));
                }
            }, new Item.Settings().maxCount(64));

    public static final Item WHEAT_CROP_6 = registerItems("wheat_crop_6",
            settings -> new BlockItem(Blocks.WHEAT, settings) {
                @Override
                protected boolean place(ItemPlacementContext context, BlockState state) {
                    return super.place(context, state.with(Properties.AGE_7, 6));
                }
            }, new Item.Settings().maxCount(64));

    public static final Item WHEAT_CROP_7 = registerItems("wheat_crop_7",
            settings -> new BlockItem(Blocks.WHEAT, settings) {
                @Override
                protected boolean place(ItemPlacementContext context, BlockState state) {
                    return super.place(context, state.with(Properties.AGE_7, 7));
                }
            }, new Item.Settings().maxCount(64));

    // 胡萝卜 - 8个生长阶段 (0-7)
    public static final Item CARROT_CROP_0 = registerItems("carrot_crop_0",
            settings -> new BlockItem(Blocks.CARROTS, settings) {
                @Override
                protected boolean place(ItemPlacementContext context, BlockState state) {
                    return super.place(context, state.with(Properties.AGE_7, 0));
                }
            }, new Item.Settings().maxCount(64));

    public static final Item CARROT_CROP_1 = registerItems("carrot_crop_1",
            settings -> new BlockItem(Blocks.CARROTS, settings) {
                @Override
                protected boolean place(ItemPlacementContext context, BlockState state) {
                    return super.place(context, state.with(Properties.AGE_7, 1));
                }
            }, new Item.Settings().maxCount(64));

    public static final Item CARROT_CROP_2 = registerItems("carrot_crop_2",
            settings -> new BlockItem(Blocks.CARROTS, settings) {
                @Override
                protected boolean place(ItemPlacementContext context, BlockState state) {
                    return super.place(context, state.with(Properties.AGE_7, 2));
                }
            }, new Item.Settings().maxCount(64));

    public static final Item CARROT_CROP_3 = registerItems("carrot_crop_3",
            settings -> new BlockItem(Blocks.CARROTS, settings) {
                @Override
                protected boolean place(ItemPlacementContext context, BlockState state) {
                    return super.place(context, state.with(Properties.AGE_7, 3));
                }
            }, new Item.Settings().maxCount(64));

    public static final Item CARROT_CROP_4 = registerItems("carrot_crop_4",
            settings -> new BlockItem(Blocks.CARROTS, settings) {
                @Override
                protected boolean place(ItemPlacementContext context, BlockState state) {
                    return super.place(context, state.with(Properties.AGE_7, 4));
                }
            }, new Item.Settings().maxCount(64));

    public static final Item CARROT_CROP_5 = registerItems("carrot_crop_5",
            settings -> new BlockItem(Blocks.CARROTS, settings) {
                @Override
                protected boolean place(ItemPlacementContext context, BlockState state) {
                    return super.place(context, state.with(Properties.AGE_7, 5));
                }
            }, new Item.Settings().maxCount(64));

    public static final Item CARROT_CROP_6 = registerItems("carrot_crop_6",
            settings -> new BlockItem(Blocks.CARROTS, settings) {
                @Override
                protected boolean place(ItemPlacementContext context, BlockState state) {
                    return super.place(context, state.with(Properties.AGE_7, 6));
                }
            }, new Item.Settings().maxCount(64));

    public static final Item CARROT_CROP_7 = registerItems("carrot_crop_7",
            settings -> new BlockItem(Blocks.CARROTS, settings) {
                @Override
                protected boolean place(ItemPlacementContext context, BlockState state) {
                    return super.place(context, state.with(Properties.AGE_7, 7));
                }
            }, new Item.Settings().maxCount(64));

    // 马铃薯 - 8个生长阶段 (0-7)
    public static final Item POTATO_CROP_0 = registerItems("potato_crop_0",
            settings -> new BlockItem(Blocks.POTATOES, settings) {
                @Override
                protected boolean place(ItemPlacementContext context, BlockState state) {
                    return super.place(context, state.with(Properties.AGE_7, 0));
                }
            }, new Item.Settings().maxCount(64));

    public static final Item POTATO_CROP_1 = registerItems("potato_crop_1",
            settings -> new BlockItem(Blocks.POTATOES, settings) {
                @Override
                protected boolean place(ItemPlacementContext context, BlockState state) {
                    return super.place(context, state.with(Properties.AGE_7, 1));
                }
            }, new Item.Settings().maxCount(64));

    public static final Item POTATO_CROP_2 = registerItems("potato_crop_2",
            settings -> new BlockItem(Blocks.POTATOES, settings) {
                @Override
                protected boolean place(ItemPlacementContext context, BlockState state) {
                    return super.place(context, state.with(Properties.AGE_7, 2));
                }
            }, new Item.Settings().maxCount(64));

    public static final Item POTATO_CROP_3 = registerItems("potato_crop_3",
            settings -> new BlockItem(Blocks.POTATOES, settings) {
                @Override
                protected boolean place(ItemPlacementContext context, BlockState state) {
                    return super.place(context, state.with(Properties.AGE_7, 3));
                }
            }, new Item.Settings().maxCount(64));

    public static final Item POTATO_CROP_4 = registerItems("potato_crop_4",
            settings -> new BlockItem(Blocks.POTATOES, settings) {
                @Override
                protected boolean place(ItemPlacementContext context, BlockState state) {
                    return super.place(context, state.with(Properties.AGE_7, 4));
                }
            }, new Item.Settings().maxCount(64));

    public static final Item POTATO_CROP_5 = registerItems("potato_crop_5",
            settings -> new BlockItem(Blocks.POTATOES, settings) {
                @Override
                protected boolean place(ItemPlacementContext context, BlockState state) {
                    return super.place(context, state.with(Properties.AGE_7, 5));
                }
            }, new Item.Settings().maxCount(64));

    public static final Item POTATO_CROP_6 = registerItems("potato_crop_6",
            settings -> new BlockItem(Blocks.POTATOES, settings) {
                @Override
                protected boolean place(ItemPlacementContext context, BlockState state) {
                    return super.place(context, state.with(Properties.AGE_7, 6));
                }
            }, new Item.Settings().maxCount(64));

    public static final Item POTATO_CROP_7 = registerItems("potato_crop_7",
            settings -> new BlockItem(Blocks.POTATOES, settings) {
                @Override
                protected boolean place(ItemPlacementContext context, BlockState state) {
                    return super.place(context, state.with(Properties.AGE_7, 7));
                }
            }, new Item.Settings().maxCount(64));

    // 甜菜根 - 4个生长阶段 (0-3)
    public static final Item BEETROOT_CROP_0 = registerItems("beetroot_crop_0",
            settings -> new BlockItem(Blocks.BEETROOTS, settings) {
                @Override
                protected boolean place(ItemPlacementContext context, BlockState state) {
                    return super.place(context, state.with(Properties.AGE_3, 0));
                }
            }, new Item.Settings().maxCount(64));

    public static final Item BEETROOT_CROP_1 = registerItems("beetroot_crop_1",
            settings -> new BlockItem(Blocks.BEETROOTS, settings) {
                @Override
                protected boolean place(ItemPlacementContext context, BlockState state) {
                    return super.place(context, state.with(Properties.AGE_3, 1));
                }
            }, new Item.Settings().maxCount(64));

    public static final Item BEETROOT_CROP_2 = registerItems("beetroot_crop_2",
            settings -> new BlockItem(Blocks.BEETROOTS, settings) {
                @Override
                protected boolean place(ItemPlacementContext context, BlockState state) {
                    return super.place(context, state.with(Properties.AGE_3, 2));
                }
            }, new Item.Settings().maxCount(64));

    public static final Item BEETROOT_CROP_3 = registerItems("beetroot_crop_3",
            settings -> new BlockItem(Blocks.BEETROOTS, settings) {
                @Override
                protected boolean place(ItemPlacementContext context, BlockState state) {
                    return super.place(context, state.with(Properties.AGE_3, 3));
                }
            }, new Item.Settings().maxCount(64));

    // 西瓜茎 - 8个生长阶段 (0-7)
    public static final Item MELON_STEM_0 = registerItems("melon_stem_0",
            settings -> new BlockItem(Blocks.MELON_STEM, settings) {
                @Override
                protected boolean place(ItemPlacementContext context, BlockState state) {
                    return super.place(context, state.with(Properties.AGE_7, 0));
                }
            }, new Item.Settings().maxCount(64));

    public static final Item MELON_STEM_1 = registerItems("melon_stem_1",
            settings -> new BlockItem(Blocks.MELON_STEM, settings) {
                @Override
                protected boolean place(ItemPlacementContext context, BlockState state) {
                    return super.place(context, state.with(Properties.AGE_7, 1));
                }
            }, new Item.Settings().maxCount(64));

    public static final Item MELON_STEM_2 = registerItems("melon_stem_2",
            settings -> new BlockItem(Blocks.MELON_STEM, settings) {
                @Override
                protected boolean place(ItemPlacementContext context, BlockState state) {
                    return super.place(context, state.with(Properties.AGE_7, 2));
                }
            }, new Item.Settings().maxCount(64));

    public static final Item MELON_STEM_3 = registerItems("melon_stem_3",
            settings -> new BlockItem(Blocks.MELON_STEM, settings) {
                @Override
                protected boolean place(ItemPlacementContext context, BlockState state) {
                    return super.place(context, state.with(Properties.AGE_7, 3));
                }
            }, new Item.Settings().maxCount(64));

    public static final Item MELON_STEM_4 = registerItems("melon_stem_4",
            settings -> new BlockItem(Blocks.MELON_STEM, settings) {
                @Override
                protected boolean place(ItemPlacementContext context, BlockState state) {
                    return super.place(context, state.with(Properties.AGE_7, 4));
                }
            }, new Item.Settings().maxCount(64));

    public static final Item MELON_STEM_5 = registerItems("melon_stem_5",
            settings -> new BlockItem(Blocks.MELON_STEM, settings) {
                @Override
                protected boolean place(ItemPlacementContext context, BlockState state) {
                    return super.place(context, state.with(Properties.AGE_7, 5));
                }
            }, new Item.Settings().maxCount(64));

    public static final Item MELON_STEM_6 = registerItems("melon_stem_6",
            settings -> new BlockItem(Blocks.MELON_STEM, settings) {
                @Override
                protected boolean place(ItemPlacementContext context, BlockState state) {
                    return super.place(context, state.with(Properties.AGE_7, 6));
                }
            }, new Item.Settings().maxCount(64));

    public static final Item MELON_STEM_7 = registerItems("melon_stem_7",
            settings -> new BlockItem(Blocks.MELON_STEM, settings) {
                @Override
                protected boolean place(ItemPlacementContext context, BlockState state) {
                    return super.place(context, state.with(Properties.AGE_7, 7));
                }
            }, new Item.Settings().maxCount(64));

    // 南瓜茎 - 8个生长阶段 (0-7)
    public static final Item PUMPKIN_STEM_0 = registerItems("pumpkin_stem_0",
            settings -> new BlockItem(Blocks.PUMPKIN_STEM, settings) {
                @Override
                protected boolean place(ItemPlacementContext context, BlockState state) {
                    return super.place(context, state.with(Properties.AGE_7, 0));
                }
            }, new Item.Settings().maxCount(64));

    public static final Item PUMPKIN_STEM_1 = registerItems("pumpkin_stem_1",
            settings -> new BlockItem(Blocks.PUMPKIN_STEM, settings) {
                @Override
                protected boolean place(ItemPlacementContext context, BlockState state) {
                    return super.place(context, state.with(Properties.AGE_7, 1));
                }
            }, new Item.Settings().maxCount(64));

    public static final Item PUMPKIN_STEM_2 = registerItems("pumpkin_stem_2",
            settings -> new BlockItem(Blocks.PUMPKIN_STEM, settings) {
                @Override
                protected boolean place(ItemPlacementContext context, BlockState state) {
                    return super.place(context, state.with(Properties.AGE_7, 2));
                }
            }, new Item.Settings().maxCount(64));

    public static final Item PUMPKIN_STEM_3 = registerItems("pumpkin_stem_3",
            settings -> new BlockItem(Blocks.PUMPKIN_STEM, settings) {
                @Override
                protected boolean place(ItemPlacementContext context, BlockState state) {
                    return super.place(context, state.with(Properties.AGE_7, 3));
                }
            }, new Item.Settings().maxCount(64));

    public static final Item PUMPKIN_STEM_4 = registerItems("pumpkin_stem_4",
            settings -> new BlockItem(Blocks.PUMPKIN_STEM, settings) {
                @Override
                protected boolean place(ItemPlacementContext context, BlockState state) {
                    return super.place(context, state.with(Properties.AGE_7, 4));
                }
            }, new Item.Settings().maxCount(64));

    public static final Item PUMPKIN_STEM_5 = registerItems("pumpkin_stem_5",
            settings -> new BlockItem(Blocks.PUMPKIN_STEM, settings) {
                @Override
                protected boolean place(ItemPlacementContext context, BlockState state) {
                    return super.place(context, state.with(Properties.AGE_7, 5));
                }
            }, new Item.Settings().maxCount(64));

    public static final Item PUMPKIN_STEM_6 = registerItems("pumpkin_stem_6",
            settings -> new BlockItem(Blocks.PUMPKIN_STEM, settings) {
                @Override
                protected boolean place(ItemPlacementContext context, BlockState state) {
                    return super.place(context, state.with(Properties.AGE_7, 6));
                }
            }, new Item.Settings().maxCount(64));

    public static final Item PUMPKIN_STEM_7 = registerItems("pumpkin_stem_7",
            settings -> new BlockItem(Blocks.PUMPKIN_STEM, settings) {
                @Override
                protected boolean place(ItemPlacementContext context, BlockState state) {
                    return super.place(context, state.with(Properties.AGE_7, 7));
                }
            }, new Item.Settings().maxCount(64));

    // 甘蔗 - 16个生长阶段 (0-15)
    public static final Item SUGAR_CANE_0 = registerItems("sugar_cane_0",
            settings -> new BlockItem(Blocks.SUGAR_CANE, settings) {
                @Override
                protected boolean place(ItemPlacementContext context, BlockState state) {
                    return super.place(context, state.with(Properties.AGE_15, 0));
                }
            }, new Item.Settings().maxCount(64));

    public static final Item SUGAR_CANE_1 = registerItems("sugar_cane_1",
            settings -> new BlockItem(Blocks.SUGAR_CANE, settings) {
                @Override
                protected boolean place(ItemPlacementContext context, BlockState state) {
                    return super.place(context, state.with(Properties.AGE_15, 1));
                }
            }, new Item.Settings().maxCount(64));

    public static final Item SUGAR_CANE_2 = registerItems("sugar_cane_2",
            settings -> new BlockItem(Blocks.SUGAR_CANE, settings) {
                @Override
                protected boolean place(ItemPlacementContext context, BlockState state) {
                    return super.place(context, state.with(Properties.AGE_15, 2));
                }
            }, new Item.Settings().maxCount(64));

    public static final Item SUGAR_CANE_3 = registerItems("sugar_cane_3",
            settings -> new BlockItem(Blocks.SUGAR_CANE, settings) {
                @Override
                protected boolean place(ItemPlacementContext context, BlockState state) {
                    return super.place(context, state.with(Properties.AGE_15, 3));
                }
            }, new Item.Settings().maxCount(64));

    public static final Item SUGAR_CANE_4 = registerItems("sugar_cane_4",
            settings -> new BlockItem(Blocks.SUGAR_CANE, settings) {
                @Override
                protected boolean place(ItemPlacementContext context, BlockState state) {
                    return super.place(context, state.with(Properties.AGE_15, 4));
                }
            }, new Item.Settings().maxCount(64));

    public static final Item SUGAR_CANE_5 = registerItems("sugar_cane_5",
            settings -> new BlockItem(Blocks.SUGAR_CANE, settings) {
                @Override
                protected boolean place(ItemPlacementContext context, BlockState state) {
                    return super.place(context, state.with(Properties.AGE_15, 5));
                }
            }, new Item.Settings().maxCount(64));

    public static final Item SUGAR_CANE_6 = registerItems("sugar_cane_6",
            settings -> new BlockItem(Blocks.SUGAR_CANE, settings) {
                @Override
                protected boolean place(ItemPlacementContext context, BlockState state) {
                    return super.place(context, state.with(Properties.AGE_15, 6));
                }
            }, new Item.Settings().maxCount(64));

    public static final Item SUGAR_CANE_7 = registerItems("sugar_cane_7",
            settings -> new BlockItem(Blocks.SUGAR_CANE, settings) {
                @Override
                protected boolean place(ItemPlacementContext context, BlockState state) {
                    return super.place(context, state.with(Properties.AGE_15, 7));
                }
            }, new Item.Settings().maxCount(64));

    public static final Item SUGAR_CANE_8 = registerItems("sugar_cane_8",
            settings -> new BlockItem(Blocks.SUGAR_CANE, settings) {
                @Override
                protected boolean place(ItemPlacementContext context, BlockState state) {
                    return super.place(context, state.with(Properties.AGE_15, 8));
                }
            }, new Item.Settings().maxCount(64));

    public static final Item SUGAR_CANE_9 = registerItems("sugar_cane_9",
            settings -> new BlockItem(Blocks.SUGAR_CANE, settings) {
                @Override
                protected boolean place(ItemPlacementContext context, BlockState state) {
                    return super.place(context, state.with(Properties.AGE_15, 9));
                }
            }, new Item.Settings().maxCount(64));

    public static final Item SUGAR_CANE_10 = registerItems("sugar_cane_10",
            settings -> new BlockItem(Blocks.SUGAR_CANE, settings) {
                @Override
                protected boolean place(ItemPlacementContext context, BlockState state) {
                    return super.place(context, state.with(Properties.AGE_15, 10));
                }
            }, new Item.Settings().maxCount(64));

    public static final Item SUGAR_CANE_11 = registerItems("sugar_cane_11",
            settings -> new BlockItem(Blocks.SUGAR_CANE, settings) {
                @Override
                protected boolean place(ItemPlacementContext context, BlockState state) {
                    return super.place(context, state.with(Properties.AGE_15, 11));
                }
            }, new Item.Settings().maxCount(64));

    public static final Item SUGAR_CANE_12 = registerItems("sugar_cane_12",
            settings -> new BlockItem(Blocks.SUGAR_CANE, settings) {
                @Override
                protected boolean place(ItemPlacementContext context, BlockState state) {
                    return super.place(context, state.with(Properties.AGE_15, 12));
                }
            }, new Item.Settings().maxCount(64));

    public static final Item SUGAR_CANE_13 = registerItems("sugar_cane_13",
            settings -> new BlockItem(Blocks.SUGAR_CANE, settings) {
                @Override
                protected boolean place(ItemPlacementContext context, BlockState state) {
                    return super.place(context, state.with(Properties.AGE_15, 13));
                }
            }, new Item.Settings().maxCount(64));

    public static final Item SUGAR_CANE_14 = registerItems("sugar_cane_14",
            settings -> new BlockItem(Blocks.SUGAR_CANE, settings) {
                @Override
                protected boolean place(ItemPlacementContext context, BlockState state) {
                    return super.place(context, state.with(Properties.AGE_15, 14));
                }
            }, new Item.Settings().maxCount(64));

    public static final Item SUGAR_CANE_15 = registerItems("sugar_cane_15",
            settings -> new BlockItem(Blocks.SUGAR_CANE, settings) {
                @Override
                protected boolean place(ItemPlacementContext context, BlockState state) {
                    return super.place(context, state.with(Properties.AGE_15, 15));
                }
            }, new Item.Settings().maxCount(64));

    // 辅助方法：注册方块物品
    private static Item registerBlockItem(String path, Block block) {
        return registerItems(path,
                settings -> new BlockItem(block, settings),
                new Item.Settings().maxCount(64));
    }

    public static Item registerItems(String path, Function<Item.Settings, Item> factory, Item.Settings settings) {
        Identifier itemId = Identifier.of("flyconfig", path);
        RegistryKey<Item> registryKey = RegistryKey.of(RegistryKeys.ITEM, itemId);
        Item item = factory.apply(settings.registryKey(registryKey));

        if (item instanceof BlockItem blockItem) {
            blockItem.appendBlocks(Item.BLOCK_ITEMS, item);
        }

        return Registry.register(Registries.ITEM, registryKey, item);
    }

    public static Item registerItems(String path, Function<Item.Settings, Item> factory) {
        return registerItems(path, factory, new Item.Settings());
    }

    public static void initialize() {
    }

    public static void registerModItems() {
        FlightManagement.LOGGER.info("Registering ModItems.");
    }
}