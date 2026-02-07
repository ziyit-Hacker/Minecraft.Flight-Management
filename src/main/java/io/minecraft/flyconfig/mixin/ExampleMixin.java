package io.minecraft.flyconfig.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.SplashOverlay;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.texture.TextureManager;
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
		MinecraftClient client = MinecraftClient.getInstance();
		TextureManager textureManager = client.getTextureManager();

		ci.cancel();

		int screenWidth = client.getWindow().getScaledWidth();
		int screenHeight = client.getWindow().getScaledHeight();
		int logoWidth = 512;
		int logoHeight = 512;
		int x = (screenWidth - logoWidth) / 2;
		int y = (screenHeight - logoHeight) / 2;

		textureManager.getTexture(BUGJANG_LOGO);

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);

		bufferBuilder.vertex((float)x, (float)(y + logoHeight), 0.0F).texture(0.0F, 1.0F);
		bufferBuilder.vertex((float)(x + logoWidth), (float)(y + logoHeight), 0.0F).texture(1.0F, 1.0F);
		bufferBuilder.vertex((float)(x + logoWidth), (float)y, 0.0F).texture(1.0F, 0.0F);
		bufferBuilder.vertex((float)x, (float)y, 0.0F).texture(0.0F, 0.0F);

		bufferBuilder.end();
	}
}