package io.minecraft.flyconfig.entity.client;

import io.minecraft.flyconfig.entity.custom.NuclearEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.util.Identifier;

public class NuclearRenderer extends MobEntityRenderer<NuclearEntity, LivingEntityRenderState, Nuclear> {

    private static final Identifier TEXTURE = Identifier.of("flyconfig", "textures/entity/nuclear.png");

    public NuclearRenderer(EntityRendererFactory.Context context) {
        super(context, new Nuclear(context.getPart(ModModelLayers.NUCLEAR)), 0.5F);
    }

    @Override
    public Identifier getTexture(LivingEntityRenderState state) {
        return TEXTURE;
    }

    @Override
    public LivingEntityRenderState createRenderState() {
        return new LivingEntityRenderState();
    }
}