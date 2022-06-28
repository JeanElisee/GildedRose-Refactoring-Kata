package com.gildedrose.service.impl;

import com.gildedrose.constant.SpecialProducts;
import com.gildedrose.model.Item;
import org.springframework.stereotype.Service;
import com.gildedrose.service.ItemConditions;

@Service
public class ItemConditionsImpl implements ItemConditions {
    static final int LOWEST_QUALITY = 0;
    static final int HIGHEST_QUALITY = 50;

    @Override
    public Item ManageProduct(Item item) {
        item.setQuality(item.getQuality() + this.ManageQuality(item));
        item.setSellIn(UpdateProductValidity(item));

        return item;
    }

    /*
    params: item is the item we are dealing with
    return the valueToRemove from the quality
     */
    private int DecreaseQuality(Item item) {
        // By default we remove 1
        int valueToRemove = 1;
        if (item.sellIn < 0) {
            // If the product is expired we remove 2
            valueToRemove = 2;
        }

        // "Conjured" items degrade in Quality twice as fast as normal items
        if (item.name.toLowerCase().contains(SpecialProducts.CONJURED.getKeyword())) {
            valueToRemove *= 2;
        }
        // return 0 if the quality is equal or below zero, otherwise the value.
        return item.quality < 0 ? 0 : valueToRemove;
    }

    // TODO: Will be better to create a class of product, to deal with the conditions
    private int ManageQuality(Item item) {
        int valueToAdd = 0;
        // We have two special product
        // We need to check if the item is part of them, otherwise we return 0
        if (item.name.toLowerCase().contains(SpecialProducts.BACKSTAGE_PASSES.getKeyword())) {
            if (item.sellIn > 10) {
                // Normal day quality += 1
                valueToAdd = 1;
            } else if (item.sellIn <= 10 && item.sellIn > 5) {
                // When we have <=10 days left quality += 2
                valueToAdd = 2;
            } else if (item.sellIn >= 0 && item.sellIn <= 5) {
                // When we have <=5 days left quality += 2
                valueToAdd = 3;
            } else {
                // After the concert
                valueToAdd = -1 * item.quality;
            }
        } else if (item.name.toLowerCase().contains(SpecialProducts.SULFURAS.getKeyword())) {
            // Sulfuras product never expired
            valueToAdd = 0;
        } else if (item.name.equalsIgnoreCase(SpecialProducts.AGED_BRIE.getKeyword())) {
            // Aged brie increase by 1
            valueToAdd = 1;
        } else {
            // We decrease the quality of product here
            valueToAdd = -1 * DecreaseQuality(item);

            // check if the value will not send the quality below 0
            if (valueToAdd + item.quality < LOWEST_QUALITY) {
                // if so we remove the quality from itself
                valueToAdd = -1 * item.quality;
            }
        }


        // to never cross a quality of 50
        // Sends back sulfuras to 50 instead of 80 as mentioned in the test case
        if (valueToAdd + item.quality > HIGHEST_QUALITY) {
            // To return exactly what is needed to reach 50
            valueToAdd = valueToAdd - ((valueToAdd + item.quality) - 50);
        }

        return valueToAdd;
    }

    private int UpdateProductValidity(Item item) {
        return item.name.toLowerCase().contains(SpecialProducts.SULFURAS.getKeyword()) ? item.sellIn : item.sellIn - 1;
    }
}
