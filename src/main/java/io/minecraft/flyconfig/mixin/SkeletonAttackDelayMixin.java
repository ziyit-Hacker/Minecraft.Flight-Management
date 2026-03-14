package io.minecraft.flyconfig.mixin;

import net.minecraft.entity.ai.goal.BowAttackGoal;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.HostileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractSkeletonEntity.class)
public class SkeletonAttackDelayMixin {
    @Inject(method = "getRegularAttackInterval", at = @At("HEAD"), cancellable = true)
    private void modifyRegularAttackInterval(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(149);
    }

    @Inject(method = "getHardAttackInterval", at = @At("HEAD"), cancellable = true)
    private void modifyHardAttackInterval(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(149);
    }

    @Inject(method = "updateAttackType", at = @At("TAIL"))
    private void onUpdateAttackType(CallbackInfo ci) {
        AbstractSkeletonEntity skeleton = (AbstractSkeletonEntity)(Object)this;

        if (!skeleton.getWorld().isClient) {
            AbstractSkeletonEntityAccessor accessor = (AbstractSkeletonEntityAccessor) skeleton;
            BowAttackGoal<AbstractSkeletonEntity> bowGoal = accessor.getBowAttackGoal();

            if (bowGoal != null) {
                bowGoal.setAttackInterval(149);
            }
        }
    }
}