package com.kazav.gabi.emt;

import java.util.FormatFlagsConversionMismatchException;

/**
 * Created by gabik on 9/4/15.
 */
public class Deal {
    private String deal_name;
    private float deal_price;

    public Deal(String name, float price) {
        this.deal_name = name;
        this.deal_price = price;
    }

    public String get_name() { return this.deal_name; }
    public float getprice() { return this.deal_price; }

}
