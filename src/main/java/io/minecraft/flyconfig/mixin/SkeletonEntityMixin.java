package io.minecraft.flyconfig.mixin;

import io.minecraft.flyconfig.FlightManagement;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.ProjectileAttackGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SkeletonEntity.class)
public abstract class SkeletonEntityMixin {

    @Unique
    private int danceTimer = 0;
    @Unique
    private boolean isDancing = false;
    @Unique
    private LivingEntity targetCache = null;

    @Inject(method = "initGoals", at = @At("TAIL"))
    private void modifyGoals(CallbackInfo ci) {
        SkeletonEntity skeleton = (SkeletonEntity)(Object)this;

        skeleton.targetSelector.add(1, new ActiveTargetGoal<>(skeleton, PlayerEntity.class, true, true) {
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

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        SkeletonEntity skeleton = (SkeletonEntity)(Object)this;
        LivingEntity target = skeleton.getTarget();

        if (target instanceof PlayerEntity player) {
            if (player.isSpectator()) {
                skeleton.setTarget(null);
                target = null;
            }
        }

        if (target == null && targetCache != null) {
            startDancing(skeleton);
        } else if (target != null && targetCache == null) {
            stopDancing();
        }

        if (isDancing) {
            danceTimer++;
            if (danceTimer % 20 == 0) {
                skeleton.setYaw(skeleton.getYaw() + 45);
            }
        }

        targetCache = target;
    }

    @Unique
    private void startDancing(SkeletonEntity skeleton) {
        if (!isDancing) {
            isDancing = true;
            danceTimer = 0;
            playSound(skeleton, Identifier.of("flyconfig", "entity.skeleton.sb"), 1.0F, 1.0F);
        }
    }

    @Unique
    private void stopDancing() {
        if (isDancing) {
            isDancing = false;
            danceTimer = 0;
        }
    }

    @Inject(method = "onAttacking", at = @At("HEAD"))
    private void onAttacking(LivingEntity target, CallbackInfo ci) {
        SkeletonEntity skeleton = (SkeletonEntity)(Object)this;
        playSound(skeleton, Identifier.of("flyconfig", "entity.skeleton.donk"), 1.0F, 1.0F);
    }

    @Unique
    private void playSound(SkeletonEntity skeleton, Identifier soundId, float volume, float pitch) {
        FlightManagement.LOGGER.info("Attempting to play sound: " + soundId);

        if (!skeleton.getWorld().isClient) {
            skeleton.getWorld().playSound(
                    null,
                    skeleton.getX(),
                    skeleton.getY(),
                    skeleton.getZ(),
                    SoundEvent.of(soundId),
                    SoundCategory.HOSTILE,
                    volume,
                    pitch
            );
            FlightManagement.LOGGER.info("Sound played successfully: " + soundId);
        } else {
            FlightManagement.LOGGER.info("Sound not played (client side): " + soundId);
        }
    }
}
