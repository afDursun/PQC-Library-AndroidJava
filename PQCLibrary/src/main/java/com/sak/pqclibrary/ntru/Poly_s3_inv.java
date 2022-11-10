package com.sak.pqclibrary.ntru;

import static com.sak.pqclibrary.ntru.Params.NTRU_N;

public class Poly_s3_inv {
    public static int both_negative_mask(int x, int y) {
        return (x & y) >> 15;
    }
    public static short[] poly_S3_inv(short[] a) {
        short[] r = new short[NTRU_N];
        short[] f = new short[NTRU_N]; short[] v = new short[NTRU_N];
        short[] g = new short[NTRU_N]; short[] w = new short[NTRU_N];
        int i,loop;
        short delta,sign,swap,t;

        for (i = 0;i < NTRU_N;++i) v[i] = 0;
        for (i = 0;i < NTRU_N;++i) w[i] = 0;
        w[0] = 1;

        for (i = 0;i < NTRU_N;++i) f[i] = 1;
        for (i = 0;i < NTRU_N-1;++i) g[NTRU_N-2-i] = Sample_iid.mod3_1((byte) ((a[i] & 3) + 2*(a[NTRU_N-1] & 3)));
        g[NTRU_N-1] = 0;

        delta = 1;

        for (loop = 0;loop < 2*(NTRU_N-1)-1;++loop) {
            for (i = NTRU_N-1;i > 0;--i) v[i] = v[i-1];
            v[0] = 0;

            sign = (short) Sample_iid.mod3((byte) (2 * g[0] * f[0]));
            swap = (short) both_negative_mask(-delta,-(short) g[0]);
            delta ^= swap & (delta ^ -delta);
            delta += 1;

            for (i = 0;i < NTRU_N;++i) {
                t = (short) (swap&(f[i]^g[i])); f[i] ^= t; g[i] ^= t;
                t = (short) (swap&(v[i]^w[i])); v[i] ^= t; w[i] ^= t;
            }

            for (i = 0;i < NTRU_N;++i) g[i] = (short) Sample_iid.mod3((byte) (g[i]+sign*f[i]));
            for (i = 0;i < NTRU_N;++i) w[i] = (short) Sample_iid.mod3((byte) (w[i]+sign*v[i]));
            for (i = 0;i < NTRU_N-1;++i) g[i] = g[i+1];
            g[NTRU_N-1] = 0;
        }

        sign = (short) f[0];
        for (i = 0;i < NTRU_N-1;++i) r[i] = (short) Sample_iid.mod3((byte) (sign*v[NTRU_N-2-i]));
        r[NTRU_N-1] = 0;
        return r;
    }
}
