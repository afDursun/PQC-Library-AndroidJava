package com.sak.pqclibary.saber.utils;

import java.util.Random;

public class Utils {
    public static byte[] randomBytes(int len){
        byte[] randomArray = new byte[len];
        Random random = new Random();
        random.nextBytes(randomArray);
        return randomArray;
    }
    public static byte[] shiftLeft(byte[] array, int shift) {
        byte[] reordered = new byte[array.length];

        for (int i = 0; i < array.length; i++) {
            reordered[i] = array[(shift + i) % array.length];
        }
        return reordered;
    }
    public static String hex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte aByte : bytes) {
            result.append(String.format("%02x", aByte));
        }
        return result.toString();
    }
}
