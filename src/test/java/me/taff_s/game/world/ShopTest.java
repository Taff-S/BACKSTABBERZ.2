package me.taff_s.game.world;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import me.taff_s.game.items.Item;
import me.taff_s.game.items.armour.ArmourLibrary;
import me.taff_s.game.items.potions.PotionLibrary;
import me.taff_s.game.items.weapons.WeaponLibrary;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ShopTest {

    @Test
    void randomShopContainsHealingPotionStrengthPotionAndThirdItem() {
        Shop shop = Shop.createRandomShop();

        List<String> stockLines = Arrays.stream(shop.showStockDetailed().split("\\R"))
            .filter(line -> line.matches("\\[\\d+\\].*"))
            .collect(Collectors.toList());

        assertEquals(3, stockLines.size());
        assertTrue(containsItemName(stockLines.get(0), PotionLibrary.getAllHealingPotions()));
        assertTrue(containsItemName(stockLines.get(1), PotionLibrary.getAllStrengthPotions()));
        assertTrue(containsItemName(stockLines.get(2), WeaponLibrary.getAllWeapons())
            || containsItemName(stockLines.get(2), ArmourLibrary.getAllArmours()));
    }

    @Test
    void shopDisplaysInjectedStartingStockInOrder() {
        Shop shop = new Shop(Arrays.asList(
            PotionLibrary.LESSER_HEAL,
            PotionLibrary.LESSER_STRENGTH,
            WeaponLibrary.ironSword
        ));

        String stock = shop.showStockDetailed();

        assertTrue(stock.indexOf("[1] " + PotionLibrary.LESSER_HEAL.getItemName())
            < stock.indexOf("[2] " + PotionLibrary.LESSER_STRENGTH.getItemName()));
        assertTrue(stock.indexOf("[2] " + PotionLibrary.LESSER_STRENGTH.getItemName())
            < stock.indexOf("[3] " + WeaponLibrary.ironSword.getItemName()));
    }

    private boolean containsItemName(String stockLine, List<? extends Item> items) {
        return items.stream().anyMatch(item -> stockLine.contains(item.getItemName()));
    }
}
