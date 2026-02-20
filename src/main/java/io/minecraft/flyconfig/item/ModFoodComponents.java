package io.minecraft.flyconfig.item;

import net.minecraft.component.type.FoodComponent;

public class ModFoodComponents {
    public static final FoodComponent METAL_SCRAP = new FoodComponent.Builder().nutrition(1).saturationModifier(0.1F).build();

    private static FoodComponent.Builder createStew(int nutrition) {
        return new FoodComponent.Builder().nutrition(nutrition).saturationModifier(0.6F);
    }
}