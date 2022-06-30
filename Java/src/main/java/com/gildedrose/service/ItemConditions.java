package com.gildedrose.service;

import com.gildedrose.constant.SpecialProducts;
import com.gildedrose.model.Item;

public class ItemConditions {
    /*
     Constants to define the lowest and highest quality value of an item,
     The value is set separately so that they can be easily identified and modified.
    */
    static final int LOWEST_QUALITY = 0;
    static final int HIGHEST_QUALITY = 50;

    public Item ManageProduct(Item item) {
        item.sellIn = UpdateProductValidity(item);
        item.quality = item.quality + this.ManageQuality(item);
        return item;
    }

    /*
    params: item is the item we are dealing with
    return the valueToRemove from the quality
     */
    public int DecreaseQuality(Item item) {
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

    /*
    This function serves to decrease and increase the quality of the products based on predefined conditions
     */
    public int ManageQuality(Item item) {
        int valueToAdd;
        // Cases for the special products
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

            // Checks if the value will not send the quality below 0
            if (valueToAdd + item.quality < LOWEST_QUALITY) {
                // if so we remove the quality from itself
                valueToAdd = -1 * item.quality;
            }
        }

        // to never cross a quality of 50
        if (valueToAdd + item.quality > HIGHEST_QUALITY && !item.name.toLowerCase().contains(SpecialProducts.SULFURAS.getKeyword())) {
            // To return exactly what is needed to reach 50
            valueToAdd = valueToAdd - ((valueToAdd + item.quality) - 50);
        }
        // Can be either negative if the conditions suggest to decrease the quality or not
        return valueToAdd;
    }

    /*
    Updates the sellIn, taking care of the fact that Sulfuras does not age
     */
    public int UpdateProductValidity(Item item) {
        // Updates all the sellIn except the one for Sulfuras products
        return item.name.toLowerCase().contains(SpecialProducts.SULFURAS.getKeyword()) ? item.sellIn : item.sellIn - 1;
    }
}
