package com.sak.pqclibrary.saber.models;

public class Keys {
    private byte[] pk,sk;

    public Keys(byte[] pk, byte[] sk) {
        this.pk = pk;
        this.sk = sk;
    }

    public byte[] getPk() {
        return pk;
    }

    public byte[] getSk() {
        return sk;
    }

    public void setPk(byte[] pk) {
        this.pk = pk;
    }

    public void setSk(byte[] sk) {
        this.sk = sk;
    }
}
