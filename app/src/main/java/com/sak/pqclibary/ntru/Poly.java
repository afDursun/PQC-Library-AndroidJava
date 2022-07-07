package com.sak.pqclibary.ntru;

import static com.sak.pqclibary.ntru.Params.NTRU_N;
import static com.sak.pqclibary.ntru.Params.NTRU_PACK_DEG;
import static com.sak.pqclibary.ntru.Params.NTRU_Q;

public class Poly {
    public static short[] poly_Z3_to_Zq(short[] f) {
        short[] r = new short[NTRU_N];
        int i;
        for(i=0; i<NTRU_N; i++)
            r[i] = (short) (f[i] | ((-(f[i]>>1)) & (NTRU_Q-1)));
        return r;
    }

    public static short[] poly_lift(short[] a) {
        short[] retvalue = new short[NTRU_N];
        int i;
        for(i=0; i<NTRU_N; i++) {
            retvalue[i] = a[i];
        }
        retvalue = Poly.poly_Z3_to_Zq(retvalue);
        return retvalue;
    }
    public static short[] poly_Sq_frombytes(byte[] d) {
        short[] r = new short[NTRU_N];
        short[] a = new short[d.length];

        for (int i = 0 ; i < d.length ; i++){
            if(d[i] < 0 )
                a[i] = (short) (256 + d[i]);
            else
                a[i] = d[i];
        }

        int i;
        for(i=0;i<NTRU_PACK_DEG/8;i++)
        {
            r[8*i+0] = (short) ((a[11*i+ 0] >> 0) | (((short)a[11*i+ 1] & 0x07) << 8));
            r[8*i+1] = (short) ((a[11*i+ 1] >> 3) | (((short)a[11*i+ 2] & 0x3f) << 5));
            r[8*i+2] = (short) ((a[11*i+ 2] >> 6) | (((short)a[11*i+ 3] & 0xff) << 2) | (((short)a[11*i+ 4] & 0x01) << 10));
            r[8*i+3] = (short) ((a[11*i+ 4] >> 1) | (((short)a[11*i+ 5] & 0x0f) << 7));
            r[8*i+4] = (short) ((a[11*i+ 5] >> 4) | (((short)a[11*i+ 6] & 0x7f) << 4));
            r[8*i+5] = (short) ((a[11*i+ 6] >> 7) | (((short)a[11*i+ 7] & 0xff) << 1) | (((short)a[11*i+ 8] & 0x03) <<  9));
            r[8*i+6] = (short) ((a[11*i+ 8] >> 2) | (((short)a[11*i+ 9] & 0x1f) << 6));
            r[8*i+7] = (short) ((a[11*i+ 9] >> 5) | (((short)a[11*i+10] & 0xff) << 3));
        }
        switch(NTRU_PACK_DEG&0x07)
        {
            case 4:
                r[8*i+0] = (short) ((a[11*i+ 0] >> 0) | (((short)a[11*i+ 1] & 0x07) << 8));
                r[8*i+1] = (short) ((a[11*i+ 1] >> 3) | (((short)a[11*i+ 2] & 0x3f) << 5));
                r[8*i+2] = (short) ((a[11*i+ 2] >> 6) | (((short)a[11*i+ 3] & 0xff) << 2) | (((short)a[11*i+ 4] & 0x01) << 10));
                r[8*i+3] = (short) ((a[11*i+ 4] >> 1) | (((short)a[11*i+ 5] & 0x0f) << 7));
                break;
            case 2:
                r[8*i+0] = (short) ((a[11*i+ 0] >> 0) | (((short)a[11*i+ 1] & 0x07) << 8));
                r[8*i+1] = (short) ((a[11*i+ 1] >> 3) | (((short)a[11*i+ 2] & 0x3f) << 5));
                break;
        }
        r[NTRU_N-1] = 0;
        return r;
    }



    public static short MODQ(short X) {
        return (short) ((X) & (NTRU_Q-1));
    }


    public static short[] poly_Rq_inv(short[] a) {
        short[] r = new short[NTRU_N];

        short[] ai2 = new short[NTRU_N];
        ai2 = poly_R2_inv(a);
        r = poly_R2_inv_to_Rq_inv(ai2, a);

        return r;
    }

    public static short[] poly_Sq_mul(short[] a, short[] b) {
        short[] r = new short[NTRU_N];

        r = Poly_req_mul.poly_Rq_mul(a, b);
        r = poly_mod_q_Phi_n(r);

        return r;
    }

    public static short[] poly_mod_q_Phi_n(short[] r) {
        short[] r1 = new short[NTRU_N];

        int i;
        for(i=0; i<NTRU_N; i++)
            r1[i] = (short) (r[i] - r[NTRU_N-1]);

        return r1;

    }
    public static short[] poly_R2_inv(short[] a) {
        short[] r = new short[NTRU_N];

        short[] f = new short[NTRU_N];
        short[] g = new short[NTRU_N];
        short[] v = new short[NTRU_N];
        short[] w = new short[NTRU_N];
        int i, loop;
        short delta,sign,swap,t;

        for (i = 0;i < NTRU_N;++i) v[i] = 0;
        for (i = 0;i < NTRU_N;++i) w[i] = 0;
        w[0] = 1;

        for (i = 0;i < NTRU_N;++i) f[i] = 1;
        for (i = 0;i < NTRU_N-1;++i) g[NTRU_N-2-i] = (short) ((a[i] ^ a[NTRU_N-1]) & 1);
        g[NTRU_N-1] = 0;

        delta = 1;

        for (loop = 0;loop < 2*(NTRU_N-1)-1;++loop) {
            for (i = NTRU_N-1;i > 0;--i) v[i] = v[i-1];
            v[0] = 0;

            sign = (short) (g[0] & f[0]);
            swap = (short) Poly_s3_inv.both_negative_mask(-delta,-(short) g[0]);
            delta ^= swap & (delta ^ -delta);
            delta += 1;

            for (i = 0;i < NTRU_N;++i) {
                t = (short) (swap&(f[i]^g[i])); f[i] ^= t; g[i] ^= t;
                t = (short) (swap&(v[i]^w[i])); v[i] ^= t; w[i] ^= t;
            }

            for (i = 0;i < NTRU_N;++i) g[i] = (short) (g[i]^(sign&f[i]));
            for (i = 0;i < NTRU_N;++i) w[i] = (short) (w[i]^(sign&v[i]));
            for (i = 0;i < NTRU_N-1;++i) g[i] = g[i+1];
            g[NTRU_N-1] = 0;
        }

        for (i = 0;i < NTRU_N-1;++i) r[i] = v[NTRU_N-2-i];
        r[NTRU_N-1] = 0;


        return r;
    }

    public static short[] poly_R2_inv_to_Rq_inv(short[] ai, short[] a) {
        short[] r = new short[NTRU_N];
        short[] b = new short[NTRU_N];
        short[] c = new short[NTRU_N];
        short[] s = new short[NTRU_N];
        int i;


        for(i=0; i<NTRU_N; i++)
            b[i] = (short) -(a[i]);

        for(i=0; i<NTRU_N; i++)
            r[i] = ai[i];

        c = Poly_req_mul.poly_Rq_mul(r, b);
        c[0] += 2; // c = 2 - a*ai
        s = Poly_req_mul.poly_Rq_mul(c, r); // s = ai*c

        c = Poly_req_mul.poly_Rq_mul(s, b);
        c[0] += 2; // c = 2 - a*s
        r = Poly_req_mul.poly_Rq_mul(c, s); // r = s*c

        c = Poly_req_mul.poly_Rq_mul(r, b);
        c[0] += 2; // c = 2 - a*r
        s = Poly_req_mul.poly_Rq_mul(c, r); // s = r*c

        c = Poly_req_mul.poly_Rq_mul(s, b);
        c[0] += 2; // c = 2 - a*s
        r = Poly_req_mul.poly_Rq_mul(c, s); // r = s*c
        return r;

    }
}
