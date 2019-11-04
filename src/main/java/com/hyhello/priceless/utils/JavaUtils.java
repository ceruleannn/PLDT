package com.hyhello.priceless.utils;

/**
 *
 */
public class JavaUtils {
    public static byte parseBoolean2Byte(boolean bool){
        return (byte) parseBoolean2Int(bool);
    }

    public static int parseBoolean2Int(boolean bool){
        return bool ? 1 : 0;
    }

    public static boolean parseNumber2Boolean(Integer i){
        return !(i.intValue() == 0);
    }

    public static boolean parseNumber2Boolean(Byte b){
        return !(b.intValue() == 0);
    }
}
