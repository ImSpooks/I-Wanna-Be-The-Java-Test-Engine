package me.ImSpooks.IWBTJ.utils;

import me.ImSpooks.IWBTJ.Main;

import java.util.Random;

public class SomeValues {

    private static Random random = new Random();

    public static double maxDistance(int x, int y) {
        double value = Main.getInstance().distance(0, x, 0, y);
        return value + 1; // adding 1 to make sure the hitbox distances are correct
    }

    public static int getRandom(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

    public static double getRandom(double min, double max) {
        return random.nextDouble() * (max - min) + min;
    }
}
