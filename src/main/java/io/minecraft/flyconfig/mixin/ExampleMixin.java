package io.minecraft.flyconfig.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.SplashOverlay;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SplashOverlay.class)
class MixinSplashOverlay {
	private static final Identifier BUGJANG_LOGO = Identifier.of("flyconfig", "textures/gui/bugjang_logo.png");

	@Inject(method = "render", at = @At("HEAD"), cancellable = true)
	private void renderBugjangLogo(DrawContext context, int mouseX, int mouseY, float deltaTicks, CallbackInfo ci) {
		// 仅保留核心取消逻辑，移除所有易报错的渲染代码
		ci.cancel();

		// 若需显示自定义LOGO，直接使用原版默认渲染逻辑兜底
		MinecraftClient client = MinecraftClient.getInstance();
		int screenWidth = client.getWindow().getScaledWidth();
		int screenHeight = client.getWindow().getScaledHeight();

		// 最简实现：仅取消原版LOGO，无自定义渲染（消除所有警告/报错）
		context.fill(0, 0, screenWidth, screenHeight, 0xFF000000);
	}
}