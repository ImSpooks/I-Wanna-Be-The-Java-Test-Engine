package me.ImSpooks.iwbtgengine.helpers;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by Nick on 08 nov. 2019.
 * Copyright © ImSpooks
 */
public class NumberConversions {

    /**
     * @param value Value to be rounded
     * @param places amount of decimals
     * @return Double value with {@code places} amount of decimals
     */
    public static double round(double value, int places) {
        if (places == 0) {
            return round(value);
        }
        if (places < 0) {
            throw new IllegalArgumentException("places must be positive");
        }

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    /**
     * @param num Value to be rounded
     * @return Rounded value
     */
    public static int round(double num) {
        return floor(num + 0.5d);
    }

    /**
     * @param num Number to floor
     * @return Floored number
     */
    public static int floor(double num) {
        final int floor = (int) num;
        return floor == num ? floor : floor - (int) (Double.doubleToRawLongBits(num) >>> 63);
    }

    /**
     * @param num Number to ceil
     * @return Ceiled number
     */
    public static int ceil(double num) {
        final int floor = (int) num;
        return floor == num ? floor : floor + (int) (~Double.doubleToRawLongBits(num) >>> 63);
    }

    /**
     * @param num Number to be squared
     * @return Squared number
     */
    public static double square(double num) {
        return num * num;
    }

    /**
     * @param num Number to be clamped
     * @param min Minimum value
     * @param max Maximum value
     * @return Clamped number
     */
    public static double clamp(double num, double min, double max) {
        return num > max ? max : Math.max(num, min);
    }
    /**Clamp as byte
     * @see NumberConversions#clamp(double, double, double)
     */
    public static float clamp(float num, float min, float max) {
        return num > max ? max : Math.max(num, min);
    }
    /**Clamp as byte
     * @see NumberConversions#clamp(double, double, double)
     */
    public static int clamp(int num, int min, int max) {
        return num > max ? max : Math.max(num, min);
    }
    /**Clamp as byte
     * @see NumberConversions#clamp(double, double, double)
     */
    public static long clamp(long num, long min, long max) {
        return num > max ? max : Math.max(num, min);
    }
    /**Clamp as byte
     * @see NumberConversions#clamp(double, double, double)
     */
    public static short clamp(short num, short min, short max) {
        return (short) (num > max ? max : Math.max(num, min));
    }
    /**Clamp as byte
     * @see NumberConversions#clamp(double, double, double)
     */
    public static byte clamp(byte num, byte min, byte max) {
        return (byte) (num > max ? max : Math.max(num, min));
    }

    /**
     * @param num Number to be checked
     * @param min Minimum value
     * @param max Maximum value
     * @return {@code true} if the entered number is in range, {@code false} other wise
     */
    public static boolean isInRange(double num, double min, double max) {
        return num > min && num < max;
    }

    /**
     * @param object Possible number object
     *
     * @return Number parsed from the object
     */
    public static int toInt(Object object) {
        if (object instanceof Number) {
            return ((Number) object).intValue();
        }

        try {
            return Integer.parseInt(object.toString());
        } catch (NumberFormatException | NullPointerException ignored) { }
        return 0;
    }
    /**
     * @param object Possible number object
     *
     * @return Number parsed from the object
     */
    public static float toFloat(Object object) {
        if (object instanceof Number) {
            return ((Number) object).floatValue();
        }

        try {
            return Float.parseFloat(object.toString());
        } catch (NumberFormatException | NullPointerException ignored) { }
        return 0;
    }
    /**
     * @param object Possible number object
     *
     * @return Number parsed from the object
     */
    public static double toDouble(Object object) {
        if (object instanceof Number) {
            return ((Number) object).doubleValue();
        }

        try {
            return Double.parseDouble(object.toString());
        } catch (NumberFormatException | NullPointerException ignored) {}
        return 0;
    }
    /**
     * @param object Possible number object
     *
     * @return Number parsed from the object
     */
    public static long toLong(Object object) {
        if (object instanceof Number) {
            return ((Number) object).longValue();
        }

        try {
            return Long.parseLong(object.toString());
        } catch (NumberFormatException | NullPointerException ignored) {}
        return 0;
    }
    /**
     * @param object Possible number object
     *
     * @return Number parsed from the object
     */
    public static short toShort(Object object) {
        if (object instanceof Number) {
            return ((Number) object).shortValue();
        }

        try {
            return Short.parseShort(object.toString());
        } catch (NumberFormatException | NullPointerException ignored) {}
        return 0;
    }
    /**
     * @param object Possible number object
     *
     * @return Number parsed from the object
     */
    public static byte toByte(Object object) {
        if (object instanceof Number) {
            return ((Number) object).byteValue();
        }

        try {
            return Byte.parseByte(object.toString());
        } catch (NumberFormatException | NullPointerException ignored) {}
        return 0;
    }
}