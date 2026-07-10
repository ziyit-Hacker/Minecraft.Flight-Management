package io.minecraft.flyconfig.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCollisionHandler;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.List;

public class UraniumOreBlock extends Block {

    private static final int RADIUS = 5;
    private static final int DURATION = 200;

    public UraniumOreBlock(Block.Settings settings) {
        super(settings);
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        super.onBlockAdded(state, world, pos, oldState, notify);
        if (!world.isClient) {
            applyEffectsToNearbyPlayers((ServerWorld) world, pos);
            world.scheduleBlockTick(pos, this, 20);
        }
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, net.minecraft.util.math.random.Random random) {
        super.scheduledTick(state, world, pos, random);
        applyEffectsToNearbyPlayers(world, pos);
        world.scheduleBlockTick(pos, this, 20);
    }

    private void applyEffectsToNearbyPlayers(ServerWorld world, BlockPos pos) {
        Box box = new Box(pos).expand(RADIUS);
        List<PlayerEntity> players = world.getEntitiesByClass(PlayerEntity.class, box, Entity::isAlive);

        for (PlayerEntity player : players) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, DURATION, 0));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, DURATION, 0));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, DURATION, 0));
        }
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity, EntityCollisionHandler handler) {
        super.onEntityCollision(state, world, pos, entity, handler);
        if (!world.isClient && entity instanceof PlayerEntity player) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, DURATION, 0));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, DURATION, 0));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, DURATION, 0));
        }
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        super.onSteppedOn(world, pos, state, entity);
        if (!world.isClient && entity instanceof PlayerEntity player) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, DURATION, 0));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, DURATION, 0));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, DURATION, 0));
        }
    }
}