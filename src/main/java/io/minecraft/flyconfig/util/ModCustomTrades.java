package io.minecraft.flyconfig.util;

import io.minecraft.flyconfig.item.ModItems;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;

public class ModCustomTrades {
    public static void registerModCustomTrades() {
        TradeOfferHelper.registerVillagerOffers(VillagerProfession.ARMORER, 4, factories -> {
            factories.add(new TradeOffers.SellItemFactory(ModItems.AIRCRAFT_OIL, 32, 1, 16, 16, 0.01f));
            factories.add(new TradeOffers.BuyItemFactory(ModItems.AIRCRAFT_OIL, 1, 16, 15, 14));
        });

        TradeOfferHelper.registerVillagerOffers(VillagerProfession.ARMORER, 5, factories -> {
            factories.add(new TradeOffers.SellItemFactory(ModItems.AIRCRAFT_OIL, 31, 1, 16, 16, 0.02f));
            factories.add(new TradeOffers.BuyItemFactory(ModItems.AIRCRAFT_OIL, 1, 16, 15, 13));
        });

        TradeOfferHelper.registerVillagerOffers(VillagerProfession.ARMORER, 2, factories -> {
            factories.add(new TradeOffers.SellItemFactory(ModItems.METAL_SCRAP, 3, 1, 32, 1, 0.7f));
            factories.add(new TradeOffers.BuyItemFactory(ModItems.METAL_SCRAP, 1, 30, 1, 2));
        });

        TradeOfferHelper.registerVillagerOffers(VillagerProfession.ARMORER, 3, factories -> {
            factories.add(new TradeOffers.ProcessItemFactory(ModItems.METAL_SCRAP, 11, 5, ModItems.AIRCRAFT_OIL, 1, 12, 14, 0.03f));
        });
    }
}