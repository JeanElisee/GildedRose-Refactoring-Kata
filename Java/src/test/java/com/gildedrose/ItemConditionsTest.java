package com.gildedrose;

import com.gildedrose.model.Item;
import com.gildedrose.service.ItemConditions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ItemConditionsTest {
    // Testing when we have a normal product and we have to decrease the sellIn
    @Test
    void updateNormalProduct() {
        Item[] items = new Item[]{new Item("foo", 5, 10)};
        ItemConditions app = new ItemConditions();
        int i = app.UpdateProductValidity(items[0]);
        assertEquals(4, i);
    }

    // Sulfuras does not age since it is a legendary product
    @Test
    void updateNormalProductSulfuras() {
        Item[] items = new Item[]{new Item("Sulfuras", 5, 10)};
        ItemConditions app = new ItemConditions();
        int i = app.UpdateProductValidity(items[0]);
        assertEquals(items[0].sellIn, i);
    }

    // The quality does not change so the return value is 0
    @Test
    void ManageQualityTestSulfuras() {
        Item[] items = new Item[]{new Item("Sulfuras", 4, 48)};
        ItemConditions app = new ItemConditions();
        assertEquals(0, app.ManageQuality(items[0]));
    }

    // When products expired, the quality degrades as twice as fast
    @Test
    void ManageQualityTestExpired() {
        Item[] items = new Item[]{new Item("Test product", -1, 10)};
        ItemConditions app = new ItemConditions();
        assertEquals(-2, app.ManageQuality(items[0]));
    }

    // Backstage test cases
    // 1. Normal price
    @Test
    void ManageQualityTestBackStageSellInMoreThanTen() {
        Item[] items = new Item[]{new Item("backstage passes", 11, 10)};
        ItemConditions app = new ItemConditions();
        int i = app.ManageQuality(items[0]);
        assertEquals(1, i);
    }

    // 2. Quality += 2, sellIn <= 10
    @Test
    void ManageQualityTestBackStageSellInLessThanTen() {
        Item[] items = new Item[]{new Item("backstage passes", 9, 10)};
        ItemConditions app = new ItemConditions();
        int i = app.ManageQuality(items[0]);
        assertEquals(2, i);
    }

    // 3. Quality += 3, sellIn <= 5
    @Test
    void ManageQualityTestBackStageSellInLessThanFive() {
        Item[] items = new Item[]{new Item("backstage passes", 5, 10)};
        ItemConditions app = new ItemConditions();
        assertEquals(3, app.ManageQuality(items[0]));
    }


    // 4. After the concert quality -= quality, sellIn < 0
    @Test
    void ManageQualityTestBackStageSellInAfterConcert() {
        Item[] items = new Item[]{new Item("backstage passes", -1, 10)};
        ItemConditions app = new ItemConditions();
        int i = app.ManageQuality(items[0]);
        assertEquals(-1 * items[0].quality, i);
    }

    // In this test case the quality is supposed to cross 50 (the max quality) but we set it to fifty
    @Test
    void ManageQualityTestBackStageQualityMoreThanFifty() {
        Item[] items = new Item[]{new Item("backstage passes", 4, 48)};
        ItemConditions app = new ItemConditions();
        int i = app.ManageQuality(items[0]);
        assertEquals(2, i);
    }

    // AGED BRIE, actually increases in Quality the older it gets
    @Test
    void ManageQualityTestAgedBrie() {
        Item[] items = new Item[]{new Item("aged brie", 4, 48)};
        ItemConditions app = new ItemConditions();
        int i = app.ManageQuality(items[0]);
        assertEquals(1, i);
    }

    // CONJURED, items degrade in Quality twice as fast as normal items
    @Test
    void ManageQualityTestConjured() {
        Item[] items = new Item[]{new Item("conjured", 4, 48)};
        ItemConditions app = new ItemConditions();
        assertEquals(-2, app.ManageQuality(items[0]));
    }

    // To check when the function returns a negative value
    @Test
    void ManageQualityTestNegativeQuality() {
        Item[] items = new Item[]{new Item("foo", 0, 0)};
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertEquals("foo", app.items[0].name);
    }
}
