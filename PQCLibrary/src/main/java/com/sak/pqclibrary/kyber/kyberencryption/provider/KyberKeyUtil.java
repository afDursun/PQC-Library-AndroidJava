package com.sak.pqclibrary.kyber.kyberencryption.provider;

public final class KyberKeyUtil {

    public static int constantTimeCompare(byte[] x, byte[] y) {
        if (x.length != y.length) {
            return 0;
        }

        byte v = 0;

        for (int i = 0; i < x.length; i++) {
            v = (byte) ((int) (v & 0xFF) | ((int) (x[i] & 0xFF) ^ (int) (y[i] & 0xFF)));
        }
        return Byte.compare(v, (byte) 0);
    }
}
