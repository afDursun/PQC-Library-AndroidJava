package com.sak.pqclibrary.kyber.kyberencryption.provider;

public class KyberSecretKey {
    private byte[] secretKey;
    private byte[] cipherText;

    public byte[] getSecretKey() {
        return secretKey;
    }

    public byte[] getCipherText() {
        return cipherText;
    }

    public KyberSecretKey(byte[] secretKey, byte[] cipherText) {
        this.secretKey = secretKey;
        this.cipherText = cipherText;
    }
}