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
    private int destroyRadius = 100;
    private int damageRadius = 200;
    private int warningRadius = 150;
    private boolean exploded = false;
    private int blocksDestroyed = 0;
    private static final int MAX_BLOCKS_PER_BATCH = 10000;
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
        return state.isOf(Blocks.OBSIDIAN) ||
                state.isOf(Blocks.CRYING_OBSIDIAN) ||
                state.isOf(Blocks.BEDROCK) ||
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
        int radius = this.destroyRadius;
        int radiusSq = radius * radius;

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    int distSq = dx * dx + dy * dy + dz * dz;
                    if (distSq > radiusSq) continue;

                    BlockPos pos = centerPos.add(dx, dy, dz);
                    BlockState state = world.getBlockState(pos);

                    if (!state.isAir()) continue;

                    BlockPos below = pos.down();
                    if (world.getBlockState(below).isAir()) continue;

                    if (world.random.nextFloat() <= 0.75F) {
                        world.setBlockState(pos, Blocks.FIRE.getDefaultState(), Block.NOTIFY_LISTENERS);
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