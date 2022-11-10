package com.sak.pqclibrary.kyber.kyberencryption.provider.kyber;

public final class KyberParams {
    public final static int paramsN = 256;
    public final static int paramsQ = 3329;
    public final static int paramsQinv = 62209;
    public final static int paramsSymBytes = 32;
    public final static int paramsPolyBytes = 384;
    public final static int paramsETAK512 = 3;
    public final static int paramsETAK768K1024 = 2;
    public final static int paramsPolyvecBytesK512 = 2 * paramsPolyBytes;
    public final static int paramsPolyvecBytesK768 = 3 * paramsPolyBytes;
    public final static int paramsPolyvecBytesK1024 = 4 * paramsPolyBytes;
    public final static int paramsPolyCompressedBytesK768 = 128;
    public final static int paramsPolyCompressedBytesK1024 = 160;
    public final static int paramsPolyvecCompressedBytesK512 = 2 * 320;
    public final static int paramsPolyvecCompressedBytesK768 = 3 * 320;
    public final static int paramsPolyvecCompressedBytesK1024 = 4 * 352;
    public final static int paramsIndcpaPublicKeyBytesK512 = paramsPolyvecBytesK512 + paramsSymBytes;
    public final static int paramsIndcpaPublicKeyBytesK768 = paramsPolyvecBytesK768 + paramsSymBytes;
    public final static int paramsIndcpaPublicKeyBytesK1024 = paramsPolyvecBytesK1024 + paramsSymBytes;
    public final static int paramsIndcpaSecretKeyBytesK512 = 2 * paramsPolyBytes;
    public final static int paramsIndcpaSecretKeyBytesK768 = 3 * paramsPolyBytes;
    public final static int paramsIndcpaSecretKeyBytesK1024 = 4 * paramsPolyBytes;
    public final static int Kyber512SKBytes = paramsPolyvecBytesK512 + ((paramsPolyvecBytesK512 + paramsSymBytes) + 2 * paramsSymBytes);
    public final static int Kyber768SKBytes = paramsPolyvecBytesK768 + ((paramsPolyvecBytesK768 + paramsSymBytes) + 2 * paramsSymBytes);
    public final static int Kyber1024SKBytes = paramsPolyvecBytesK1024 + ((paramsPolyvecBytesK1024 + paramsSymBytes) + 2 * paramsSymBytes);
    public final static int KyberSSBytes = 32;
}
