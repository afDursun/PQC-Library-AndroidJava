package com.sak.pqclibary.kyber.kyberencryption.provider;

public class KyberKeyPair {
    private byte[] kyberPublicKey,kyberPrivateKey;

    public byte[] getKyberPublicKey() {
        return kyberPublicKey;
    }

    public byte[] getKyberPrivateKey() {
        return kyberPrivateKey;
    }

    public KyberKeyPair(byte[] kyberPublicKey, byte[] kyberPrivateKey) {
        this.kyberPublicKey = kyberPublicKey;
        this.kyberPrivateKey = kyberPrivateKey;
    }
}
