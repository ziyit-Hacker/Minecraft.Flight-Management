package io.minecraft.flyconfig.item;

import io.minecraft.flyconfig.FlightManagement;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.consume.UseAction;
import net.minecraft.world.World;

public class ULHEALN extends Item {
    private static final int NAUSEA_AMPLIFIER = 255;
    private static final int NAUSEA_DURATION = 200;

    public ULHEALN(Settings settings) {
        super(settings);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.EAT;
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 32;
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        ItemStack result = super.finishUsing(stack, world, user);

        FlightManagement.LOGGER.info("ULHEALN finishUsing called! User: " + user.getName().getString());

        if (!world.isClient() && user instanceof PlayerEntity player) {
            FlightManagement.LOGGER.info("Server side: Processing ULHEALN effects for " + player.getName().getString());

            player.clearStatusEffects();
            FlightManagement.LOGGER.info("Cleared all status effects for " + player.getName().getString());

            player.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, NAUSEA_DURATION, NAUSEA_AMPLIFIER));
            FlightManagement.LOGGER.info("Applied NAUSEA effect (amplifier: " + NAUSEA_AMPLIFIER + ", duration: " + NAUSEA_DURATION + " ticks) to " + player.getName().getString());
        }

        return result;
    }
}