package ru.vych.common;

import java.util.concurrent.ThreadLocalRandom;

public class RandomUtils {
    public static int inRange(int start, int end) {
        return ThreadLocalRandom.current().nextInt(start, end);
    }
}
