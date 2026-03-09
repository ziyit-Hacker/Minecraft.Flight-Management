package io.minecraft.flyconfig.util;

import io.minecraft.flyconfig.item.ModItems;
import io.minecraft.flyconfig.villager.ModVillagers;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.item.Items;
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

        TradeOfferHelper.registerVillagerOffers(ModVillagers.FUHRER_LECTERN, 1, factories -> {
            factories.add(new TradeOffers.ProcessItemFactory(ModItems.GERMANY_DEVELOPED, 1, 2, ModItems.MUSIC_DISC_SIEG_HEIL, 1, 16, 3, 0.05f));
            factories.add(new TradeOffers.SellItemFactory(ModItems.GERMANY_DEVELOPED, 35, 1, 12, 4, 0.05f));
            factories.add(new TradeOffers.SellItemFactory(ModItems.METAL_SCRAP, 3, 1, 32, 1, 0.05f));
            factories.add(new TradeOffers.BuyItemFactory(ModItems.METAL_SCRAP, 1, 30, 1, 2));
            factories.add(new TradeOffers.ProcessItemFactory(ModItems.METAL_SCRAP, 11, 5, ModItems.AIRCRAFT_OIL, 1, 12, 3, 0.05f));
        });
        TradeOfferHelper.registerVillagerOffers(ModVillagers.FUHRER_LECTERN, 2, factories -> {
            factories.add(new TradeOffers.SellItemFactory(ModItems.MUSIC_DISC_SIEG_HEIL, 32, 1, 18, 5, 0.05f));
            factories.add(new TradeOffers.ProcessItemFactory(Items.GOLDEN_APPLE, 1, 9, Items.ENCHANTED_GOLDEN_APPLE, 1, 14, 5, 0.05f));
            factories.add(new TradeOffers.SellItemFactory(ModItems.AIRCRAFT_OIL, 32, 1, 16, 5, 0.01f));
            factories.add(new TradeOffers.BuyItemFactory(ModItems.AIRCRAFT_OIL, 1, 16, 5, 14));
        });
        TradeOfferHelper.registerVillagerOffers(ModVillagers.FUHRER_LECTERN, 3, factories -> {
            factories.add(new TradeOffers.ProcessItemFactory(ModItems.METAL_SCRAP, 10, 4, ModItems.AIRCRAFT_OIL, 2, 14, 6, 0.05f));
            factories.add(new TradeOffers.SellItemFactory(ModItems.AIRCRAFT_OIL, 31, 1, 16, 7, 0.05f));
            factories.add(new TradeOffers.BuyItemFactory(ModItems.AIRCRAFT_OIL, 1, 16, 6, 13));
            factories.add(new TradeOffers.SellItemFactory(ModItems.AIRCRAFT_OIL, 30, 1, 16, 6, 0.05f));
            factories.add(new TradeOffers.SellItemFactory(Items.GOLDEN_CARROT, 4, 1, 32, 7, 0.05f));
        });
        TradeOfferHelper.registerVillagerOffers(ModVillagers.FUHRER_LECTERN, 4, factories -> {
            factories.add(new TradeOffers.SellItemFactory(ModItems.FLIGHT_MANAGER, 64, 1, 12, 9, 0.05f));
            factories.add(new TradeOffers.ProcessItemFactory(ModItems.GERMANY_DEVELOPED, 1, 28, ModItems.FLIGHT_MANAGER, 1, 10, 12, 0.03f));
            factories.add(new TradeOffers.SellItemFactory(ModItems.NODATA, 55, 1, 8, 9, 0.05f));
            factories.add(new TradeOffers.ProcessItemFactory(Items.ENCHANTED_GOLDEN_APPLE, 3, 4, ModItems.ULHEALN, 1, 10, 12, 0.05f));
        });
        TradeOfferHelper.registerVillagerOffers(ModVillagers.FUHRER_LECTERN, 5, factories -> {
        });
    }
}