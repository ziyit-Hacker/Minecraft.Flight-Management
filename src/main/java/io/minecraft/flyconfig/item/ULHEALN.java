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

        if (!world.isClient() && user instanceof PlayerEntity player) {
            player.clearStatusEffects();

            player.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 200, 255));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 5100, 1));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, 5100, 3));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 5100, 5));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.INSTANT_HEALTH, 5100, 255));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 5100, 2));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 5100, 3));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 5100, 2));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 5100, 5));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.WATER_BREATHING, 5100, 5));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, 500, 255));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 5100, 10));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SATURATION, 5100, 2));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.LUCK, 5100, 2));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 1000, 1));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.CONDUIT_POWER, 5100, 1));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.DOLPHINS_GRACE, 5100, 2));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.HERO_OF_THE_VILLAGE, 5100, 1));
        }

        return result;
    }
}
