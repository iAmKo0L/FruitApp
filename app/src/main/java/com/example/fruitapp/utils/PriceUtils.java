package com.example.fruitapp.utils;

import java.text.NumberFormat;
import java.util.Locale;

public class PriceUtils {
    private static final NumberFormat NUMBER_FORMAT = NumberFormat.getNumberInstance(new Locale("vi", "VN"));

    public static String format(double price) {
        return NUMBER_FORMAT.format((long) price) + " ₫";
    }
}
