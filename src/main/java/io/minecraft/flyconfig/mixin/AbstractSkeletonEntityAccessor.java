package io.minecraft.flyconfig.mixin;

import net.minecraft.entity.ai.goal.BowAttackGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractSkeletonEntity.class)
public interface AbstractSkeletonEntityAccessor {

    @Accessor("bowAttackGoal")
    BowAttackGoal<AbstractSkeletonEntity> getBowAttackGoal();

    @Accessor("meleeAttackGoal")
    MeleeAttackGoal getMeleeAttackGoal();
}