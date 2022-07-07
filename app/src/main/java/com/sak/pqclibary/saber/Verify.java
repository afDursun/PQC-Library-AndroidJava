package com.sak.pqclibary.saber;

public class Verify {

    public static long verify(byte[] a, byte[] b, int len) {
        long r;
        r = 0;

        for (int i = 0; i < len; i++)
            r |= a[i] ^ b[i];

        r = (-r) >> 63;
        return r;
    }

    public static byte[] cmov(byte[] x, int len, long b) {
        byte[] r = new byte[32];
        int i;

        b = -b;
        for (i = 0; i < len; i++)
            r[i] ^= b & (x[i] ^ r[i]);
        return  r;
    }
}
