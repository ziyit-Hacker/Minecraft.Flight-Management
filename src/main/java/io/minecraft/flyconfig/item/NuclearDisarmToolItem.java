package io.minecraft.flyconfig.item;

import io.minecraft.flyconfig.entity.custom.NuclearEntity;
import io.minecraft.flyconfig.entity.ModEntities;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.List;

public class NuclearDisarmToolItem extends Item {

    private static final int REQUIRED_TICKS = 300;

    public NuclearDisarmToolItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        PlayerEntity player = context.getPlayer();
        World world = context.getWorld();

        if (player == null || world.isClient) {
            return ActionResult.PASS;
        }

        Box searchBox = new Box(
                context.getBlockPos().getX() - 2,
                context.getBlockPos().getY() - 2,
                context.getBlockPos().getZ() - 2,
                context.getBlockPos().getX() + 3,
                context.getBlockPos().getY() + 3,
                context.getBlockPos().getZ() + 3
        );

        List<NuclearEntity> nuclearEntities = world.getEntitiesByClass(
                NuclearEntity.class, searchBox, entity -> entity.isAlive() && !entity.isIgnited()
        );

        if (nuclearEntities.isEmpty()) {
            player.sendMessage(Text.translatable("item.flyconfig.nuclear_disarm_tool.no_nuclear"), false);
            return ActionResult.FAIL;
        }

        NuclearEntity target = nuclearEntities.get(0);
        double distance = player.getPos().distanceTo(target.getPos());
        if (distance > 3.0) {
            player.sendMessage(Text.translatable("item.flyconfig.nuclear_disarm_tool.too_far"), false);
            return ActionResult.FAIL;
        }

        player.setCurrentHand(context.getHand());
        player.sendMessage(Text.translatable("item.flyconfig.nuclear_disarm_tool.start"), false);

        return ActionResult.SUCCESS;
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (world.isClient || !(user instanceof PlayerEntity player)) return;

        int elapsedTicks = REQUIRED_TICKS - remainingUseTicks;

        if (elapsedTicks % 20 == 0 && elapsedTicks > 0) {
            int secondsLeft = (REQUIRED_TICKS - elapsedTicks) / 20 + 1;
            player.sendMessage(Text.translatable("item.flyconfig.nuclear_disarm_tool.progress", secondsLeft), true);
        }

        if (elapsedTicks >= 5) {
            Box searchBox = new Box(
                    player.getX() - 3, player.getY() - 3, player.getZ() - 3,
                    player.getX() + 3, player.getY() + 3, player.getZ() + 3
            );

            List<NuclearEntity> nuclearEntities = world.getEntitiesByClass(
                    NuclearEntity.class, searchBox, entity -> entity.isAlive()
            );

            if (nuclearEntities.isEmpty()) {
                player.sendMessage(Text.translatable("item.flyconfig.nuclear_disarm_tool.lost_target"), false);
                player.clearActiveItem();
                return;
            }

            NuclearEntity target = nuclearEntities.get(0);

            if (target.isIgnited()) {
                player.sendMessage(Text.translatable("item.flyconfig.nuclear_disarm_tool.ignited"), false);
                player.clearActiveItem();
                return;
            }

            if (player.getPos().distanceTo(target.getPos()) > 3.5) {
                player.sendMessage(Text.translatable("item.flyconfig.nuclear_disarm_tool.too_far"), false);
                player.clearActiveItem();
                return;
            }
        }

        if (elapsedTicks >= REQUIRED_TICKS) {
            Box searchBox = new Box(
                    player.getX() - 3, player.getY() - 3, player.getZ() - 3,
                    player.getX() + 3, player.getY() + 3, player.getZ() + 3
            );

            List<NuclearEntity> nuclearEntities = world.getEntitiesByClass(
                    NuclearEntity.class, searchBox, entity -> entity.isAlive() && !entity.isIgnited()
            );

            if (nuclearEntities.isEmpty()) {
                player.sendMessage(Text.translatable("item.flyconfig.nuclear_disarm_tool.lost_target"), false);
                player.clearActiveItem();
                return;
            }

            NuclearEntity target = nuclearEntities.get(0);

            if (target.isIgnited()) {
                player.sendMessage(Text.translatable("item.flyconfig.nuclear_disarm_tool.ignited"), false);
                player.clearActiveItem();
                return;
            }

            if (player.getPos().distanceTo(target.getPos()) > 3.5) {
                player.sendMessage(Text.translatable("item.flyconfig.nuclear_disarm_tool.too_far"), false);
                player.clearActiveItem();
                return;
            }

            target.discard();

            ItemStack spawnEgg = new ItemStack(io.minecraft.flyconfig.item.ModItems.NUCLEAR_SPAWN_EGG);
            if (!player.getInventory().insertStack(spawnEgg)) {
                player.dropItem(spawnEgg, false);
            }

            player.sendMessage(Text.translatable("item.flyconfig.nuclear_disarm_tool.success"), false);
            player.clearActiveItem();
            stack.decrement(1);
        }
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return REQUIRED_TICKS;
    }

    @Override
    public boolean isUsedOnRelease(ItemStack stack) {
        return true;
    }

    @Override
    public boolean canMine(ItemStack stack, net.minecraft.block.BlockState state, World world, net.minecraft.util.math.BlockPos pos, LivingEntity user) {
        return false;
    }
}