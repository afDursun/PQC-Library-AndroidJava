package com.sak.pqclibary.ntru;

import static com.sak.pqclibary.ntru.Params.CRYPTO_PUBLICKEYBYTES;
import static com.sak.pqclibary.ntru.Params.NTRU_N;
import static com.sak.pqclibary.ntru.Params.NTRU_PACK_DEG;
import static com.sak.pqclibary.ntru.Params.NTRU_PUBLICKEYBYTES;

public class Packq {
    public static short[] poly_Rq_sum_zero_frombytes(byte[] a) {
        short[] r = new short[NTRU_N];
        int i;
        r = Poly.poly_Sq_frombytes(a);
        r[NTRU_N-1] = 0;
        for(i=0;i<NTRU_PACK_DEG;i++)
            r[NTRU_N-1] -= r[i];
        return r;
    }
    public static byte[] poly_Rq_sum_zero_tobytes(short[] h) {
        byte[] r = new byte[CRYPTO_PUBLICKEYBYTES] ;
        r = Packq.poly_Sq_tobytes(h);
        return r;
    }
    public static byte[] poly_Sq_tobytes(short[] a) {//236
        byte[] r = new byte[NTRU_PUBLICKEYBYTES];

        int i,j;
        short[] t = new short[8];

        for(i=0;i<NTRU_PACK_DEG/8;i++)
        {
            for(j=0;j<8;j++)
                t[j] = Poly.MODQ(a[8*i+j]);

            r[11 * i + 0] = (byte) ( t[0]        & 0xff);
            r[11 * i + 1] = (byte) ((t[0] >>  8) | ((t[1] & 0x1f) << 3));
            r[11 * i + 2] = (byte) ((t[1] >>  5) | ((t[2] & 0x03) << 6));
            r[11 * i + 3] = (byte) ((t[2] >>  2) & 0xff);
            r[11 * i + 4] = (byte) ((t[2] >> 10) | ((t[3] & 0x7f) << 1));
            r[11 * i + 5] = (byte) ((t[3] >>  7) | ((t[4] & 0x0f) << 4));
            r[11 * i + 6] = (byte) ((t[4] >>  4) | ((t[5] & 0x01) << 7));
            r[11 * i + 7] = (byte) ((t[5] >>  1) & 0xff);
            r[11 * i + 8] = (byte) ((t[5] >>  9) | ((t[6] & 0x3f) << 2));
            r[11 * i + 9] = (byte) ((t[6] >>  6) | ((t[7] & 0x07) << 5));
            r[11 * i + 10] = (byte) ((t[7] >>  3));
        }

        for(j=0;j<NTRU_PACK_DEG-8*i;j++)
            t[j] = Poly.MODQ(a[8*i+j]);
        for(; j<8; j++)
            t[j] = 0;

        switch(NTRU_PACK_DEG&0x07)
        {
            case 4:
                r[11 * i + 0] = (byte) (t[0]        & 0xff);
                r[11 * i + 1] = (byte) ((byte) (t[0] >>  8) | ((t[1] & 0x1f) << 3));
                r[11 * i + 2] = (byte) ((byte) (t[1] >>  5) | ((t[2] & 0x03) << 6));
                r[11 * i + 3] = (byte) ((byte) (t[2] >>  2) & 0xff);
                r[11 * i + 4] = (byte) ((byte) (t[2] >> 10) | ((t[3] & 0x7f) << 1));
                r[11 * i + 5] = (byte) ((byte) (t[3] >>  7) | ((t[4] & 0x0f) << 4));
                break;
            case 2:
                r[11 * i + 0] = (byte) (t[0]        & 0xff);
                r[11 * i + 1] = (byte) ((byte) (t[0] >>  8) | ((t[1] & 0x1f) << 3));
                r[11 * i + 2] = (byte) ((byte) (t[1] >>  5) | ((t[2] & 0x03) << 6));
                break;
        }

        return r;
    }
}
