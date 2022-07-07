package com.sak.pqclibary.kyber.kyberencryption.provider;

import java.math.BigInteger;


public final class KyberPublicKeySpec implements java.security.spec.KeySpec {

    private byte[] y;
    private BigInteger p;
    private BigInteger g;

    private KyberKeySize kyberKeySize;


    public KyberPublicKeySpec(byte[] y, BigInteger p, BigInteger g, KyberKeySize kyberKeySize) {
        this.kyberKeySize = kyberKeySize;
        this.y = y;
        this.p = p;
        this.g = g;
    }
    public byte[] getY() {
        return y;
    }
    public BigInteger getP() {
        return this.p;
    }
    public BigInteger getG() {
        return this.g;
    }
    public KyberKeySize getKyberKeySize() {
        return kyberKeySize;
    }
    public void setKyberKeySize(KyberKeySize kyberKeySize) {
        this.kyberKeySize = kyberKeySize;
    }
}
