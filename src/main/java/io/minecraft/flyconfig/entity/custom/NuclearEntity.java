package io.minecraft.flyconfig.entity.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class NuclearEntity extends PassiveEntity {

    private static final TrackedData<Integer> FUSE_SPEED = DataTracker.registerData(NuclearEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Boolean> IGNITED = DataTracker.registerData(NuclearEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> CHARGED = DataTracker.registerData(NuclearEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    private int lastFuseTime;
    private int currentFuseTime;
    private int fuseTime = 700;
    private int destroyRadius = 128;
    private int damageRadius = 256;
    private int warningRadius = 175;
    private boolean exploded = false;
    private int blocksDestroyed = 0;
    private List<BlockPos> pendingBlocks = new ArrayList<>();
    private int destroyStage = 0;
    private int stageTimer = 0;
    private boolean destroying = false;
    private boolean damageApplied = false;

    private static final Identifier NUCLEAR_SOUND_ID = Identifier.of("flyconfig", "nuclear");

    public NuclearEntity(EntityType<? extends PassiveEntity> entityType, World world) {
        super(entityType, world);
        this.setNoGravity(false);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(FUSE_SPEED, -1);
        builder.add(IGNITED, false);
        builder.add(CHARGED, false);
    }

    public static DefaultAttributeContainer.Builder createNuclearAttributes() {
        return PassiveEntity.createMobAttributes()
                .add(EntityAttributes.MAX_HEALTH, 9999.0D)
                .add(EntityAttributes.MOVEMENT_SPEED, 0.0D)
                .add(EntityAttributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
    }

    public boolean isInvulnerableTo(DamageSource damageSource) {
        return true;
    }

    @Override
    public boolean isFireImmune() {
        return true;
    }

    @Override
    public boolean damage(ServerWorld world, DamageSource source, float amount) {
        return false;
    }

    public void knockback(double strength, double x, double z) {
    }

    @Override
    public void pushAwayFrom(Entity entity) {
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    public boolean isCharged() {
        return this.dataTracker.get(CHARGED);
    }

    public void setCharged(boolean charged) {
        this.dataTracker.set(CHARGED, charged);
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        var itemStack = player.getStackInHand(hand);
        if (itemStack.isIn(ItemTags.CREEPER_IGNITERS)) {
            SoundEvent soundEvent = itemStack.getItem() == net.minecraft.item.Items.FIRE_CHARGE ? SoundEvents.ITEM_FIRECHARGE_USE : SoundEvents.ITEM_FLINTANDSTEEL_USE;
            this.getWorld().playSound(player, this.getX(), this.getY(), this.getZ(), soundEvent, this.getSoundCategory(), 1.0F, this.random.nextFloat() * 0.4F + 0.8F);
            if (!this.getWorld().isClient) {
                this.ignite();
                if (!itemStack.isDamageable()) {
                    itemStack.decrement(1);
                } else {
                    itemStack.damage(1, player, getSlotForHand(hand));
                }
            }
            return ActionResult.SUCCESS;
        }
        return super.interactMob(player, hand);
    }

    public int getFuseSpeed() {
        return this.dataTracker.get(FUSE_SPEED);
    }

    public void setFuseSpeed(int fuseSpeed) {
        this.dataTracker.set(FUSE_SPEED, fuseSpeed);
    }

    public float getLerpedFuseTime(float tickProgress) {
        return MathHelper.lerp(tickProgress, (float)this.lastFuseTime, (float)this.currentFuseTime) / (this.fuseTime - 2);
    }

    private void sendWarningToPlayers(String key, Object... args) {
        if (this.getWorld().isClient) return;
        ServerWorld serverWorld = (ServerWorld) this.getWorld();
        List<ServerPlayerEntity> players = serverWorld.getPlayers();
        Vec3d pos = this.getPos();

        for (ServerPlayerEntity player : players) {
            double distance = player.getPos().distanceTo(pos);
            if (distance <= this.warningRadius) {
                player.sendMessage(Text.translatable(key, args), false);
            }
        }
    }

    private void playNuclearSound() {
        if (this.getWorld().isClient) return;
        ServerWorld serverWorld = (ServerWorld) this.getWorld();
        Vec3d pos = this.getPos();

        serverWorld.getPlayers().forEach(player -> {
            double distance = player.getPos().distanceTo(pos);
            if (distance <= 200.0D) {
                float volume = (float) (1.0D - distance / 200.0D);
                SoundEvent soundEvent = SoundEvent.of(NUCLEAR_SOUND_ID);
                player.playSoundToPlayer(soundEvent, net.minecraft.sound.SoundCategory.MASTER, volume, 1.0F);
            }
        });
    }

    private boolean isFullBlock(ServerWorld world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        if (state.isAir()) return false;
        return state.isFullCube(world, pos);
    }

    private void applyWitherEffectToNearbyPlayers(ServerWorld world, Vec3d center) {
        if (!this.isIgnited()) {
            int radius = 15;
            Box effectBox = new Box(
                    center.x - radius, center.y - radius, center.z - radius,
                    center.x + radius, center.y + radius, center.z + radius
            );

            List<PlayerEntity> players = world.getEntitiesByClass(
                    PlayerEntity.class, effectBox, player -> player.isAlive() && player.getPos().distanceTo(center) <= radius
            );

            for (PlayerEntity player : players) {
                Vec3d toPlayer = player.getPos().subtract(center);
                double distance = player.getPos().distanceTo(center);

                if (distance < 0.1) {
                    player.addStatusEffect(new net.minecraft.entity.effect.StatusEffectInstance(
                            net.minecraft.entity.effect.StatusEffects.WITHER, 500, 1
                    ));
                    continue;
                }

                int blockCount = 0;
                for (double step = 0.5; step < distance; step += 0.5) {
                    Vec3d checkPos = center.add(toPlayer.normalize().multiply(step));
                    BlockPos checkBlock = BlockPos.ofFloored(checkPos);
                    if (checkBlock.equals(BlockPos.ofFloored(center))) continue;
                    if (checkBlock.equals(BlockPos.ofFloored(player.getPos()))) break;
                    if (isFullBlock(world, checkBlock)) {
                        blockCount++;
                        if (blockCount >= 2) break;
                    }
                }

                if (blockCount < 2) {
                    int amplifier = Math.max(0, 1 - blockCount);
                    player.addStatusEffect(new net.minecraft.entity.effect.StatusEffectInstance(
                            net.minecraft.entity.effect.StatusEffects.WITHER, 500, amplifier
                    ));
                }
            }
        } else {
            int radius = 50;
            Box effectBox = new Box(
                    center.x - radius, center.y - radius, center.z - radius,
                    center.x + radius, center.y + radius, center.z + radius
            );

            List<PlayerEntity> players = world.getEntitiesByClass(
                    PlayerEntity.class, effectBox, player -> player.isAlive() && player.getPos().distanceTo(center) <= radius
            );

            for (PlayerEntity player : players) {
                Vec3d toPlayer = player.getPos().subtract(center);
                double distance = player.getPos().distanceTo(center);

                if (distance < 0.1) {
                    player.addStatusEffect(new net.minecraft.entity.effect.StatusEffectInstance(
                            net.minecraft.entity.effect.StatusEffects.WITHER, 2000, 7
                    ));
                    continue;
                }

                int blockCount = 0;
                for (double step = 0.5; step < distance; step += 0.5) {
                    Vec3d checkPos = center.add(toPlayer.normalize().multiply(step));
                    BlockPos checkBlock = BlockPos.ofFloored(checkPos);
                    if (checkBlock.equals(BlockPos.ofFloored(center))) continue;
                    if (checkBlock.equals(BlockPos.ofFloored(player.getPos()))) break;
                    if (isFullBlock(world, checkBlock)) {
                        blockCount++;
                        if (blockCount >= 3) break;
                    }
                }

                if (blockCount < 3) {
                    int amplifier = Math.max(0, 7 - blockCount);
                    player.addStatusEffect(new net.minecraft.entity.effect.StatusEffectInstance(
                            net.minecraft.entity.effect.StatusEffects.WITHER, 2000, amplifier
                    ));
                }
            }
        }
    }

    @Override
    public void tick() {
        if (this.isAlive()) {
            if (!this.exploded) {
                this.lastFuseTime = this.currentFuseTime;

                if (this.isIgnited()) {
                    this.setFuseSpeed(1);
                }

                int i = this.getFuseSpeed();
                if (i > 0 && this.currentFuseTime == 0) {
                    this.playSound(SoundEvents.ENTITY_CREEPER_PRIMED, 1.0F, 0.5F);
                }

                this.currentFuseTime += i;
                if (this.currentFuseTime < 0) {
                    this.currentFuseTime = 0;
                }

                if (this.isIgnited() && this.currentFuseTime > 0) {
                    if (this.currentFuseTime % 20 == 0) {
                        int remainingSeconds = (this.fuseTime - this.currentFuseTime) / 20;
                        this.sendWarningToPlayers("nuclear.warning.countdown", remainingSeconds);
                    }
                    if (this.currentFuseTime % 10 == 0) {
                        this.playNuclearSound();
                    }
                    if (this.currentFuseTime % 5 == 0) {
                        if (this.getWorld() instanceof ServerWorld serverWorld) {
                            this.applyWitherEffectToNearbyPlayers(serverWorld, this.getPos());
                        }
                    }
                }

                if (this.currentFuseTime >= this.fuseTime && this.isIgnited()) {
                    this.startExplosion();
                }
            } else {
                this.processExplosion();
            }
        }
        super.tick();
    }

    private boolean isIndestructibleBlock(BlockState state) {
        return state.isOf(Blocks.BEDROCK) ||
                state.isOf(Blocks.ANVIL) ||
                state.isOf(Blocks.CHIPPED_ANVIL) ||
                state.isOf(Blocks.DAMAGED_ANVIL) ||
                state.isOf(Blocks.BARRIER) ||
                state.isOf(Blocks.COMMAND_BLOCK) ||
                state.isOf(Blocks.CHAIN_COMMAND_BLOCK) ||
                state.isOf(Blocks.REPEATING_COMMAND_BLOCK) ||
                state.isOf(Blocks.STRUCTURE_BLOCK) ||
                state.isOf(Blocks.STRUCTURE_VOID) ||
                state.isOf(Blocks.JIGSAW) ||
                state.isOf(Blocks.LIGHT);
    }

    private boolean isBlockingBlock(BlockState state) {
        return state.isOf(Blocks.BEDROCK) ||
                state.isOf(Blocks.ANVIL) ||
                state.isOf(Blocks.CHIPPED_ANVIL) ||
                state.isOf(Blocks.DAMAGED_ANVIL) ||
                state.isOf(Blocks.BARRIER) ||
                state.isOf(Blocks.COMMAND_BLOCK) ||
                state.isOf(Blocks.CHAIN_COMMAND_BLOCK) ||
                state.isOf(Blocks.REPEATING_COMMAND_BLOCK) ||
                state.isOf(Blocks.STRUCTURE_BLOCK) ||
                state.isOf(Blocks.STRUCTURE_VOID) ||
                state.isOf(Blocks.JIGSAW) ||
                state.isOf(Blocks.LIGHT);
    }

    private void startExplosion() {
        if (this.getWorld() instanceof ServerWorld serverWorld && !this.exploded) {
            this.exploded = true;
            this.dead = true;
            Vec3d center = this.getPos();
            BlockPos centerPos = BlockPos.ofFloored(center);

            int radius = this.destroyRadius;
            int radiusSq = radius * radius;

            this.pendingBlocks.clear();
            this.blocksDestroyed = 0;
            this.destroyStage = 0;
            this.destroying = true;
            this.damageApplied = false;

            for (int dx = -radius; dx <= radius; dx++) {
                for (int dy = -radius; dy <= radius; dy++) {
                    for (int dz = -radius; dz <= radius; dz++) {
                        int distSq = dx * dx + dy * dy + dz * dz;
                        if (distSq > radiusSq) continue;

                        BlockPos pos = centerPos.add(dx, dy, dz);
                        BlockState state = serverWorld.getBlockState(pos);

                        if (isIndestructibleBlock(state)) continue;
                        if (state.isAir()) continue;

                        Vec3d direction = new Vec3d(dx, dy, dz).normalize();
                        double distance = Math.sqrt(distSq);

                        boolean blocked = false;
                        for (double step = 0.5; step < distance; step += 0.5) {
                            Vec3d checkPos = center.add(direction.multiply(step));
                            BlockPos checkBlock = BlockPos.ofFloored(checkPos);
                            if (checkBlock.equals(pos)) break;
                            BlockState checkState = serverWorld.getBlockState(checkBlock);
                            if (isBlockingBlock(checkState)) {
                                blocked = true;
                                break;
                            }
                        }

                        if (!blocked) {
                            this.pendingBlocks.add(pos);
                        }
                    }
                }
            }

            if (!this.damageApplied) {
                this.applyDamage(serverWorld, center);
                this.damageApplied = true;
            }
        }
    }

    private void applyDamage(ServerWorld serverWorld, Vec3d center) {
        Box damageBox = new Box(
                center.x - this.damageRadius, center.y - this.damageRadius, center.z - this.damageRadius,
                center.x + this.damageRadius, center.y + this.damageRadius, center.z + this.damageRadius
        );

        List<net.minecraft.entity.LivingEntity> entities = serverWorld.getEntitiesByClass(
                net.minecraft.entity.LivingEntity.class, damageBox, entity -> entity != this && entity.isAlive());

        for (net.minecraft.entity.LivingEntity entity : entities) {
            double distance = entity.getPos().distanceTo(center);
            if (distance <= this.damageRadius) {
                Vec3d toEntity = entity.getPos().subtract(center);
                boolean blocked = false;
                for (double step = 0.5; step < distance; step += 0.5) {
                    Vec3d checkPos = center.add(toEntity.normalize().multiply(step));
                    BlockPos checkBlock = BlockPos.ofFloored(checkPos);
                    if (checkBlock.equals(BlockPos.ofFloored(entity.getPos()))) break;
                    BlockState checkState = serverWorld.getBlockState(checkBlock);
                    if (isBlockingBlock(checkState)) {
                        blocked = true;
                        break;
                    }
                }

                if (!blocked) {
                    float damage = 1500.0F;
                    entity.damage(serverWorld, serverWorld.getDamageSources().explosion(this, null), damage);
                }
            }
        }
    }

    private void processExplosion() {
        if (!this.destroying) return;
        if (this.getWorld().isClient) return;

        ServerWorld serverWorld = (ServerWorld) this.getWorld();

        int batchSize = Math.min(15000, this.pendingBlocks.size());
        int processed = 0;

        while (processed < batchSize && !this.pendingBlocks.isEmpty()) {
            BlockPos pos = this.pendingBlocks.removeFirst();
            BlockState state = serverWorld.getBlockState(pos);
            if (!state.isAir() && !isIndestructibleBlock(state)) {
                serverWorld.setBlockState(pos, Blocks.AIR.getDefaultState(), Block.NOTIFY_LISTENERS | Block.FORCE_STATE);
                this.blocksDestroyed++;
            }
            processed++;
        }

        if (this.pendingBlocks.isEmpty()) {
            this.destroying = false;
            this.placeFire(serverWorld);
            this.onRemoval(serverWorld, net.minecraft.entity.Entity.RemovalReason.KILLED);
            this.discard();
        }
    }

    private void placeFire(ServerWorld world) {
        Vec3d center = this.getPos();
        BlockPos centerPos = BlockPos.ofFloored(center);
        int fireRadius = 315;
        int clearRadius = 500;

        for (int dx = -fireRadius; dx <= fireRadius; dx++) {
            for (int dy = -fireRadius; dy <= fireRadius; dy++) {
                for (int dz = -fireRadius; dz <= fireRadius; dz++) {
                    int distSq = dx * dx + dy * dy + dz * dz;
                    if (distSq > fireRadius * fireRadius) continue;

                    BlockPos pos = centerPos.add(dx, dy, dz);
                    BlockState state = world.getBlockState(pos);

                    if (!state.isAir()) continue;

                    BlockPos below = pos.down();
                    if (world.getBlockState(below).isAir()) continue;

                    world.setBlockState(pos, Blocks.FIRE.getDefaultState(), Block.NOTIFY_LISTENERS);
                }
            }
        }

        for (int dx = -clearRadius; dx <= clearRadius; dx++) {
            for (int dy = -clearRadius; dy <= clearRadius; dy++) {
                for (int dz = -clearRadius; dz <= clearRadius; dz++) {
                    int distSq = dx * dx + dy * dy + dz * dz;
                    if (distSq > clearRadius * clearRadius) continue;

                    BlockPos pos = centerPos.add(dx, dy, dz);
                    BlockState state = world.getBlockState(pos);

                    if (state.getFluidState().isStill() &&
                            (state.getFluidState().isIn(net.minecraft.registry.tag.FluidTags.WATER) ||
                                    state.getFluidState().isIn(net.minecraft.registry.tag.FluidTags.LAVA))) {
                        world.setBlockState(pos, Blocks.AIR.getDefaultState(), Block.NOTIFY_LISTENERS | Block.FORCE_STATE);
                        continue;
                    }

                    if (state.isOf(Blocks.WATER) || state.isOf(Blocks.LAVA) ||
                            state.isOf(Blocks.BUBBLE_COLUMN) || state.isOf(Blocks.KELP) ||
                            state.isOf(Blocks.KELP_PLANT) || state.isOf(Blocks.SEAGRASS) ||
                            state.isOf(Blocks.TALL_SEAGRASS)) {
                        world.setBlockState(pos, Blocks.AIR.getDefaultState(), Block.NOTIFY_LISTENERS | Block.FORCE_STATE);
                        continue;
                    }

                    if (state.isOf(Blocks.DIRT) || state.isOf(Blocks.GRASS_BLOCK) ||
                            state.isOf(Blocks.FARMLAND) || state.isOf(Blocks.DIRT_PATH) ||
                            state.isOf(Blocks.PODZOL) || state.isOf(Blocks.MYCELIUM)) {
                        world.setBlockState(pos, Blocks.ROOTED_DIRT.getDefaultState(), Block.NOTIFY_LISTENERS | Block.FORCE_STATE);
                        continue;
                    }

                    if (state.isOf(Blocks.STONE) || state.isOf(Blocks.DEEPSLATE) ||
                            state.isOf(Blocks.GRANITE) || state.isOf(Blocks.DIORITE) ||
                            state.isOf(Blocks.ANDESITE) || state.isOf(Blocks.CALCITE) ||
                            state.isOf(Blocks.TUFF)) {
                        world.setBlockState(pos, Blocks.COBBLESTONE.getDefaultState(), Block.NOTIFY_LISTENERS | Block.FORCE_STATE);
                        continue;
                    }

                    if (state.isOf(Blocks.DRIPSTONE_BLOCK) || state.isOf(Blocks.POINTED_DRIPSTONE) ||
                            state.isOf(Blocks.MUD) || state.isOf(Blocks.CLAY) ||
                            state.isOf(Blocks.SAND) || state.isOf(Blocks.GRAVEL) ||
                            state.isOf(Blocks.SNOW) || state.isOf(Blocks.SNOW_BLOCK) ||
                            state.isOf(Blocks.MOSS_BLOCK)) {
                        world.setBlockState(pos, Blocks.PALE_MOSS_BLOCK.getDefaultState(), Block.NOTIFY_LISTENERS | Block.FORCE_STATE);
                        continue;
                    }

                    if (state.isOf(Blocks.TUBE_CORAL_BLOCK) || state.isOf(Blocks.BRAIN_CORAL_BLOCK) ||
                            state.isOf(Blocks.BUBBLE_CORAL_BLOCK) || state.isOf(Blocks.FIRE_CORAL_BLOCK) ||
                            state.isOf(Blocks.HORN_CORAL_BLOCK) || state.isOf(Blocks.TUBE_CORAL) ||
                            state.isOf(Blocks.BRAIN_CORAL) || state.isOf(Blocks.BUBBLE_CORAL) ||
                            state.isOf(Blocks.FIRE_CORAL) || state.isOf(Blocks.HORN_CORAL) ||
                            state.isOf(Blocks.TUBE_CORAL_FAN) || state.isOf(Blocks.BRAIN_CORAL_FAN) ||
                            state.isOf(Blocks.BUBBLE_CORAL_FAN) || state.isOf(Blocks.FIRE_CORAL_FAN) ||
                            state.isOf(Blocks.HORN_CORAL_FAN) || state.isOf(Blocks.TUBE_CORAL_WALL_FAN) ||
                            state.isOf(Blocks.BRAIN_CORAL_WALL_FAN) || state.isOf(Blocks.BUBBLE_CORAL_WALL_FAN) ||
                            state.isOf(Blocks.FIRE_CORAL_WALL_FAN) || state.isOf(Blocks.HORN_CORAL_WALL_FAN)) {
                        world.setBlockState(pos, Blocks.DEAD_TUBE_CORAL_BLOCK.getDefaultState(), Block.NOTIFY_LISTENERS | Block.FORCE_STATE);
                        continue;
                    }

                    if (state.isOf(Blocks.OAK_LEAVES) || state.isOf(Blocks.SPRUCE_LEAVES) ||
                            state.isOf(Blocks.BIRCH_LEAVES) || state.isOf(Blocks.JUNGLE_LEAVES) ||
                            state.isOf(Blocks.ACACIA_LEAVES) || state.isOf(Blocks.DARK_OAK_LEAVES) ||
                            state.isOf(Blocks.MANGROVE_LEAVES) || state.isOf(Blocks.CHERRY_LEAVES) ||
                            state.isOf(Blocks.AZALEA_LEAVES) || state.isOf(Blocks.FLOWERING_AZALEA_LEAVES)) {
                        world.setBlockState(pos, Blocks.PALE_OAK_LEAVES.getDefaultState(), Block.NOTIFY_LISTENERS | Block.FORCE_STATE);
                        continue;
                    }

                    if (state.isOf(Blocks.DANDELION) || state.isOf(Blocks.POPPY) ||
                            state.isOf(Blocks.BLUE_ORCHID) || state.isOf(Blocks.ALLIUM) ||
                            state.isOf(Blocks.AZURE_BLUET) || state.isOf(Blocks.RED_TULIP) ||
                            state.isOf(Blocks.ORANGE_TULIP) || state.isOf(Blocks.WHITE_TULIP) ||
                            state.isOf(Blocks.PINK_TULIP) || state.isOf(Blocks.OXEYE_DAISY) ||
                            state.isOf(Blocks.CORNFLOWER) || state.isOf(Blocks.LILY_OF_THE_VALLEY) ||
                            state.isOf(Blocks.WITHER_ROSE) || state.isOf(Blocks.SUNFLOWER) ||
                            state.isOf(Blocks.LILAC) || state.isOf(Blocks.ROSE_BUSH) ||
                            state.isOf(Blocks.PEONY) || state.isOf(Blocks.TALL_GRASS) ||
                            state.isOf(Blocks.LARGE_FERN) || state.isOf(Blocks.SHORT_GRASS) ||
                            state.isOf(Blocks.FERN) || state.isOf(Blocks.DEAD_BUSH) ||
                            state.isOf(Blocks.TORCHFLOWER) || state.isOf(Blocks.PITCHER_PLANT)) {
                        world.setBlockState(pos, Blocks.AIR.getDefaultState(), Block.NOTIFY_LISTENERS | Block.FORCE_STATE);
                        continue;
                    }

                    if (state.isOf(Blocks.OAK_LOG) || state.isOf(Blocks.SPRUCE_LOG) ||
                            state.isOf(Blocks.BIRCH_LOG) || state.isOf(Blocks.JUNGLE_LOG) ||
                            state.isOf(Blocks.ACACIA_LOG) || state.isOf(Blocks.DARK_OAK_LOG) ||
                            state.isOf(Blocks.MANGROVE_LOG) || state.isOf(Blocks.CHERRY_LOG) ||
                            state.isOf(Blocks.OAK_WOOD) || state.isOf(Blocks.SPRUCE_WOOD) ||
                            state.isOf(Blocks.BIRCH_WOOD) || state.isOf(Blocks.JUNGLE_WOOD) ||
                            state.isOf(Blocks.ACACIA_WOOD) || state.isOf(Blocks.DARK_OAK_WOOD) ||
                            state.isOf(Blocks.MANGROVE_WOOD) || state.isOf(Blocks.CHERRY_WOOD) ||
                            state.isOf(Blocks.STRIPPED_OAK_LOG) || state.isOf(Blocks.STRIPPED_SPRUCE_LOG) ||
                            state.isOf(Blocks.STRIPPED_BIRCH_LOG) || state.isOf(Blocks.STRIPPED_JUNGLE_LOG) ||
                            state.isOf(Blocks.STRIPPED_ACACIA_LOG) || state.isOf(Blocks.STRIPPED_DARK_OAK_LOG) ||
                            state.isOf(Blocks.STRIPPED_MANGROVE_LOG) || state.isOf(Blocks.STRIPPED_CHERRY_LOG)) {
                        world.setBlockState(pos, Blocks.PALE_OAK_LOG.getDefaultState(), Block.NOTIFY_LISTENERS | Block.FORCE_STATE);
                        continue;
                    }
                }
            }
        }
    }

    public boolean isIgnited() {
        return this.dataTracker.get(IGNITED);
    }

    public void ignite() {
        this.dataTracker.set(IGNITED, true);
    }

    public void setIgnited(boolean ignited) {
        this.dataTracker.set(IGNITED, ignited);
    }
}