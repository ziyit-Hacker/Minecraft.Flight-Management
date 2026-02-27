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
import java.util.Random;

public class MetalScrapItem extends Item {
    private static final float CHANCE = 0.25F;
    private static final int MIN_AMPLIFIER = 0;
    private static final int MAX_AMPLIFIER = 127;
    private static final float INFINITE_DURATION_CHANCE = 0.25F;
    private static final int MIN_RANDOM_DURATION = 100;
    private static final int MAX_RANDOM_DURATION = 6000;
    private static final Random RANDOM = new Random();

    public MetalScrapItem(Settings settings) {
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

        FlightManagement.LOGGER.info("MetalScrap finishUsing called! User: " + user.getName().getString());

        if (!world.isClient() && user instanceof PlayerEntity player) {
            FlightManagement.LOGGER.info("Server side: Trying to apply all negative/neutral effects to " + player.getName().getString());

            if (player.getRandom().nextFloat() <= CHANCE) {
                addAllNegativeAndNeutralEffects(player);
                FlightManagement.LOGGER.info("Applied ALL negative/neutral effects to " + player.getName().getString() + " (random levels 0~255)");
            } else {
                FlightManagement.LOGGER.info("Luckily, no effects for " + player.getName().getString());
            }
        }

        return result;
    }

    private void addAllNegativeAndNeutralEffects(LivingEntity entity) {
        int slownessDuration = RANDOM.nextFloat() <= INFINITE_DURATION_CHANCE ? Integer.MAX_VALUE : (MIN_RANDOM_DURATION + RANDOM.nextInt(MAX_RANDOM_DURATION - MIN_RANDOM_DURATION + 1));
        entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, slownessDuration, getRandomAmplifier()));

        int miningFatigueDuration = RANDOM.nextFloat() <= INFINITE_DURATION_CHANCE ? Integer.MAX_VALUE : (MIN_RANDOM_DURATION + RANDOM.nextInt(MAX_RANDOM_DURATION - MIN_RANDOM_DURATION + 1));
        entity.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, miningFatigueDuration, getRandomAmplifier()));

        int nauseaDuration = RANDOM.nextFloat() <= INFINITE_DURATION_CHANCE ? Integer.MAX_VALUE : (MIN_RANDOM_DURATION + RANDOM.nextInt(MAX_RANDOM_DURATION - MIN_RANDOM_DURATION + 1));
        entity.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, nauseaDuration, getRandomAmplifier()));

        int blindnessDuration = RANDOM.nextFloat() <= INFINITE_DURATION_CHANCE ? Integer.MAX_VALUE : (MIN_RANDOM_DURATION + RANDOM.nextInt(MAX_RANDOM_DURATION - MIN_RANDOM_DURATION + 1));
        entity.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, blindnessDuration, getRandomAmplifier()));

        int hungerDuration = RANDOM.nextFloat() <= INFINITE_DURATION_CHANCE ? Integer.MAX_VALUE : (MIN_RANDOM_DURATION + RANDOM.nextInt(MAX_RANDOM_DURATION - MIN_RANDOM_DURATION + 1));
        entity.addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER, hungerDuration, getRandomAmplifier()));

        int weaknessDuration = RANDOM.nextFloat() <= INFINITE_DURATION_CHANCE ? Integer.MAX_VALUE : (MIN_RANDOM_DURATION + RANDOM.nextInt(MAX_RANDOM_DURATION - MIN_RANDOM_DURATION + 1));
        entity.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, weaknessDuration, getRandomAmplifier()));

        int poisonDuration = RANDOM.nextFloat() <= INFINITE_DURATION_CHANCE ? Integer.MAX_VALUE : (MIN_RANDOM_DURATION + RANDOM.nextInt(MAX_RANDOM_DURATION - MIN_RANDOM_DURATION + 1));
        entity.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, poisonDuration, getRandomAmplifier()));

        int levitationDuration = RANDOM.nextFloat() <= INFINITE_DURATION_CHANCE ? Integer.MAX_VALUE : (MIN_RANDOM_DURATION + RANDOM.nextInt(MAX_RANDOM_DURATION - MIN_RANDOM_DURATION + 1));
        entity.addStatusEffect(new StatusEffectInstance(StatusEffects.LEVITATION, levitationDuration, getRandomAmplifier()));

        int unluckDuration = RANDOM.nextFloat() <= INFINITE_DURATION_CHANCE ? Integer.MAX_VALUE : (MIN_RANDOM_DURATION + RANDOM.nextInt(MAX_RANDOM_DURATION - MIN_RANDOM_DURATION + 1));
        entity.addStatusEffect(new StatusEffectInstance(StatusEffects.UNLUCK, unluckDuration, getRandomAmplifier()));

        int darknessDuration = RANDOM.nextFloat() <= INFINITE_DURATION_CHANCE ? Integer.MAX_VALUE : (MIN_RANDOM_DURATION + RANDOM.nextInt(MAX_RANDOM_DURATION - MIN_RANDOM_DURATION + 1));
        entity.addStatusEffect(new StatusEffectInstance(StatusEffects.DARKNESS, darknessDuration, getRandomAmplifier()));

        int windChargedDuration = RANDOM.nextFloat() <= INFINITE_DURATION_CHANCE ? Integer.MAX_VALUE : (MIN_RANDOM_DURATION + RANDOM.nextInt(MAX_RANDOM_DURATION - MIN_RANDOM_DURATION + 1));
        entity.addStatusEffect(new StatusEffectInstance(StatusEffects.WIND_CHARGED, windChargedDuration, getRandomAmplifier()));

        int weavingDuration = RANDOM.nextFloat() <= INFINITE_DURATION_CHANCE ? Integer.MAX_VALUE : (MIN_RANDOM_DURATION + RANDOM.nextInt(MAX_RANDOM_DURATION - MIN_RANDOM_DURATION + 1));
        entity.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAVING, weavingDuration, getRandomAmplifier()));

        int oozingDuration = RANDOM.nextFloat() <= INFINITE_DURATION_CHANCE ? Integer.MAX_VALUE : (MIN_RANDOM_DURATION + RANDOM.nextInt(MAX_RANDOM_DURATION - MIN_RANDOM_DURATION + 1));
        entity.addStatusEffect(new StatusEffectInstance(StatusEffects.OOZING, oozingDuration, getRandomAmplifier()));

        int infestedDuration = RANDOM.nextFloat() <= INFINITE_DURATION_CHANCE ? Integer.MAX_VALUE : (MIN_RANDOM_DURATION + RANDOM.nextInt(MAX_RANDOM_DURATION - MIN_RANDOM_DURATION + 1));
        entity.addStatusEffect(new StatusEffectInstance(StatusEffects.INFESTED, infestedDuration, getRandomAmplifier()));

        int glowingDuration = RANDOM.nextFloat() <= INFINITE_DURATION_CHANCE ? Integer.MAX_VALUE : (MIN_RANDOM_DURATION + RANDOM.nextInt(MAX_RANDOM_DURATION - MIN_RANDOM_DURATION + 1));
        entity.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, glowingDuration, getRandomAmplifier()));

        int badOmenDuration = RANDOM.nextFloat() <= INFINITE_DURATION_CHANCE ? Integer.MAX_VALUE : (MIN_RANDOM_DURATION + RANDOM.nextInt(MAX_RANDOM_DURATION - MIN_RANDOM_DURATION + 1));
        entity.addStatusEffect(new StatusEffectInstance(StatusEffects.BAD_OMEN, badOmenDuration, getRandomAmplifier()));

        int trialOmenDuration = RANDOM.nextFloat() <= INFINITE_DURATION_CHANCE ? Integer.MAX_VALUE : (MIN_RANDOM_DURATION + RANDOM.nextInt(MAX_RANDOM_DURATION - MIN_RANDOM_DURATION + 1));
        entity.addStatusEffect(new StatusEffectInstance(StatusEffects.TRIAL_OMEN, trialOmenDuration, getRandomAmplifier()));

        int raidOmenDuration = RANDOM.nextFloat() <= INFINITE_DURATION_CHANCE ? Integer.MAX_VALUE : (MIN_RANDOM_DURATION + RANDOM.nextInt(MAX_RANDOM_DURATION - MIN_RANDOM_DURATION + 1));
        entity.addStatusEffect(new StatusEffectInstance(StatusEffects.RAID_OMEN, raidOmenDuration, getRandomAmplifier()));

        FlightManagement.LOGGER.info("Total effects applied: 20 (16 negative + 4 neutral) to " + entity.getName().getString());
    }

    private int getRandomAmplifier() {
        return MIN_AMPLIFIER + RANDOM.nextInt(MAX_AMPLIFIER - MIN_AMPLIFIER + 1);
    }
}
