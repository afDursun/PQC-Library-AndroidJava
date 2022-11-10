package com.sak.pqclibrary.ntru;

import static com.sak.pqclibrary.ntru.Params.NTRU_N;

public class Sample_iid {
    public static short[] sample_iid(byte[] uniformbytes) {
        short[] f = new short[NTRU_N];
        for(int i = 0 ; i < NTRU_N-1 ; i++){
            f[i] = mod3(uniformbytes[i]);
        }
        f[NTRU_N-1] = 0;
        return f;
    }

    public static short mod3(short a) {
        short r;
        short t, c;

        r = (short) ((a >> 8) + (a & 0xff)); // r mod 255 == a mod 255
        r = (short) ((r >> 4) + (r & 0xf)); // r' mod 15 == r mod 15
        r = (short) ((r >> 2) + (r & 0x3)); // r' mod 3 == r mod 3
        r = (short) ((r >> 2) + (r & 0x3)); // r' mod 3 == r mod 3

        t = (short) (r - 3);
        c = (short) (t >> 15);

        return (short) ((c&r) ^ (~c&t));
    }

    static byte mod3_1(byte a) /* a between 0 and 9 */
    {
        int t, c;
        a = (byte) ((a >> 2) + (a & 3)); /* between 0 and 4 */
        t = a - 3;
        c = t >> 5;
        return (byte) (t^(c&(a^t)));
    }
}
