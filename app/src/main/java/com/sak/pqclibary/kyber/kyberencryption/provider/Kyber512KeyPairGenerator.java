package com.sak.pqclibary.kyber.kyberencryption.provider;

import android.os.Build;
import androidx.annotation.RequiresApi;
import com.github.aelstad.keccakj.fips202.SHA3_256;
import com.sak.pqclibary.kyber.kyberencryption.provider.kyber.Indcpa;
import com.sak.pqclibary.kyber.kyberencryption.provider.kyber.KyberParams;
import java.security.*;
import java.util.ArrayList;

public final class Kyber512KeyPairGenerator {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static KyberKeyPair generateKeys512() {
        int paramsK = 2;
        KyberKeyPair keyPair = null;
        KyberKeySize kyberKeySize = KyberKeySize.KEY_512;
        SecureRandom random = new SecureRandom();
        try{
            random = SecureRandom.getInstanceStrong();
        }
        catch (Exception e){
        }
        try {
            ArrayList<byte[]> packedPKI = new ArrayList<>(Indcpa.generateKyberKeys(paramsK));
            byte[] packedPublicKey = packedPKI.get(1);
            byte[] packedPrivateKey = packedPKI.get(0);

            byte[] privateKeyFixedLength = new byte[KyberParams.Kyber512SKBytes];
            MessageDigest md = new SHA3_256();
            byte[] encodedHash = md.digest(packedPublicKey);
            byte[] pkh = new byte[encodedHash.length];
            System.arraycopy(encodedHash, 0, pkh, 0, encodedHash.length);
            byte[] rnd = new byte[KyberParams.paramsSymBytes];
            random.nextBytes(rnd);
            int offsetEnd = packedPrivateKey.length;
            System.arraycopy(packedPrivateKey, 0, privateKeyFixedLength, 0, offsetEnd);
            System.arraycopy(packedPublicKey, 0, privateKeyFixedLength, offsetEnd, packedPublicKey.length);
            offsetEnd = offsetEnd + packedPublicKey.length;
            System.arraycopy(pkh, 0, privateKeyFixedLength, offsetEnd, pkh.length);
            offsetEnd += pkh.length;
            System.arraycopy(rnd, 0, privateKeyFixedLength, offsetEnd, rnd.length);
            keyPair = new KyberKeyPair(packedPublicKey, privateKeyFixedLength);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return keyPair;
    }
}
