package io.minecraft.flyconfig.mixin;

import io.minecraft.flyconfig.FlightManagement;
import io.minecraft.flyconfig.sound.ModSoundEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
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
    @Unique
    private boolean hasPlayedDonk = false;
    @Unique
    private int soundCooldown = 0;
    @Unique
    private boolean isDead = false;

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        SkeletonEntity skeleton = (SkeletonEntity)(Object)this;

        if (!skeleton.isAlive()) {
            if (!isDead) {
                isDead = true;
                isDancing = false;
                FlightManagement.LOGGER.info("Skeleton died, stopping all sounds");
            }
            return;
        }

        LivingEntity target = skeleton.getTarget();

        if (soundCooldown > 0) {
            soundCooldown--;
        }

        if (target instanceof PlayerEntity player) {
            if (player.isSpectator()) {
                skeleton.setTarget(null);
                target = null;
            }
        }

        if (target instanceof PlayerEntity) {
            if (targetCache == null) {
                if (soundCooldown == 0) {
                    isDancing = false;
                    skeleton.getWorld().playSound(
                            null,
                            skeleton.getBlockPos(),
                            ModSoundEvents.donk,
                            SoundCategory.HOSTILE,
                            1.0f,
                            1.0f
                    );
                    soundCooldown = 40;
                }
                if (isDancing) {
                    stopDancing();
                }
            }
        } else {
            if (targetCache != null) {
                if (!isDancing && soundCooldown == 0) {
                    startDancing(skeleton);
                }
            } else {
                if (!isDancing && soundCooldown == 0) {
                    startDancing(skeleton);
                }
            }
        }

        if (isDancing && skeleton.isAlive()) {
            danceTimer++;
            if (danceTimer % 20 == 0) {
                skeleton.setYaw(skeleton.getYaw() + 45);
            }
        }

        targetCache = target;
    }

    @Unique
    private void startDancing(SkeletonEntity skeleton) {
        if (!isDancing && skeleton.isAlive()) {
            isDancing = true;
            danceTimer = 0;
            skeleton.getWorld().playSound(
                    null,
                    skeleton.getBlockPos(),
                    ModSoundEvents.sb,
                    SoundCategory.HOSTILE,
                    1.0f,
                    1.0f
            );
        }
    }

    @Unique
    private void stopDancing() {
        if (isDancing) {
            isDancing = false;
            danceTimer = 0;
        }
    }

    private void onRemove(CallbackInfo ci) {
        isDancing = false;
        isDead = true;
        FlightManagement.LOGGER.info("Skeleton removed, cleaning up");
    }
}