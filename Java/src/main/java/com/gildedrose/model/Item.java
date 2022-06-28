package com.gildedrose.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@ToString
public class Item {

    public String name;

    public int sellIn;

    public int quality;

   @Override
   public String toString() {
        return this.name + ", " + this.sellIn + ", " + this.quality;
    }
}
