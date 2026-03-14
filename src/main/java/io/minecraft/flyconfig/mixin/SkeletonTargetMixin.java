package io.minecraft.flyconfig.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractSkeletonEntity.class)
public abstract class SkeletonTargetMixin extends MobEntity {

    protected SkeletonTargetMixin(net.minecraft.entity.EntityType<? extends MobEntity> entityType, net.minecraft.world.World world) {
        super(entityType, world);
    }

    @Inject(method = "initGoals", at = @At("TAIL"))
    private void modifyTargetGoals(CallbackInfo ci) {
        TargetPredicate.EntityPredicate entityPredicate = (target, world) -> {
            if (target instanceof PlayerEntity player) {
                return !player.isSpectator();
            }
            return true;
        };

        TargetPredicate targetPredicate = TargetPredicate.createAttackable()
                .setBaseMaxDistance(10.0)
                .setPredicate(entityPredicate);

        this.targetSelector.add(2, new ActiveTargetGoal<>(
                this,
                PlayerEntity.class,
                10,
                true,
                false,
                entityPredicate
        ));
    }
}