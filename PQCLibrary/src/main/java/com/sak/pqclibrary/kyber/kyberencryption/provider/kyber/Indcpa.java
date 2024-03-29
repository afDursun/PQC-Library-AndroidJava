package com.sak.pqclibrary.kyber.kyberencryption.provider.kyber;

import android.os.Build;
import androidx.annotation.RequiresApi;
import com.github.aelstad.keccakj.core.KeccakSponge;
import com.github.aelstad.keccakj.fips202.SHA3_512;
import com.github.aelstad.keccakj.fips202.Shake128;
import com.github.aelstad.keccakj.fips202.Shake256;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;

public final class Indcpa {
    public static void generateUniform(KyberUniformRandom uniformRandom, byte[] buf, int bufl, int l) {
        short[] uniformR = new short[KyberParams.paramsPolyBytes];
        int d1;
        int d2;
        int uniformI = 0; // Always start at 0
        int j = 0;
        while ((uniformI < l) && ((j + 3) <= bufl)) {
            d1 = (int) (((((int) (buf[j] & 0xFF)) >> 0) | (((int) (buf[j + 1] & 0xFF)) << 8)) & 0xFFF);
            d2 = (int) (((((int) (buf[j + 1] & 0xFF)) >> 4) | (((int) (buf[j + 2] & 0xFF)) << 4)) & 0xFFF);
            j = j + 3;
            if (d1 < (int) KyberParams.paramsQ) {
                uniformR[uniformI] = (short) d1;
                uniformI++;
            }
            if (uniformI < l && d2 < (int) KyberParams.paramsQ) {
                uniformR[uniformI] = (short) d2;
                uniformI++;
            }
        }
        uniformRandom.setUniformI(uniformI);
        uniformRandom.setUniformR(uniformR);
    }


    public static UnpackedPublicKey unpackPublicKey(byte[] packedPublicKey, int paramsK) {
        UnpackedPublicKey unpackedKey = new UnpackedPublicKey();
        switch (paramsK) {
            case 2:
                unpackedKey.setPublicKeyPolyvec(Poly.polyVectorFromBytes(Arrays.copyOfRange(packedPublicKey, 0, KyberParams.paramsPolyvecBytesK512), paramsK));
                unpackedKey.setSeed(Arrays.copyOfRange(packedPublicKey, KyberParams.paramsPolyvecBytesK512, packedPublicKey.length));
                break;
            case 3:
                unpackedKey.setPublicKeyPolyvec(Poly.polyVectorFromBytes(Arrays.copyOfRange(packedPublicKey, 0, KyberParams.paramsPolyvecBytesK768), paramsK));
                unpackedKey.setSeed(Arrays.copyOfRange(packedPublicKey, KyberParams.paramsPolyvecBytesK768, packedPublicKey.length));
                break;
            default:
                unpackedKey.setPublicKeyPolyvec(Poly.polyVectorFromBytes(Arrays.copyOfRange(packedPublicKey, 0, KyberParams.paramsPolyvecBytesK1024), paramsK));
                unpackedKey.setSeed(Arrays.copyOfRange(packedPublicKey, KyberParams.paramsPolyvecBytesK1024, packedPublicKey.length));
        }
        return unpackedKey;
    }
    public static byte[] packPrivateKey(short[][] privateKey, int paramsK) {
        byte[] packedPrivateKey = Poly.polyVectorToBytes(privateKey, paramsK);
        return packedPrivateKey;
    }

    public static short[][] unpackPrivateKey(byte[] packedPrivateKey, int paramsK) {
        short[][] unpackedPrivateKey = Poly.polyVectorFromBytes(packedPrivateKey, paramsK);
        return unpackedPrivateKey;
    }
    public static byte[] packCiphertext(short[][] b, short[] v, int paramsK) {
        byte[] bCompress = Poly.compressPolyVector(b, paramsK);
        byte[] vCompress = Poly.compressPoly(v, paramsK);
        byte[] returnArray = new byte[bCompress.length + vCompress.length];
        System.arraycopy(bCompress, 0, returnArray, 0, bCompress.length);
        System.arraycopy(vCompress, 0, returnArray, bCompress.length, vCompress.length);
        return returnArray;
    }
    public static UnpackedCipherText unpackCiphertext(byte[] c, int paramsK) {
        UnpackedCipherText unpackedCipherText = new UnpackedCipherText();
        byte[] bpc;
        byte[] vc;
        switch (paramsK) {
            case 2:
                bpc = new byte[KyberParams.paramsPolyvecCompressedBytesK512];
                break;
            case 3:
                bpc = new byte[KyberParams.paramsPolyvecCompressedBytesK768];
                break;
            default:
                bpc = new byte[KyberParams.paramsPolyvecCompressedBytesK1024];
        }
        System.arraycopy(c, 0, bpc, 0, bpc.length);
        vc = new byte[c.length - bpc.length];
        System.arraycopy(c, bpc.length, vc, 0, vc.length);
        unpackedCipherText.setBp(Poly.decompressPolyVector(bpc, paramsK));
        unpackedCipherText.setV(Poly.decompressPoly(vc, paramsK));

        return unpackedCipherText;
    }

    public static byte[] generatePRFByteArray(int l, byte[] key, byte nonce) {
        byte[] hash = new byte[l];
        KeccakSponge xof = new Shake256();
        byte[] newKey = new byte[key.length + 1];
        System.arraycopy(key, 0, newKey, 0, key.length);
        newKey[key.length] = nonce;
        xof.getAbsorbStream().write(newKey);
        xof.getSqueezeStream().read(hash);
        return hash;
    }
    public static byte[] packPublicKey(short[][] publicKey, byte[] seed, int paramsK) {
        byte[] initialArray = Poly.polyVectorToBytes(publicKey, paramsK);
        byte[] packedPublicKey;
        switch (paramsK) {
            case 2:
                packedPublicKey = new byte[KyberParams.paramsIndcpaPublicKeyBytesK512];
                System.arraycopy(initialArray, 0, packedPublicKey, 0, initialArray.length);
                System.arraycopy(seed, 0, packedPublicKey, initialArray.length, seed.length);
                break;
            case 3:
                packedPublicKey = new byte[KyberParams.paramsIndcpaPublicKeyBytesK768];
                System.arraycopy(initialArray, 0, packedPublicKey, 0, initialArray.length);
                System.arraycopy(seed, 0, packedPublicKey, initialArray.length, seed.length);
                break;
            default:
                packedPublicKey = new byte[KyberParams.paramsIndcpaPublicKeyBytesK1024];
                System.arraycopy(initialArray, 0, packedPublicKey, 0, initialArray.length);
                System.arraycopy(seed, 0, packedPublicKey, initialArray.length, seed.length);
        }

        return packedPublicKey;
    }
    public static short[][][] generateMatrix(byte[] seed, boolean transposed, int paramsK) {
        short[][][] r = new short[paramsK][paramsK][KyberParams.paramsPolyBytes];
        byte[] buf = new byte[672];
        KyberUniformRandom uniformRandom = new KyberUniformRandom();
        KeccakSponge xof = new Shake128();
        for (int i = 0; i < paramsK; i++) {
            r[i] = Poly.generateNewPolyVector(paramsK);
            for (int j = 0; j < paramsK; j++) {
                xof.reset();
                xof.getAbsorbStream().write(seed);
                byte[] ij = new byte[2];
                if (transposed) {
                    ij[0] = (byte) i;
                    ij[1] = (byte) j;
                } else {
                    ij[0] = (byte) j;
                    ij[1] = (byte) i;
                }
                xof.getAbsorbStream().write(ij);
                xof.getSqueezeStream().read(buf);
                generateUniform(uniformRandom, Arrays.copyOfRange(buf, 0, 504), 504, KyberParams.paramsN);
                int ui = uniformRandom.getUniformI();
                r[i][j] = uniformRandom.getUniformR();
                while (ui < KyberParams.paramsN) {
                    generateUniform(uniformRandom, Arrays.copyOfRange(buf, 504, 672), 168, KyberParams.paramsN - ui);
                    int ctrn = uniformRandom.getUniformI();
                    short[] missing = uniformRandom.getUniformR();
                    for (int k = ui; k < KyberParams.paramsN; k++) {
                        r[i][j][k] = missing[k - ui];
                    }
                    ui = ui + ctrn;
                }
            }
        }
        return r;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static ArrayList<byte[]> generateKyberKeys(int paramsK) {
        ArrayList<byte[]> packedPKI = new ArrayList<>();
        try {
            short[][] skpv = Poly.generateNewPolyVector(paramsK);
            short[][] pkpv = Poly.generateNewPolyVector(paramsK);
            short[][] e = Poly.generateNewPolyVector(paramsK);
            byte[] publicSeed = new byte[KyberParams.paramsSymBytes];
            byte[] noiseSeed = new byte[KyberParams.paramsSymBytes];

            MessageDigest h = new SHA3_512();
            SecureRandom sr = SecureRandom.getInstanceStrong();
            sr.nextBytes(publicSeed);
            byte[] fullSeed = h.digest(publicSeed);

            System.arraycopy(fullSeed, 0, publicSeed, 0, KyberParams.paramsSymBytes);
            System.arraycopy(fullSeed, KyberParams.paramsSymBytes, noiseSeed, 0, KyberParams.paramsSymBytes);
            short[][][] a = generateMatrix(publicSeed, false, paramsK);
            byte nonce = (byte) 0;
            for (int i = 0; i < paramsK; i++) {
                skpv[i] = Poly.getNoisePoly(noiseSeed, nonce, paramsK);
                nonce = (byte) (nonce + (byte) 1);
            }
            for (int i = 0; i < paramsK; i++) {
                e[i] = Poly.getNoisePoly(noiseSeed, nonce, paramsK);
                nonce = (byte) (nonce + (byte) 1);
            }
            skpv = Poly.polyVectorNTT(skpv, paramsK);
            skpv = Poly.polyVectorReduce(skpv, paramsK);
            e = Poly.polyVectorNTT(e, paramsK);
            for (int i = 0; i < paramsK; i++) {
                short[] temp = Poly.polyVectorPointWiseAccMont(a[i], skpv, paramsK);
                pkpv[i] = Poly.polyToMont(temp);
            }
            pkpv = Poly.polyVectorAdd(pkpv, e, paramsK);
            pkpv = Poly.polyVectorReduce(pkpv, paramsK);
            packedPKI.add(packPrivateKey(skpv, paramsK));
            packedPKI.add(packPublicKey(pkpv, publicSeed, paramsK));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return packedPKI;
    }
    public static byte[] encrypt(byte[] m, byte[] publicKey, byte[] coins, int paramsK) {
        short[][] sp = Poly.generateNewPolyVector(paramsK);
        short[][] ep = Poly.generateNewPolyVector(paramsK);
        short[][] bp = Poly.generateNewPolyVector(paramsK);
        UnpackedPublicKey unpackedPublicKey = unpackPublicKey(publicKey, paramsK);
        short[] k = Poly.polyFromData(m);
        short[][][] at = generateMatrix(Arrays.copyOfRange(unpackedPublicKey.getSeed(), 0, KyberParams.paramsSymBytes), true, paramsK);

        for (int i = 0; i < paramsK; i++) {
            sp[i] = Poly.getNoisePoly(coins, (byte) (i), paramsK);
            ep[i] = Poly.getNoisePoly(coins, (byte) (i + paramsK), 3);
        }

        short[] epp = Poly.getNoisePoly(coins, (byte) (paramsK * 2), 3);
        sp = Poly.polyVectorNTT(sp, paramsK);
        sp = Poly.polyVectorReduce(sp, paramsK);
        for (int i = 0; i < paramsK; i++) {
            bp[i] = Poly.polyVectorPointWiseAccMont(at[i], sp, paramsK);
        }
        short[] v = Poly.polyVectorPointWiseAccMont(unpackedPublicKey.getPublicKeyPolyvec(), sp, paramsK);
        bp = Poly.polyVectorInvNTTMont(bp, paramsK);
        v = Poly.polyInvNTTMont(v);
        bp = Poly.polyVectorAdd(bp, ep, paramsK);
        v = Poly.polyAdd(Poly.polyAdd(v, epp), k);
        bp = Poly.polyVectorReduce(bp, paramsK);

        return packCiphertext(bp, Poly.polyReduce(v), paramsK);
    }
    public static byte[] decrypt(byte[] packedCipherText, byte[] privateKey, int paramsK) {
        UnpackedCipherText unpackedCipherText = unpackCiphertext(packedCipherText, paramsK);
        short[][] bp = unpackedCipherText.getBp();
        short[] v = unpackedCipherText.getV();
        short[][] unpackedPrivateKey = unpackPrivateKey(privateKey, paramsK);
        bp = Poly.polyVectorNTT(bp, paramsK);
        short[] mp = Poly.polyVectorPointWiseAccMont(unpackedPrivateKey, bp, paramsK);
        mp = Poly.polyInvNTTMont(mp);
        mp = Poly.polySub(v, mp);
        mp = Poly.polyReduce(mp);
        return Poly.polyToMsg(mp);
    }
}
