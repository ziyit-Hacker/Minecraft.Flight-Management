package io.minecraft.flyconfig.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.consume.UseAction;
import net.minecraft.world.World;

public class NODATA extends Item {
    private static final int EFFECT_DURATION = 200;
    private static final int EFFECT_AMPLIFIER = 0;

    public NODATA(Item.Settings settings) {
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

            // 正面效果
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, EFFECT_DURATION, EFFECT_AMPLIFIER));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, EFFECT_DURATION, EFFECT_AMPLIFIER));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, EFFECT_DURATION, EFFECT_AMPLIFIER));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.INSTANT_HEALTH, EFFECT_DURATION, EFFECT_AMPLIFIER));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, EFFECT_DURATION, EFFECT_AMPLIFIER));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, EFFECT_DURATION, EFFECT_AMPLIFIER));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, EFFECT_DURATION, EFFECT_AMPLIFIER));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, EFFECT_DURATION, EFFECT_AMPLIFIER));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.WATER_BREATHING, EFFECT_DURATION, EFFECT_AMPLIFIER));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, EFFECT_DURATION, EFFECT_AMPLIFIER));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, EFFECT_DURATION, EFFECT_AMPLIFIER));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.HEALTH_BOOST, EFFECT_DURATION, EFFECT_AMPLIFIER));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, EFFECT_DURATION, EFFECT_AMPLIFIER));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SATURATION, EFFECT_DURATION, EFFECT_AMPLIFIER));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.LUCK, EFFECT_DURATION, EFFECT_AMPLIFIER));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, EFFECT_DURATION, EFFECT_AMPLIFIER));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.CONDUIT_POWER, EFFECT_DURATION, EFFECT_AMPLIFIER));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.DOLPHINS_GRACE, EFFECT_DURATION, EFFECT_AMPLIFIER));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.HERO_OF_THE_VILLAGE, EFFECT_DURATION, EFFECT_AMPLIFIER));

            // 负面效果
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, EFFECT_DURATION, EFFECT_AMPLIFIER));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, EFFECT_DURATION, EFFECT_AMPLIFIER));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.INSTANT_DAMAGE, EFFECT_DURATION, EFFECT_AMPLIFIER));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, EFFECT_DURATION, EFFECT_AMPLIFIER));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, EFFECT_DURATION, EFFECT_AMPLIFIER));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER, EFFECT_DURATION, EFFECT_AMPLIFIER));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, EFFECT_DURATION, EFFECT_AMPLIFIER));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, EFFECT_DURATION, EFFECT_AMPLIFIER));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, EFFECT_DURATION, EFFECT_AMPLIFIER));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.UNLUCK, EFFECT_DURATION, EFFECT_AMPLIFIER));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.LEVITATION, EFFECT_DURATION, EFFECT_AMPLIFIER));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.DARKNESS, EFFECT_DURATION, EFFECT_AMPLIFIER));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.WIND_CHARGED, EFFECT_DURATION, EFFECT_AMPLIFIER));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAVING, EFFECT_DURATION, EFFECT_AMPLIFIER));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.OOZING, EFFECT_DURATION, EFFECT_AMPLIFIER));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.INFESTED, EFFECT_DURATION, EFFECT_AMPLIFIER));

            // 中性效果
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, EFFECT_DURATION, EFFECT_AMPLIFIER));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.BAD_OMEN, EFFECT_DURATION, EFFECT_AMPLIFIER));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.TRIAL_OMEN, EFFECT_DURATION, EFFECT_AMPLIFIER));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.RAID_OMEN, EFFECT_DURATION, EFFECT_AMPLIFIER));
        }

        return result;
    }
}
