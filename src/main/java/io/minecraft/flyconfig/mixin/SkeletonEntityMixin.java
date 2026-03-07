package io.minecraft.flyconfig.mixin;

import io.minecraft.flyconfig.FlightManagement;
import net.minecraft.entity.LivingEntity;
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
    @Unique
    private int soundCooldown = 0;
    @Unique
    private boolean isDead = false;
    @Unique
    private boolean hasPlayedDonkForCurrentTarget = false;
    @Unique
    private boolean hasPlayedSbForCurrentState = false;

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
                if (soundCooldown == 0 && !hasPlayedDonkForCurrentTarget) {
                    FlightManagement.LOGGER.info("Skeleton found player! Playing donk");
                    skeleton.playSound(
                            SoundEvent.of(Identifier.of("flyconfig", "entity.skeleton.donk")),
                            1.0F,
                            1.0F
                    );
                    soundCooldown = 40; // 2秒冷却
                    hasPlayedDonkForCurrentTarget = true;
                    hasPlayedSbForCurrentState = false;
                }

                if (isDancing) {
                    stopDancing();
                }
            }
        } else {
            if (!isDancing && !hasPlayedSbForCurrentState && soundCooldown == 0) {
                startDancing(skeleton);
                hasPlayedSbForCurrentState = true;
                hasPlayedDonkForCurrentTarget = false;
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
            FlightManagement.LOGGER.info("Skeleton starting to dance");
            skeleton.playSound(
                    SoundEvent.of(Identifier.of("flyconfig", "entity.skeleton.sb")),
                    1.0F,
                    1.0F
            );
            soundCooldown = 600;
        }
    }

    @Unique
    private void stopDancing() {
        if (isDancing) {
            isDancing = false;
            danceTimer = 0;
        }
    }
}