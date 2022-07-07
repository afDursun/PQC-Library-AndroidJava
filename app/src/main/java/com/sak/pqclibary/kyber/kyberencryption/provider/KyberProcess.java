package com.sak.pqclibary.kyber.kyberencryption.provider;

import com.github.aelstad.keccakj.core.KeccakSponge;
import com.github.aelstad.keccakj.fips202.SHA3_256;
import com.github.aelstad.keccakj.fips202.SHA3_512;
import com.github.aelstad.keccakj.fips202.Shake256;
import com.sak.pqclibary.kyber.kyberencryption.provider.kyber.Indcpa;
import com.sak.pqclibary.kyber.kyberencryption.provider.kyber.KyberParams;
import java.security.MessageDigest;

public class KyberProcess {

    public static KyberSecretKey encrypt1024(byte[] variant, byte[] publicKey) {
        variant = verifyVariant(variant);
        KyberSecretKey kyberSecretKey = null;
        int paramsK = 4;
        try {
            byte[] sharedSecret = new byte[KyberParams.paramsSymBytes];
            MessageDigest md = new SHA3_256();
            byte[] buf1 = md.digest(variant);
            byte[] buf2 = md.digest(publicKey);
            byte[] buf3 = new byte[buf1.length + buf2.length];
            System.arraycopy(buf1, 0, buf3, 0, buf1.length);
            System.arraycopy(buf2, 0, buf3, buf1.length, buf2.length);
            MessageDigest md512 = new SHA3_512();
            byte[] kr = md512.digest(buf3);
            byte[] subKr = new byte[kr.length - KyberParams.paramsSymBytes];
            System.arraycopy(kr, KyberParams.paramsSymBytes, subKr, 0, subKr.length);
            byte[] ciphertext = Indcpa.encrypt(buf1, publicKey, subKr, paramsK);
            byte[] krc = md.digest(ciphertext);
            byte[] newKr = new byte[KyberParams.paramsSymBytes + krc.length];
            System.arraycopy(kr, 0, newKr, 0, KyberParams.paramsSymBytes);
            System.arraycopy(krc, 0, newKr, KyberParams.paramsSymBytes, krc.length);
            KeccakSponge xof = new Shake256();
            xof.getAbsorbStream().write(newKr);
            xof.getSqueezeStream().read(sharedSecret);
            kyberSecretKey = new KyberSecretKey(sharedSecret,ciphertext);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return kyberSecretKey;
    }
    public static KyberSecretKey encrypt768(byte[] variant, byte[] publicKey) {
        KyberSecretKey kyberSecretKey = null;
        variant = verifyVariant(variant);
        int paramsK = 3;
        try {
            byte[] sharedSecret = new byte[KyberParams.paramsSymBytes];
            MessageDigest md = new SHA3_256();
            byte[] buf1 = md.digest(variant);
            byte[] buf2 = md.digest(publicKey);
            byte[] buf3 = new byte[buf1.length + buf2.length];
            System.arraycopy(buf1, 0, buf3, 0, buf1.length);
            System.arraycopy(buf2, 0, buf3, buf1.length, buf2.length);
            MessageDigest md512 = new SHA3_512();
            byte[] kr = md512.digest(buf3);
            byte[] subKr = new byte[kr.length - KyberParams.paramsSymBytes];
            System.arraycopy(kr, KyberParams.paramsSymBytes, subKr, 0, subKr.length);
            byte[] ciphertext = Indcpa.encrypt(buf1, publicKey, subKr, paramsK);
            byte[] krc = md.digest(ciphertext);
            byte[] newKr = new byte[KyberParams.paramsSymBytes + krc.length];
            System.arraycopy(kr, 0, newKr, 0, KyberParams.paramsSymBytes);
            System.arraycopy(krc, 0, newKr, KyberParams.paramsSymBytes, krc.length);
            KeccakSponge xof = new Shake256();
            xof.getAbsorbStream().write(newKr);
            xof.getSqueezeStream().read(sharedSecret);
            kyberSecretKey = new KyberSecretKey(sharedSecret,ciphertext);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return kyberSecretKey;
    }

    public static KyberSecretKey encrypt512(byte[] variant, byte[] publicKey) throws IllegalArgumentException {
        KyberSecretKey kyberSecretKey = null;
        variant = verifyVariant(variant);
        int paramsK = 2;
        try {
            byte[] sharedSecret = new byte[KyberParams.paramsSymBytes];
            MessageDigest md = new SHA3_256();
            byte[] buf1 = md.digest(variant);
            byte[] buf2 = md.digest(publicKey);
            byte[] buf3 = new byte[buf1.length + buf2.length];
            System.arraycopy(buf1, 0, buf3, 0, buf1.length);
            System.arraycopy(buf2, 0, buf3, buf1.length, buf2.length);
            MessageDigest md512 = new SHA3_512();
            byte[] kr = md512.digest(buf3);
            byte[] subKr = new byte[kr.length - KyberParams.paramsSymBytes];
            System.arraycopy(kr, KyberParams.paramsSymBytes, subKr, 0, subKr.length);
            byte[] ciphertext = Indcpa.encrypt(buf1, publicKey, subKr, paramsK);
            byte[] krc = md.digest(ciphertext);
            byte[] newKr = new byte[KyberParams.paramsSymBytes + krc.length];
            System.arraycopy(kr, 0, newKr, 0, KyberParams.paramsSymBytes);
            System.arraycopy(krc, 0, newKr, KyberParams.paramsSymBytes, krc.length);
            KeccakSponge xof = new Shake256();
            xof.getAbsorbStream().write(newKr);
            xof.getSqueezeStream().read(sharedSecret);
            kyberSecretKey = new KyberSecretKey(sharedSecret,ciphertext);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return kyberSecretKey;
    }


    public static byte[] decrypt512(byte[] ciphertext,byte[] privateKey) {
        int paramsK = 2;
        byte[] sharedSecretFixedLength = new byte[KyberParams.KyberSSBytes];
        byte[] indcpaPrivateKey = new byte[KyberParams.paramsIndcpaSecretKeyBytesK512];
        System.arraycopy(privateKey, 0, indcpaPrivateKey, 0, indcpaPrivateKey.length);
        byte[] publicKey = new byte[KyberParams.paramsIndcpaPublicKeyBytesK512];
        System.arraycopy(privateKey, KyberParams.paramsIndcpaSecretKeyBytesK512, publicKey, 0, publicKey.length);

        byte[] buf = Indcpa.decrypt(ciphertext, indcpaPrivateKey, paramsK);
        int ski = KyberParams.Kyber512SKBytes - 2 * KyberParams.paramsSymBytes;
        byte[] newBuf = new byte[buf.length + KyberParams.paramsSymBytes];
        System.arraycopy(buf, 0, newBuf, 0, buf.length);
        System.arraycopy(privateKey, ski, newBuf, buf.length, KyberParams.paramsSymBytes);
        MessageDigest md512 = new SHA3_512();
        byte[] kr = md512.digest(newBuf);
        byte[] subKr = new byte[kr.length - KyberParams.paramsSymBytes];
        System.arraycopy(kr, KyberParams.paramsSymBytes, subKr, 0, subKr.length);
        byte[] cmp = Indcpa.encrypt(buf, publicKey, subKr, paramsK);
        byte fail = (byte) KyberKeyUtil.constantTimeCompare(ciphertext, cmp);
        if (fail == (byte) 0) {
            MessageDigest md = new SHA3_256();
            byte[] krh = md.digest(ciphertext);
            for (int i = 0; i < KyberParams.paramsSymBytes; i++) {
                int length = KyberParams.Kyber512SKBytes - KyberParams.paramsSymBytes + i;
                byte[] skx = new byte[length];
                System.arraycopy(privateKey, 0, skx, 0, length);
                kr[i] = (byte) ((int) (kr[i] & 0xFF) ^ ((int) (fail & 0xFF) & ((int) (kr[i] & 0xFF) ^ (int) (skx[i] & 0xFF))));
            }
            byte[] tempBuf = new byte[KyberParams.paramsSymBytes + krh.length];
            System.arraycopy(kr, 0, tempBuf, 0, KyberParams.paramsSymBytes);
            System.arraycopy(krh, 0, tempBuf, KyberParams.paramsSymBytes, krh.length);
            KeccakSponge xof = new Shake256();
            xof.getAbsorbStream().write(tempBuf);
            xof.getSqueezeStream().read(sharedSecretFixedLength);
            return sharedSecretFixedLength;
        } else {
            throw new IllegalArgumentException("Geçersiz anahtarlar");
        }
    }
    public static byte[] decrypt768(byte[] ciphertext,byte[] privateKey) {
        int paramsK = 3;
        byte[] sharedSecretFixedLength = new byte[KyberParams.KyberSSBytes];
        byte[] indcpaPrivateKey = new byte[KyberParams.paramsIndcpaSecretKeyBytesK768];
        System.arraycopy(privateKey, 0, indcpaPrivateKey, 0, indcpaPrivateKey.length);
        byte[] publicKey = new byte[KyberParams.paramsIndcpaPublicKeyBytesK768];
        System.arraycopy(privateKey, KyberParams.paramsIndcpaSecretKeyBytesK768, publicKey, 0, publicKey.length);

        byte[] buf = Indcpa.decrypt(ciphertext, indcpaPrivateKey, paramsK);
        int ski = KyberParams.Kyber768SKBytes - 2 * KyberParams.paramsSymBytes;
        byte[] newBuf = new byte[buf.length + KyberParams.paramsSymBytes];
        System.arraycopy(buf, 0, newBuf, 0, buf.length);
        System.arraycopy(privateKey, ski, newBuf, buf.length, KyberParams.paramsSymBytes);
        MessageDigest md512 = new SHA3_512();
        byte[] kr = md512.digest(newBuf);
        byte[] subKr = new byte[kr.length - KyberParams.paramsSymBytes];
        System.arraycopy(kr, KyberParams.paramsSymBytes, subKr, 0, subKr.length);
        byte[] cmp = Indcpa.encrypt(buf, publicKey, subKr, paramsK);
        byte fail = (byte) KyberKeyUtil.constantTimeCompare(ciphertext, cmp);
        if (fail == (byte) 0) {
            MessageDigest md = new SHA3_256();
            byte[] krh = md.digest(ciphertext);
            for (int i = 0; i < KyberParams.paramsSymBytes; i++) {
                int length = KyberParams.Kyber768SKBytes - KyberParams.paramsSymBytes + i;
                byte[] skx = new byte[length];
                System.arraycopy(privateKey, 0, skx, 0, length);
                kr[i] = (byte) ((int) (kr[i] & 0xFF) ^ ((int) (fail & 0xFF) & ((int) (kr[i] & 0xFF) ^ (int) (skx[i] & 0xFF))));
            }
            byte[] tempBuf = new byte[KyberParams.paramsSymBytes + krh.length];
            System.arraycopy(kr, 0, tempBuf, 0, KyberParams.paramsSymBytes);
            System.arraycopy(krh, 0, tempBuf, KyberParams.paramsSymBytes, krh.length);
            KeccakSponge xof = new Shake256();
            xof.getAbsorbStream().write(tempBuf);
            xof.getSqueezeStream().read(sharedSecretFixedLength);

            return sharedSecretFixedLength;
        } else {
            throw new IllegalArgumentException("Geçersiz anahtarlar");
        }
    }
    public static byte[] decrypt1024(byte[] ciphertext,byte[] privateKey)
            throws IllegalArgumentException {
        int paramsK = 4;
        byte[] sharedSecretFixedLength = new byte[KyberParams.KyberSSBytes];
        byte[] indcpaPrivateKey = new byte[KyberParams.paramsIndcpaSecretKeyBytesK1024];
        System.arraycopy(privateKey, 0, indcpaPrivateKey, 0, indcpaPrivateKey.length);
        byte[] publicKey = new byte[KyberParams.paramsIndcpaPublicKeyBytesK1024];
        System.arraycopy(privateKey, KyberParams.paramsIndcpaSecretKeyBytesK1024, publicKey, 0, publicKey.length);

        byte[] buf = Indcpa.decrypt(ciphertext, indcpaPrivateKey, paramsK);
        int ski = KyberParams.Kyber1024SKBytes - 2 * KyberParams.paramsSymBytes;
        byte[] newBuf = new byte[buf.length + KyberParams.paramsSymBytes];
        System.arraycopy(buf, 0, newBuf, 0, buf.length);
        System.arraycopy(privateKey, ski, newBuf, buf.length, KyberParams.paramsSymBytes);
        MessageDigest md512 = new SHA3_512();
        byte[] kr = md512.digest(newBuf);
        byte[] subKr = new byte[kr.length - KyberParams.paramsSymBytes];
        System.arraycopy(kr, KyberParams.paramsSymBytes, subKr, 0, subKr.length);
        byte[] cmp = Indcpa.encrypt(buf, publicKey, subKr, paramsK);
        byte fail = (byte) KyberKeyUtil.constantTimeCompare(ciphertext, cmp);
        if (fail == (byte) 0) {
            MessageDigest md = new SHA3_256();
            byte[] krh = md.digest(ciphertext);
            for (int i = 0; i < KyberParams.paramsSymBytes; i++) {
                int length = KyberParams.Kyber1024SKBytes - KyberParams.paramsSymBytes + i;
                byte[] skx = new byte[length];
                System.arraycopy(privateKey, 0, skx, 0, length);
                kr[i] = (byte) ((int) (kr[i] & 0xFF) ^ ((int) (fail & 0xFF) & ((int) (kr[i] & 0xFF) ^ (int) (skx[i] & 0xFF))));
            }
            byte[] tempBuf = new byte[KyberParams.paramsSymBytes + krh.length];
            System.arraycopy(kr, 0, tempBuf, 0, KyberParams.paramsSymBytes);
            System.arraycopy(krh, 0, tempBuf, KyberParams.paramsSymBytes, krh.length);
            KeccakSponge xof = new Shake256();
            xof.getAbsorbStream().write(tempBuf);
            xof.getSqueezeStream().read(sharedSecretFixedLength);

            return sharedSecretFixedLength;
        } else {
            throw new IllegalArgumentException("Geçersiz anahtarlar");
        }
    }

    public static byte[] verifyVariant(byte[] variant) throws IllegalArgumentException {
        if (variant.length > KyberParams.paramsSymBytes) {
            throw new IllegalArgumentException("" + KyberParams.paramsSymBytes + "");
        } else if (variant.length < KyberParams.paramsSymBytes) {
            byte[] tempData = new byte[KyberParams.paramsSymBytes];
            System.arraycopy(variant, 0, tempData, 0, variant.length);
            byte[] emptyBytes = new byte[KyberParams.paramsSymBytes - variant.length];
            for (int i = 0; i < emptyBytes.length; ++i) {
                emptyBytes[i] = (byte) 0;
            }

            System.arraycopy(emptyBytes, 0, tempData, variant.length, emptyBytes.length);
            return tempData;
        }
        return variant;
    }
}
