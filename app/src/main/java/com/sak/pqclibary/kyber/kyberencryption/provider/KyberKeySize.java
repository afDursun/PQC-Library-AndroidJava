package com.sak.pqclibary.kyber.kyberencryption.provider;

public enum KyberKeySize {
    KEY_512("2"),
    KEY_768("3"),
    KEY_1024("4");

    public final String paramsK;

    private KyberKeySize(String keySize) {
        this.paramsK = keySize;
    }

    public int getParamsK() {
        return Integer.parseInt(paramsK);
    }
}
