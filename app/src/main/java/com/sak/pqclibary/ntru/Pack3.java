package com.sak.pqclibary.ntru;

import static com.sak.pqclibary.ntru.Params.NTRU_PACK_DEG;
import static com.sak.pqclibary.ntru.Params.NTRU_PACK_TRINARY_BYTES;

public class Pack3 {

    public static byte[] poly_S3_tobytes(short[] a) {
        byte[] msg = new byte[NTRU_PACK_TRINARY_BYTES];

        int i;
        int j;
        byte c;

        for (i = 0; i < NTRU_PACK_DEG / 5; i++) {
            c = (byte) (a[5 * i + 4] & 255);
            c = (byte) ((3 * c + a[5 * i + 3]) & 255);
            c = (byte) ((3 * c + a[5 * i + 2]) & 255);
            c = (byte) ((3 * c + a[5 * i + 1]) & 255);
            c = (byte) ((3 * c + a[5 * i + 0]) & 255);
            msg[i] = c;
        }
        if (NTRU_PACK_DEG > (NTRU_PACK_DEG / 5) * 5){
            i = NTRU_PACK_DEG / 5;
            c = 0;
            for (j = NTRU_PACK_DEG - (5 * i) - 1; j >= 0; j--)
                c = (byte) ((3 * c + a[5 * i + j]) & 255);
            msg[i] = c;
        }

        return msg;
    }
}
