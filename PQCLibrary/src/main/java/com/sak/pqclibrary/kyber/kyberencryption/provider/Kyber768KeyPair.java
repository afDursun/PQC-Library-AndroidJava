package com.sak.pqclibrary.kyber.kyberencryption.provider;

import android.os.Build;
import android.util.Log;
import androidx.annotation.RequiresApi;
import com.github.aelstad.keccakj.fips202.SHA3_256;
import com.sak.pqclibrary.kyber.kyberencryption.provider.kyber.Indcpa;
import com.sak.pqclibrary.kyber.kyberencryption.provider.kyber.KyberParams;
import com.sak.pqclibrary.saber.models.Keys;

import java.security.*;
import java.util.ArrayList;

public final class Kyber768KeyPair {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static Keys generateKeys768( ) {
        Keys keys = null;
        KyberKeySize kyberKeySize = KyberKeySize.KEY_512;
        SecureRandom random = new SecureRandom();
        try{
            random = SecureRandom.getInstanceStrong();
        }
        catch (Exception e){
            Log.d("AFD-AFD",e.toString());
        }
        int paramsK = 3;
        try {
            ArrayList<byte[]> packedPKI = new ArrayList<>(Indcpa.generateKyberKeys(paramsK));
            byte[] packedPublicKey = packedPKI.get(1);
            byte[] packedPrivateKey = packedPKI.get(0);

            byte[] privateKeyFixedLength = new byte[KyberParams.Kyber768SKBytes];
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
            keys = new Keys(packedPublicKey, privateKeyFixedLength);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return keys;
    }
}
