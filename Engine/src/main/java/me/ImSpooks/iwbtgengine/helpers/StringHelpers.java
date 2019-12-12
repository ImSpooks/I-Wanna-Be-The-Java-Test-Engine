package me.ImSpooks.iwbtgengine.helpers;

/**
 * Created by Nick on 14 okt. 2019.
 * Copyright © ImSpooks
 */
public class StringHelpers {

    /**
     * @param input String input
     * @return Capitalized string: e.g. if {@param input} is {@code "hElLo WoRlD"} the output is {@code "Hello World"}
     */
    public static String capitalize(String input) {
        String[] string = input.split(" ");
        StringBuilder output = new StringBuilder();

        for (String s : string) {
            output.append(s.substring(0, 1).toUpperCase()).append(s.substring(1).toLowerCase()).append(" ");
        }

        return output.substring(0, output.length() - 1);
    }

    /**
     * @param input String input
     * @return {@param input} with first letter as an uppercase
     */
    public static String firstUpper(String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    /**
     * @param input String input
     * @return {@param input} with first letter as a lowercase
     */
    public static String firstLower(String input) {
        return input.substring(0, 1).toLowerCase() + input.substring(1);
    }

    /**
     * Add any character in any position
     *
     * @param input String input
     * @param str String to add
     * @param position Position to add {@param str} in {@param input}
     * @return {@param input} with {@param str} at position {@param position}
     */
    public static String addChar(String input, String str, int position) {
        StringBuilder sb = new StringBuilder(input);
        if (position >= 0)
            sb.insert(position, str);
        else
            sb = new StringBuilder(str + input);
        return sb.toString();
    }
}
