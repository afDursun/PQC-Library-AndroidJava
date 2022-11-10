package com.sak.pqclibrary.ntru;

import static com.sak.pqclibrary.ntru.Params.NTRU_N;

public class Poly_req_mul {
    public static short[] poly_Rq_mul(short[] a, short[] b) {
        short[] r = new short[NTRU_N];

        for(int k=0; k<NTRU_N; k++)
        {
            r[k] = 0;
            for(int i=1; i<NTRU_N-k; i++)
                r[k] += a[k+i] * b[NTRU_N-i];
            for(int i =0; i<k+1; i++)
                r[k] += a[k-i] * b[i];
        }
        return r;
    }
}
