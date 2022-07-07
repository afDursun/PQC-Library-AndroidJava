package com.sak.pqclibary.saber;

import static com.sak.pqclibary.saber.Params.CRYPTO_BYTES;
import static com.sak.pqclibary.saber.Params.CRYPTO_CIPHERTEXTBYTES;
import static com.sak.pqclibary.saber.Params.SABER_BYTES_CCA_DEC;
import static com.sak.pqclibary.saber.Params.SABER_HASHBYTES;
import static com.sak.pqclibary.saber.Params.SABER_INDCPA_PUBLICKEYBYTES;
import static com.sak.pqclibary.saber.Params.SABER_INDCPA_SECRETKEYBYTES;
import static com.sak.pqclibary.saber.Params.SABER_SECRETKEYBYTES;
import static com.sak.pqclibary.saber.Params.isRandom;

import com.github.aelstad.keccakj.fips202.SHA3_256;
import com.github.aelstad.keccakj.fips202.SHA3_512;
import com.sak.pqclibary.saber.models.EncapsulationModel;
import com.sak.pqclibary.saber.models.Keys;
import com.sak.pqclibary.saber.utils.Utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class Kem {
    public static Keys crypto_kem_keypair(){
        Keys keys = null;
        try {
            keys = Indcpa.indcpa_kem_keypair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        byte[] sk1 = new byte[SABER_INDCPA_SECRETKEYBYTES+SABER_INDCPA_PUBLICKEYBYTES];
        byte[] sk2 = new byte[SABER_INDCPA_SECRETKEYBYTES+SABER_INDCPA_PUBLICKEYBYTES+SABER_HASHBYTES];
        byte[] skNew = new byte[SABER_SECRETKEYBYTES];


        System.arraycopy(keys.getSk(), 0, sk1, 0, keys.getSk().length);
        System.arraycopy(keys.getPk(), 0, sk1, keys.getSk().length, keys.getPk().length);

        MessageDigest md = new SHA3_256();
        byte[] encodedHash = md.digest(keys.getPk());

        System.arraycopy(sk1, 0, sk2, 0, sk1.length);
        System.arraycopy(encodedHash, 0, sk2, sk1.length, encodedHash.length);

        byte[] randomBytes = new byte[32];
        Arrays.fill(randomBytes,(byte)1);

        System.arraycopy(sk2, 0, skNew, 0, sk2.length);
        System.arraycopy(randomBytes, 0, skNew, sk2.length, randomBytes.length);

        keys.setSk(skNew);
        return keys;
    }
    public static EncapsulationModel crypto_kem_enc(byte[] pk){
        byte[] ct = new byte[CRYPTO_CIPHERTEXTBYTES];
        byte[] k = new byte[CRYPTO_BYTES];


        byte[] kr = new byte[64];
        byte[] kr_p1 = new byte[32];
        byte[] kr_p2 = new byte[32];
        byte[] buf = new byte[64];
        byte[] randomBytes = new byte[32];


        //RandomControl
        if(isRandom){
            randomBytes = Utils.randomBytes(randomBytes.length);
        }
        else{
            Arrays.fill(randomBytes,(byte) 1);
        }



        MessageDigest md = new SHA3_256();
        byte[] encodedPK = md.digest(pk);
        randomBytes = md.digest(randomBytes);

        System.arraycopy(randomBytes, 0, buf, 0, randomBytes.length);
        System.arraycopy(encodedPK, 0, buf, randomBytes.length, encodedPK.length);

        md = new SHA3_512();
        kr = md.digest(buf);


        ct = Indcpa.indcpa_kem_enc(buf,Arrays.copyOfRange(kr,32,64),pk,0);
        md = new SHA3_256();
        kr_p2 = md.digest(ct);

        kr_p1 = Arrays.copyOfRange(kr,0,32);

        System.arraycopy(kr_p1, 0, kr, 0, kr_p1.length);
        System.arraycopy(kr_p2, 0, kr, kr_p1.length, kr_p2.length);

        k = md.digest(kr);
        return new EncapsulationModel(ct,k);
    }

    public static byte[] crypto_kem_dec(byte[] sk, byte[] cipherText) {
        byte[] cmp = new byte[SABER_BYTES_CCA_DEC];
        byte[] key_ss = new byte[CRYPTO_BYTES];
        byte[] buf = new byte[64];
        byte[] kr = new byte[64];
        byte[] krNew = new byte[64];
        byte[] buf_p1 = new byte[32];
        byte[] buf_p2 = new byte[32];
        byte[] encoded ;

        byte[] pk = Arrays.copyOfRange(sk,SABER_INDCPA_SECRETKEYBYTES,sk.length);

        buf_p1 = Indcpa.indcpa_kem_dec(sk,cipherText);

        for (int i = 0; i < 32; i++) {
            buf_p2[i] = sk[SABER_SECRETKEYBYTES - 64 + i];
        }

        System.arraycopy(buf_p1, 0, buf, 0, buf_p1.length);
        System.arraycopy(buf_p2, 0, buf, buf_p1.length, buf_p2.length);



        MessageDigest md = new SHA3_512();
        kr = md.digest(buf);



        cmp = Indcpa.indcpa_kem_enc(buf, Arrays.copyOfRange(kr,32,kr.length), pk,1);

        long fail = Verify.verify(cipherText, cmp, SABER_BYTES_CCA_DEC);

        md = new SHA3_256();
        byte[] krp1 = Arrays.copyOfRange(kr,0,32);
        byte[] krp2 = md.digest(cipherText);


        System.arraycopy(krp1, 0, krNew, 0, krp1.length);
        System.arraycopy(krp2, 0, krNew, krp1.length, krp2.length);

        //kr = Verify.cmov(Arrays.copyOfRange(sk,SABER_SECRETKEYBYTES-SABER_KEYBYTES,sk.length), SABER_KEYBYTES, fail);

        key_ss = md.digest(krNew);
        return key_ss;
    }
}
