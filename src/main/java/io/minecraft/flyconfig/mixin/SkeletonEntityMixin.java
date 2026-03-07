package io.minecraft.flyconfig.mixin;

import io.minecraft.flyconfig.FlightManagement;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
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
    private int donkRepeatTimer = 0;
    @Unique
    private int sbRepeatTimer = 0;
    @Unique
    private boolean isDead = false;
    @Unique
    private static final int REPEAT_INTERVAL = 40;

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        SkeletonEntity skeleton = (SkeletonEntity)(Object)this;

        if (!skeleton.isAlive()) {
            if (!isDead) {
                isDead = true;
                isDancing = false;
                FlightManagement.LOGGER.info("Skeleton died");
            }
            return;
        }

        LivingEntity target = skeleton.getTarget();

        if (donkRepeatTimer > 0) {
            donkRepeatTimer--;
        }
        if (sbRepeatTimer > 0) {
            sbRepeatTimer--;
        }

        if (target instanceof PlayerEntity player) {
            if (player.isSpectator()) {
                skeleton.setTarget(null);
                target = null;
            }
        }

        if (target instanceof PlayerEntity) {
            if (!isDancing) {
                isDancing = true;
                danceTimer = 0;
                FlightManagement.LOGGER.info("Skeleton found player");
            }

            if (donkRepeatTimer <= 0 && skeleton.isAlive()) {
                skeleton.playSound(
                        SoundEvent.of(Identifier.of("flyconfig", "entity.skeleton.donk")),
                        1.0F,
                        1.0F
                );
                donkRepeatTimer = REPEAT_INTERVAL;
            }
        } else {
            if (isDancing) {
                isDancing = false;
                danceTimer = 0;
                FlightManagement.LOGGER.info("Skeleton lost target");
            }

            if (sbRepeatTimer <= 0 && skeleton.isAlive()) {
                skeleton.playSound(
                        SoundEvent.of(Identifier.of("flyconfig", "entity.skeleton.sb")),
                        1.0F,
                        1.0F
                );
                sbRepeatTimer = REPEAT_INTERVAL;
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
}