package com.hyhello.priceless.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 *
 */
public class ByteUtils {

    public static double toMB(long bytes){
        BigDecimal t = new BigDecimal("1024");
        return new BigDecimal(bytes).divide(t.multiply(t),2, RoundingMode.HALF_DOWN).doubleValue();
    }
}
