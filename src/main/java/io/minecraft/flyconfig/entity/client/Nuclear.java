package io.minecraft.flyconfig.entity.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.util.math.MatrixStack;

@Environment(EnvType.CLIENT)
public class Nuclear extends EntityModel<EntityRenderState> {
	private final ModelPart bone3;

	public Nuclear(ModelPart root) {
		super(root);
		this.bone3 = root.getChild("bone3");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();

		ModelPartData bone3 = modelPartData.addChild("bone3",
				ModelPartBuilder.create()
						.uv(0, 0)
						.cuboid(-4.0F, -24.0F, -3.0F, 7.0F, 20.0F, 7.0F, new Dilation(1.0F)),
				ModelTransform.origin(0.0F, 24.0F, 0.0F)
		);

		bone3.addChild("bone",
				ModelPartBuilder.create()
						.uv(0, 24)
						.cuboid(-4.0F, -3.0F, -3.0F, 7.0F, 1.0F, 7.0F, Dilation.NONE)
						.uv(0, 26)
						.cuboid(-3.0F, -2.0F, -2.0F, 5.0F, 1.0F, 5.0F, Dilation.NONE)
						.uv(9, 28)
						.cuboid(-2.0F, -1.0F, -1.0F, 3.0F, 1.0F, 3.0F, Dilation.NONE),
				ModelTransform.origin(0.0F, 0.0F, 0.0F)
		);

		bone3.addChild("bone2",
				ModelPartBuilder.create()
						.uv(0, 24)
						.cuboid(-4.0F, -1.0F, -4.0F, 7.0F, 1.0F, 7.0F, Dilation.NONE)
						.uv(4, 26)
						.cuboid(-3.0F, -2.0F, -3.0F, 5.0F, 1.0F, 5.0F, Dilation.NONE)
						.uv(8, 28)
						.cuboid(-2.0F, -3.0F, -2.0F, 3.0F, 1.0F, 3.0F, Dilation.NONE)
						.uv(12, 30)
						.cuboid(-1.0F, -4.0F, -1.0F, 1.0F, 1.0F, 1.0F, Dilation.NONE),
				ModelTransform.origin(0.0F, -25.0F, 1.0F)
		);

		return TexturedModelData.of(modelData, 32, 32);
	}

	@Override
	public void setAngles(EntityRenderState state) {
	}

	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		bone3.render(matrices, vertexConsumer, light, overlay);
	}
}