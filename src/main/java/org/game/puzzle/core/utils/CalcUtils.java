package org.game.puzzle.core.utils;

public class CalcUtils {
    public static int percent(int min, int max, int current) {
        double result = (current / ((double)(max - min) / 100));
        return (int)result;
    }

    public static long percent(long min, long max, long current) {
        double result = (current / ((double)(max - min) / 100));
        return (long)result;
    }
}
