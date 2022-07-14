package com.cinua.shadowrun5alchemiecalculator;
import java.util.Arrays;
import java.util.Random;

public class Dice{
    public static final boolean FAIL = false;
    public static final boolean SUCCESS = true;

    public static final int D6 = 6;

    public static int roll(int sides){
        return new Random().nextInt(sides)+1;
    }

    public static int[] roll(int sides, int times){
        int[] results = new int[times];
        for(int i = 0; i < times; i++){
            results[i] = roll(sides);
        }
        return results;
    }

    public static boolean isSuccess(int result){
        return result >= 5;
    }

    public static boolean isFail(int result){
        return result == 1;
    }

    public static long getSuccesses(int[] rollResults){
        return Arrays.stream(rollResults).filter(Dice::isSuccess).count();
    }

    public static long getFails(int[] rollResults){
        return Arrays.stream(rollResults).filter(Dice::isFail).count();
    }
}
