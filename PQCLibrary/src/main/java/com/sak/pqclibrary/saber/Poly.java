package com.sak.pqclibrary.saber;

import static com.sak.pqclibrary.saber.Params.SABER_L;
import static com.sak.pqclibrary.saber.Params.SABER_N;
import static com.sak.pqclibrary.saber.Params.SABER_POLYCOINBYTES;
import static com.sak.pqclibrary.saber.Params.SABER_POLYVECBYTES;

import com.github.aelstad.keccakj.core.KeccakSponge;
import com.github.aelstad.keccakj.fips202.Shake128;

import java.util.Arrays;


public class Poly {
    public static short[][][] GenMatrix(short[][][] A, byte[] seed_a) {
        short[][][] retValue = new short[SABER_L][SABER_L][SABER_N];
        byte[] buf = new byte[SABER_L * SABER_POLYVECBYTES];
        int i;

        KeccakSponge xof = new Shake128();
        xof.getAbsorbStream().write(seed_a);
        xof.getSqueezeStream().read(buf);

        for (i = 0; i < SABER_L; i++) {

            retValue[i] = PackUnpack.BS2POLVECq(Arrays.copyOfRange(buf,i * SABER_POLYVECBYTES,buf.length), A[i]);

        }
        return retValue;
    }

    public static short[][] GenSecret(short[][] s, byte[] seed_s) {
        short[][] retValue = new short[SABER_L][SABER_N];
        byte[] buf = new byte[SABER_L * SABER_POLYCOINBYTES];

        KeccakSponge xof = new Shake128();
        xof.getAbsorbStream().write(seed_s);
        xof.getSqueezeStream().read(buf);

        for (int i = 0; i < SABER_L; i++) {

            retValue[i] = CBD.cbd(s[i], Arrays.copyOfRange(buf,i * SABER_POLYCOINBYTES,buf.length));
        }

        return retValue;
    }

    public static short[][] MatrixVectorMul(short[][][] a, short[][] s, short[][] res, int transpose) {
        short[][] retValue = new short[SABER_L][SABER_N];
        for (int i = 0; i < SABER_L; i++) {
            for (int j = 0; j < SABER_L; j++) {
                if (transpose == 1) {
                    retValue[i] = PolyMul.poly_mul_acc(a[j][i], s[j], res[i]);
                } else {
                    retValue[i] = PolyMul.poly_mul_acc(a[i][j], s[j], res[i]);
                }
            }
        }
        return retValue;
    }


    public static short[] InnerProd(short[][] b, short[][] s, short[] res) {
        short[] vp= new short[SABER_N] ;
        int j;
        for (j = 0; j < SABER_L; j++)
        {
            vp = PolyMul.poly_mul_acc(b[j], s[j], res);
        }
        return vp;
    }
}
