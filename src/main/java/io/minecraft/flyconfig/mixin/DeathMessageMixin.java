package io.minecraft.flyconfig.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DamageSource.class)
public class DeathMessageMixin {

    @Inject(method = "getDeathMessage", at = @At("HEAD"), cancellable = true)
    private void modifyDeathMessage(LivingEntity entity, CallbackInfoReturnable<Text> cir) {
        DamageSource source = (DamageSource)(Object)this;

        if (source.getSource() instanceof ArrowEntity arrow &&
                arrow.getOwner() instanceof SkeletonEntity skeleton) {

            if (entity instanceof PlayerEntity) {
                Text message = Text.literal(
                        entity.getDisplayName().getString() +
                                "被" + skeleton.getDisplayName().getString() +
                                "预瞄用7.49秒的时间击败了"
                );
                cir.setReturnValue(message);
            }
        }
    }
}