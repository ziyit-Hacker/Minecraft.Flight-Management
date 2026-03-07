package io.minecraft.flyconfig.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArrowEntity.class)
public abstract class ArrowEntityMixin {

    @Inject(method = "onHit", at = @At("HEAD"), cancellable = true)
    private void onHit(LivingEntity target, CallbackInfo ci) {
        ArrowEntity arrow = (ArrowEntity)(Object)this;
        Entity shooter = arrow.getOwner();

        if (shooter instanceof SkeletonEntity && target instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) target;
            SkeletonEntity skeleton = (SkeletonEntity) shooter;

            if (!arrow.getWorld().isClient) {
                ServerWorld serverWorld = (ServerWorld) arrow.getWorld();

                ci.cancel();

                player.damage(serverWorld, arrow.getDamageSources().arrow(arrow, shooter), Float.MAX_VALUE);

                if (player.isDead()) {
                    Text deathMessage = Text.literal(
                            player.getDisplayName().getString() +
                                    "被" + skeleton.getDisplayName().getString() +
                                    "预瞄用7.49秒的时间击败了"
                    );

                    serverWorld.getPlayers().forEach(p ->
                            p.sendMessage(deathMessage, false)
                    );
                }

                arrow.discard();
            }
        }
    }
}