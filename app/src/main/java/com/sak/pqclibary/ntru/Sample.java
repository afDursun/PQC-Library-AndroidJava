package com.sak.pqclibary.ntru;

import static com.sak.pqclibary.ntru.Params.NTRU_N;
import static com.sak.pqclibary.ntru.Params.NTRU_SAMPLE_IID_BYTES;
import static com.sak.pqclibary.ntru.Params.NTRU_WEIGHT;

import com.sak.pqclibary.ntru.models.TwoLongStruct;

import java.util.Arrays;

public class Sample {
    public static TwoLongStruct sample_rm(byte[] uniformbytes) {
        short[] r = new short[NTRU_N] ;
        short[] m = new short[NTRU_N] ;
        TwoLongStruct twoLongStruct;
        r = Sample_iid.sample_iid(uniformbytes);
        m = sample_fixed_type(Arrays.copyOfRange(uniformbytes,NTRU_SAMPLE_IID_BYTES,uniformbytes.length));

        return  new TwoLongStruct(r,m);
    }
    public static TwoLongStruct sample_fg(byte[] uniformbytes) {
        short[] f;
        short[] g;
        f = Sample_iid.sample_iid(uniformbytes);
        g = sample_fixed_type(Arrays.copyOfRange(uniformbytes,NTRU_SAMPLE_IID_BYTES,uniformbytes.length));
        return new TwoLongStruct(f,g);
    }

    private static short[] sample_fixed_type(byte[] u) {
        short[] r = new short[NTRU_N];
        int[] s = new int[NTRU_N-1];
        int i;
        for (i = 0; i < (NTRU_N-1)/4; i++)
        {
            s[4*i+0] =   (u[15*i+ 0] << 2) + (u[15*i+ 1] << 10) + (u[15*i+ 2] << 18) + ((int) u[15*i+ 3] << 26);
            s[4*i+1] = ((u[15*i+ 3] & 0xc0) >> 4) + (u[15*i+ 4] << 4) + (u[15*i+ 5] << 12) + (u[15*i+ 6] << 20) + ((int) u[15*i+ 7] << 28);
            s[4*i+2] = ((u[15*i+ 7] & 0xf0) >> 2) + (u[15*i+ 8] << 6) + (u[15*i+ 9] << 14) + (u[15*i+10] << 22) + ((int) u[15*i+11] << 30);
            s[4*i+3] =  (u[15*i+11] & 0xfc)       + (u[15*i+12] << 8) + (u[15*i+13] << 16) + ((int) u[15*i+14] << 24);
        }

        if((NTRU_N - 1) > ((NTRU_N - 1) / 4) * 4)
        {
            i = (NTRU_N-1)/4;
            s[4*i+0] = (u[15*i+ 0] << 2) + (u[15*i+ 1] << 10) + (u[15*i+ 2] << 18) + ((int) u[15*i+ 3] << 26);
            s[4*i+1] = ((u[15*i+ 3] & 0xc0) >> 4) + (u[15*i+ 4] << 4) + (u[15*i+ 5] << 12) + (u[15*i+ 6] << 20) + ((int) u[15*i+ 7] << 28);

        }

        for (i = 0; i<NTRU_WEIGHT/2; i++) s[i] |=  1;

        for (i = NTRU_WEIGHT/2; i<NTRU_WEIGHT; i++) s[i] |=  2;

        Arrays.sort(s);

        for(i=0; i<NTRU_N-1; i++)
            r[i] = ((short) (s[i] & 3));

        r[NTRU_N-1] = 0;

        return  r;
    }



}
