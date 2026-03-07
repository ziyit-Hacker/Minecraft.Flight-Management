package io.minecraft.flyconfig.mixin;

import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    @Unique
    private boolean poemLoopMode = false;

    public boolean flyconfig$isPoemLoopMode() {
        return poemLoopMode;
    }

    public void flyconfig$setPoemLoopMode(boolean enabled) {
        this.poemLoopMode = enabled;
    }
}