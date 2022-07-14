package com.cinua.shadowrun5alchemiecalculator;
import java.util.Arrays;
import java.util.Random;

public class Dice{

    public static final int D6 = 6;

    public static int roll(int sides){
        return new Random().nextInt(sides)+1;
    }

    public static int[] roll(int sides, int rolls){
        int[] results = new int[rolls];
        for(int currentRoll = 0; currentRoll < rolls; currentRoll++){
            results[currentRoll] = roll(sides);
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
