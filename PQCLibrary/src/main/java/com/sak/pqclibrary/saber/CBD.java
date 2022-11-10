package com.sak.pqclibrary.saber;

import static com.sak.pqclibrary.saber.Params.SABER_N;
import com.sak.pqclibrary.saber.utils.Utils;

public class CBD {
    public static short[] cbd(short[] shorts, byte[] buf1) {

        short[] retValue = new short[SABER_N];
        long t;
        int d;
        int[] a = new int[4];
        int[] b = new int[4];
        int i, j;


        for (i = 0; i < SABER_N / 4; i++) {

            t = (long) load_littleendian(Utils.shiftLeft(buf1,i*4), 4);
            //Log.d("AFD-AFD",t+"");
            d = 0;
            for (j = 0; j < 4; j++)
                d += (t >> j) & 0x11111111;

            a[0] = d & 0xf;
            b[0] = (d >> 4) & 0xf;
            a[1] = (d >> 8) & 0xf;
            b[1] = (d >> 12) & 0xf;
            a[2] = (d >> 16) & 0xf;
            b[2] = (d >> 20) & 0xf;
            a[3] = (d >> 24) & 0xf;
            b[3] = (d >> 28);


            retValue[4 * i + 0] = (short) (a[0] - b[0]) ;
            retValue[4 * i + 1] = (short) (a[1] - b[1]) ;
            retValue[4 * i + 2] = (short) (a[2] - b[2]) ;
            retValue[4 * i + 3] = (short) (a[3] - b[3]) ;

        }

        return retValue;
    }

    private static long load_littleendian(byte[] x, int bytes) {
        short[] af = new short[8];
        for(int i = 0 ; i < 8 ; i++){
            if(x[i] < 0){
                af[i] = (short) (x[i] + 256);
            }
            else{
                af[i] = x[i];
            }
        }
        int i;
        long r = af[0];
        for (i = 1; i < bytes; i++){
            r |= (long) af[i] << (8 * i);
        }
        return r;
    }
}
