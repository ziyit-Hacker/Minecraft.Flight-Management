package io.minecraft.flyconfig.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class DancingSkeletonEntity extends SkeletonEntity {

    private int danceTimer = 0;
    private boolean isDancing = false;
    private LivingEntity targetCache = null;

    public DancingSkeletonEntity(EntityType<? extends SkeletonEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(2, new ProjectileAttackGoal(this, 1.0, 20, 15.0F));
        this.goalSelector.add(3, new WanderAroundFarGoal(this, 1.0));
        this.goalSelector.add(4, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));

        // 修复1：修正 ActiveTargetGoal 的构造函数
        this.targetSelector.add(1, new ActiveTargetGoal<>(this, PlayerEntity.class, true, true) {
            @Override
            public boolean canStart() {
                if (!super.canStart()) return false;
                if (this.targetEntity instanceof PlayerEntity player) {
                    return !player.isSpectator();
                }
                return true;
            }
        });
    }

    @Override
    public void tick() {
        super.tick();

        LivingEntity target = this.getTarget();

        if (target instanceof PlayerEntity player) {
            if (player.isSpectator()) {
                this.setTarget(null);
                target = null;
            }
        }

        if (target == null && targetCache != null) {
            startDancing();
        } else if (target != null && targetCache == null) {
            stopDancing();
        }

        if (isDancing) {
            danceTimer++;
            if (danceTimer % 20 == 0) {
                this.setYaw(this.getYaw() + 45);
            }
        }

        targetCache = target;
    }

    private void startDancing() {
        if (!isDancing) {
            isDancing = true;
            danceTimer = 0;
            playSound(Identifier.of("flyconfig", "entity.skeleton.sb"), 1.0F, 1.0F);
        }
    }

    private void stopDancing() {
        if (isDancing) {
            isDancing = false;
            danceTimer = 0;
        }
    }

    public void onAttacking(LivingEntity target) {
        super.onAttacking(target);
        playSound(Identifier.of("flyconfig", "entity.skeleton.donk"), 1.0F, 1.0F);
    }

    private void playSound(Identifier soundId, float volume, float pitch) {
        if (!this.getWorld().isClient) {
            this.getWorld().playSound(
                    null,
                    this.getX(),
                    this.getY(),
                    this.getZ(),
                    SoundEvent.of(soundId),
                    SoundCategory.HOSTILE,
                    volume,
                    pitch
            );
        }
    }

    public static DefaultAttributeContainer.Builder createSkeletonAttributes() {
        return SkeletonEntity.createMobAttributes()
                .add(EntityAttributes.MAX_HEALTH, 20.0)
                .add(EntityAttributes.MOVEMENT_SPEED, 0.25);
    }

    public boolean isDancing() {
        return isDancing;
    }
}