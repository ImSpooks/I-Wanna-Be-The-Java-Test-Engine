package me.ImSpooks.iwbtgengine.util;

import java.nio.charset.StandardCharsets;

/**
 * Created by Nick on 20 sep. 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public class HexEncoder {
    public static String encode(String input) {
        if (input == null) return null;

        byte[] bytes = input.getBytes(StandardCharsets.UTF_8);
        char[] chars = new char[bytes.length * 2];

        for (int i = 0; i < bytes.length; i++) {
            char[] hex = byteToHex(bytes[i]);
            chars[i * 2] = hex[0];
            chars[i * 2 + 1] = hex[1];
        }

        return new String(chars);
    }

    public static String decode(String input) {
        if (input == null) return null;

        input = input.toLowerCase().replace("" + '@', "");

        if (input.length() % 2 != 0) {
            input = input.substring(0, (input.length() / 2) * 2);
        }

        char[] chars = input.toCharArray();
        byte[] bytes = new byte[chars.length / 2];

        for (int i = 0; i < chars.length; i += 2) {
            bytes[i / 2] = hexToByte(chars[i], chars[i + 1]);
        }

        return new String(bytes, StandardCharsets.UTF_8);
    }

    private static int hexToUnsignedInt(char c) {
        if (c >= '0' && c <= '9') {
            return c - 48;
        } else if (c >= 'a' && c <= 'f') {
            return c - 87;
        } else {
            throw new IllegalArgumentException("Invalid hex char: out of range");
        }
    }

    private static char unsignedIntToHex(int i) {
        if (i >= 0 && i <= 9) {
            return (char) (i + 48);
        } else if (i >= 10 && i <= 15) {
            return (char) (i + 87);
        } else {
            throw new IllegalArgumentException("Invalid hex int: out of range");
        }
    }

    private static byte hexToByte(char hex1, char hex0) {
        return (byte) (((hexToUnsignedInt(hex1) << 4) | hexToUnsignedInt(hex0)) + Byte.MIN_VALUE);
    }

    private static char[] byteToHex(byte b) {
        int unsignedByte = (int) b - Byte.MIN_VALUE;
        return new char[]{unsignedIntToHex((unsignedByte >> 4) & 0xf), unsignedIntToHex(unsignedByte & 0xf)};
    }
}
