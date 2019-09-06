package me.ImSpooks.iwbtgengine.util;

/**
 * Created by Nick on 06 sep. 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public class StringUtils {

    public static String capitalize(String input) {
        String[] string = input.split(" ");
        StringBuilder output = new StringBuilder();

        for (String s : string) {
            output.append(s.substring(0, 1).toUpperCase()).append(s.substring(1).toLowerCase()).append(" ");
        }

        return output.substring(0, output.length() - 1);
    }
}
