package io.minecraft.flyconfig;

import io.minecraft.flyconfig.entity.ModEntities;
import io.minecraft.flyconfig.entity.client.ModModelLayers;
import io.minecraft.flyconfig.entity.client.Nuclear;
import io.minecraft.flyconfig.entity.client.NuclearRenderer;
import io.minecraft.flyconfig.entity.custom.NuclearEntity;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class FlightManagementClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        EntityModelLayerRegistry.registerModelLayer(ModModelLayers.NUCLEAR, Nuclear::getTexturedModelData);
        EntityRendererRegistry.register(ModEntities.NUCLEAR, NuclearRenderer::new);
    }
}