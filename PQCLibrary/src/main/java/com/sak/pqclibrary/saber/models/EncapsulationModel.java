package com.sak.pqclibrary.saber.models;

public class EncapsulationModel {
    private byte[] cipherText, ss;

    public EncapsulationModel(byte[] cipherText, byte[] ss) {
        this.cipherText = cipherText;
        this.ss = ss;
    }

    public byte[] getCipherText() {
        return cipherText;
    }

    public byte[] getSs() {
        return ss;
    }
}
