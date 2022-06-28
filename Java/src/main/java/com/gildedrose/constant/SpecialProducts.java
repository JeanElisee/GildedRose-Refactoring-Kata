package com.gildedrose.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SpecialProducts {
    SULFURAS("sulfuras"),
    AGED_BRIE("aged brie"),
    BACKSTAGE_PASSES("backstage passes"),
    CONJURED("conjured");

    private final String keyword;
}
