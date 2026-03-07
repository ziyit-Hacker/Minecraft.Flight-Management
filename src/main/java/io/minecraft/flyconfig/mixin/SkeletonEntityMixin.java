package io.minecraft.flyconfig.mixin;

import io.minecraft.flyconfig.FlightManagement;
import io.minecraft.flyconfig.sound.ModSoundEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
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
    @Unique
    private boolean isPlayingDonk = false;
    @Unique
    private boolean isPlayingSb = false;
    @Unique
    private int donkRepeatTimer = 0;
    @Unique
    private int sbRepeatTimer = 0;
    @Unique
    private static final int DONK_INTERVAL = 3020;
    @Unique
    private static final int SB_INTERVAL = 600;

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        SkeletonEntity skeleton = (SkeletonEntity)(Object)this;

        if (!skeleton.isAlive()) {
            if (!isDead) {
                isDead = true;
                isDancing = false;
                isPlayingDonk = false;
                isPlayingSb = false;
                donkRepeatTimer = 0;
                sbRepeatTimer = 0;
                FlightManagement.LOGGER.info("Skeleton died, stopped all sounds");
            }
            return;
        }

        if (donkRepeatTimer > 0) {
            donkRepeatTimer--;
            if (donkRepeatTimer == 0) {
                isPlayingDonk = false;
            }
        }
        if (sbRepeatTimer > 0) {
            sbRepeatTimer--;
            if (sbRepeatTimer == 0) {
                isPlayingSb = false;
            }
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
                if (isDancing) {
                    stopDancing();
                }
                if (isPlayingSb) {
                    isPlayingSb = false;
                    sbRepeatTimer = 0;
                }

                if (soundCooldown == 0 && !isPlayingDonk) {
                    playSoundAtSkeleton(skeleton, ModSoundEvents.donk);
                    isPlayingDonk = true;
                    donkRepeatTimer = DONK_INTERVAL;
                    soundCooldown = 40;
                }
            }
        } else {
            if (targetCache != null) {
                if (isPlayingDonk) {
                    isPlayingDonk = false;
                    donkRepeatTimer = 0;
                }

                if (!isDancing && soundCooldown == 0) {
                    startDancing(skeleton);
                }
            } else {
                if (!isDancing && soundCooldown == 0) {
                    startDancing(skeleton);
                }
            }

            if (isDancing && skeleton.isAlive() && sbRepeatTimer <= 0 && !isPlayingSb) {
                playSoundAtSkeleton(skeleton, ModSoundEvents.sb);
                isPlayingSb = true;
                sbRepeatTimer = SB_INTERVAL;
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
            playSoundAtSkeleton(skeleton, ModSoundEvents.sb);
            isPlayingSb = true;
            sbRepeatTimer = SB_INTERVAL;
            FlightManagement.LOGGER.info("Skeleton starting to dance at " + skeleton.getBlockPos());
        }
    }

    @Unique
    private void stopDancing() {
        if (isDancing) {
            isDancing = false;
            danceTimer = 0;
            isPlayingSb = false;
            sbRepeatTimer = 0;
            FlightManagement.LOGGER.info("Skeleton stopped dancing");
        }
    }

    @Unique
    private void playSoundAtSkeleton(SkeletonEntity skeleton, SoundEvent soundEvent) {
        if (!skeleton.getWorld().isClient && skeleton.isAlive()) {
            skeleton.getWorld().playSoundFromEntity(
                    null,
                    skeleton,
                    soundEvent,
                    SoundCategory.HOSTILE,
                    1.0f,
                    1.0f
            );
        }
    }

    private void onRemove(CallbackInfo ci) {
        isDancing = false;
        isDead = true;
        isPlayingDonk = false;
        isPlayingSb = false;
        donkRepeatTimer = 0;
        sbRepeatTimer = 0;
        FlightManagement.LOGGER.info("Skeleton removed, cleaning up");
    }
}