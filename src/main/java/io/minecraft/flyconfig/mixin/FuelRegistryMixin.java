package io.minecraft.flyconfig.mixin;

import net.minecraft.item.FuelRegistry;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.resource.featuretoggle.FeatureSet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import io.minecraft.flyconfig.item.ModFuels;

@Mixin(FuelRegistry.class)
public class FuelRegistryMixin {

	@Inject(
			method = "createDefault(Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;Lnet/minecraft/resource/featuretoggle/FeatureSet;I)Lnet/minecraft/item/FuelRegistry;",
			at = @At("RETURN"),
			cancellable = true,
			remap = true
	)

	private static void injectCustomFuel(
			RegistryWrapper.WrapperLookup registries,
			FeatureSet enabledFeatures,
			int itemSmeltTime,
			CallbackInfoReturnable<FuelRegistry> cir
	) {
		FuelRegistry customFuel = ModFuels.createCustomFuelRegistry(registries, enabledFeatures, itemSmeltTime);
		cir.setReturnValue(customFuel);
	}
}