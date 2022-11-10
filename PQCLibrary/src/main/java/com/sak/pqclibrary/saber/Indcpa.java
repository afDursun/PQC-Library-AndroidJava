package com.sak.pqclibrary.saber;

import static com.sak.pqclibrary.saber.Params.CRYPTO_CIPHERTEXTBYTES;
import static com.sak.pqclibrary.saber.Params.SABER_EP;
import static com.sak.pqclibrary.saber.Params.SABER_EQ;
import static com.sak.pqclibrary.saber.Params.SABER_ET;
import static com.sak.pqclibrary.saber.Params.SABER_L;
import static com.sak.pqclibrary.saber.Params.SABER_N;
import static com.sak.pqclibrary.saber.Params.SABER_NOISE_SEEDBYTES;
import static com.sak.pqclibrary.saber.Params.SABER_POLYVECBYTES;
import static com.sak.pqclibrary.saber.Params.SABER_POLYVECCOMPRESSEDBYTES;
import static com.sak.pqclibrary.saber.Params.SABER_SEEDBYTES;
import static com.sak.pqclibrary.saber.Params.h1;
import static com.sak.pqclibrary.saber.Params.h2;
import static com.sak.pqclibrary.saber.Params.isRandom;
import com.github.aelstad.keccakj.core.KeccakSponge;
import com.github.aelstad.keccakj.fips202.Shake128;
import com.sak.pqclibrary.saber.models.Keys;
import com.sak.pqclibrary.saber.utils.Utils;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

public class Indcpa {
    /**
     * 0 => pk
     * 1 => sk
     */
    public static Keys indcpa_kem_keypair() throws NoSuchAlgorithmException {
        byte[] pk1;
        byte[] pk = new byte[SABER_POLYVECCOMPRESSEDBYTES + SABER_SEEDBYTES];
        byte[] sk = new byte[SABER_POLYVECBYTES];

        short[][][] A = new short[SABER_L][SABER_L][SABER_N];
        short[][] s = new short[SABER_L][SABER_N];
        short[][] b = new short[SABER_L][SABER_N];

        byte[] seed_A = new byte[SABER_SEEDBYTES];
        byte[] seed_s = new byte[SABER_NOISE_SEEDBYTES];
        int i, j;


        seed_A = Utils.randomBytes(seed_A.length);
        seed_s = Utils.randomBytes(seed_s.length);

        KeccakSponge xof = new Shake128();
        xof.getAbsorbStream().write(seed_A);
        xof.getSqueezeStream().read(seed_A);

        A = Poly.GenMatrix(A, seed_A);
        s = Poly.GenSecret(s, seed_s);
        b = Poly.MatrixVectorMul(A, s, b, 1);

        for (i = 0; i < SABER_L; i++) {
            for (j = 0; j < SABER_N; j++) {
                b[i][j] = (short) ((b[i][j] + h1) >> (SABER_EQ - SABER_EP));
            }
        }
        sk = PackUnpack.POLVECq2BS(sk, s);
        pk1 = PackUnpack.POLVECp2BS(pk, b);

        System.arraycopy(pk1, 0, pk, 0, pk1.length);
        System.arraycopy(seed_A, 0, pk, pk1.length, seed_A.length);

        return new Keys(pk,sk);
    }

    public static byte[] indcpa_kem_enc(byte[] m, byte[] seed_sp, byte[] pk,int a) {
        byte[] cipherText = new byte[CRYPTO_CIPHERTEXTBYTES];
        byte[] cipherText_part1 = new byte[960];
        byte[] cipherText_part2 = new byte[SABER_POLYVECCOMPRESSEDBYTES];

        short[][][] A = new short[SABER_L][SABER_L][SABER_N];
        short[][] sp= new short[SABER_L][SABER_N];
        short[][] bp= new short[SABER_L][SABER_N] ;
        short[] vp= new short[SABER_N] ;
        byte[] seed= new byte[SABER_SEEDBYTES] ;
        short[] mp= new short[SABER_N];
        short[][] b= new short[SABER_L][SABER_N];
        Arrays.fill(vp, (short) 0);

        byte[] seed_A = Arrays.copyOfRange(pk,SABER_POLYVECCOMPRESSEDBYTES,pk.length);

        A = Poly.GenMatrix(A,seed_A);
        sp = Poly.GenSecret(sp,seed_sp);
        bp = Poly.MatrixVectorMul(A, sp, bp, 0);

        for (int i = 0; i < SABER_L; i++)
        {
            for (int j = 0; j < SABER_N; j++)
            {
                bp[i][j] = (short) ((bp[i][j] + h1) >> (SABER_EQ - SABER_EP));
            }
        }

        cipherText_part1 = PackUnpack.POLVECp2BS(cipherText,bp);
        b = PackUnpack.BS2POLVECp(pk);
        vp = Poly.InnerProd(b, sp,vp);

        mp = PackUnpack.BS2POLmsg(m);

        for (int j = 0; j < SABER_N; j++)
        {
            vp[j] = (short) ((vp[j] - (mp[j] << (SABER_EP - 1)) + h1) >> (SABER_EP - SABER_ET));
        }

        cipherText_part2 = PackUnpack.POLT2BS(vp);

        System.arraycopy(cipherText_part1, 0, cipherText, 0, cipherText_part1.length);
        System.arraycopy(cipherText_part2, 0, cipherText, cipherText_part1.length, cipherText_part2.length);


        return cipherText;
    }

    public static byte[] indcpa_kem_dec(byte[] sk, byte[] cipherText) {
        short[][] s = new short[SABER_L][SABER_N];
        short[][] b = new short[SABER_L][SABER_N];
        short[] v = new short[SABER_N] ;
        short[] cm = new short[SABER_N];
        byte[] m = new byte[32];

        Arrays.fill(v, (short) 0);

        s = PackUnpack.BS2POLVECq(sk,s);
        b = PackUnpack.BS2POLVECp(cipherText);
        v = Poly.InnerProd(b,s,v);

        cm = PackUnpack.BS2POLT(Arrays.copyOfRange(cipherText,SABER_POLYVECCOMPRESSEDBYTES,cipherText.length));

        for (int i = 0; i < SABER_N; i++)
        {
            v[i] = (short) ((v[i] + h2 - (cm[i] << (SABER_EP - SABER_ET))) >> (SABER_EP - 1));
        }


        m = PackUnpack.POLmsg2BS(v);
        return m;
    }
}
