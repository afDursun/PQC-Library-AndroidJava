package com.sak.pqclibrary.ntru.models;

public class TwoLongStruct {
    private short[] a,b;

    public short[] getA() {
        return a;
    }

    public short[] getB() {
        return b;
    }

    public TwoLongStruct(short[] a, short[] b) {
        this.a = a;
        this.b = b;
    }
}
