package io.minecraft.flyconfig.item;

import net.minecraft.component.type.FoodComponent;

public class ModFoodComponents {
    public static final FoodComponent METAL_SCRAP = new FoodComponent.Builder().nutrition(1).saturationModifier(0.1F).build();

    public static final FoodComponent ULHEALN = new FoodComponent.Builder().nutrition(20).saturationModifier(1.0F).build();

    public static final FoodComponent NODATA  = new FoodComponent.Builder().nutrition(0).saturationModifier(0.1F).alwaysEdible().build();

    private static FoodComponent.Builder createStew(int nutrition) {
        return new FoodComponent.Builder().nutrition(nutrition).saturationModifier(0.6F);
    }
}