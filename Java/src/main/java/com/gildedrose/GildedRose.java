package com.gildedrose;

import com.gildedrose.model.Item;
import com.gildedrose.service.ItemConditions;

class GildedRose {
    Item[] items;

    public GildedRose(Item[] items) {
        this.items = items;
    }

    public void updateQuality() {
        ItemConditions itemConditions = new ItemConditions();

        for (Item item : items) {
            item = itemConditions.ManageProduct(item);
        }
    }
}
