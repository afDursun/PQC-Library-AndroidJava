package com.sak.pqclibrary.saber;

public class Params {
    // L = 2 ; MU = 10 ; ET  = 3
    // L = 3 ; MU = 8 ; ET  = 4
    // L = 4 ; MU = 6 ; ET  = 6
    public static int SABER_L;
    public static int SABER_MU;
    public static int SABER_ET;

    public  static int SABER_N = 256;
    public  static int SABER_SEEDBYTES= 32;
    public  static int SABER_NOISE_SEEDBYTES= 32;
    public  static int SABER_EQ= 13;
    public  static int SABER_EP= 10;
    public  static int SABER_HASHBYTES = 32;
    public  static int SABER_KEYBYTES = 32;
    public  static int SABER_POLYCOMPRESSEDBYTES= SABER_EP * SABER_N / 8;
    public  static int SABER_POLYVECCOMPRESSEDBYTES = SABER_L * SABER_POLYCOMPRESSEDBYTES;
    public  static int SABER_POLYBYTES= SABER_EQ * SABER_N / 8;
    public  static int SABER_POLYVECBYTES= SABER_L * SABER_POLYBYTES;
    public  static int SABER_POLYCOINBYTES = (SABER_MU * SABER_N / 8);
    public  static int N_RES = SABER_N << 1;
    public  static int N_SB = SABER_N >> 2;
    public  static int N_SB_RES = (2*N_SB) - 1;
    public  static int KARATSUBA_N = 64;
    public  static int h1  = (1 << (SABER_EQ - SABER_EP - 1));
    public  static int SABER_INDCPA_SECRETKEYBYTES = (SABER_POLYVECBYTES);
    public  static int SABER_INDCPA_PUBLICKEYBYTES  = (SABER_POLYVECCOMPRESSEDBYTES + SABER_SEEDBYTES);
    public  static int SABER_SCALEBYTES_KEM = (SABER_ET * SABER_N / 8);
    public  static int SABER_BYTES_CCA_DEC = SABER_POLYVECCOMPRESSEDBYTES + SABER_SCALEBYTES_KEM;
    public  static int CRYPTO_CIPHERTEXTBYTES = SABER_BYTES_CCA_DEC;
    public  static int CRYPTO_BYTES = SABER_KEYBYTES;
    public  static int SABER_SECRETKEYBYTES = (SABER_INDCPA_SECRETKEYBYTES + SABER_INDCPA_PUBLICKEYBYTES + SABER_HASHBYTES + SABER_KEYBYTES);
    public  static int h2  = ((1 << (SABER_EP - 2)) - (1 << (SABER_EP - SABER_ET - 1)) + (1 << (SABER_EQ - SABER_EP - 1)));
    public  static boolean isRandom = true;

    public Params(int L, int MU,int ET) {
        SABER_L = L;
        SABER_MU = MU;
        SABER_ET = ET;

        SABER_N = 256;
        SABER_SEEDBYTES= 32;
        SABER_NOISE_SEEDBYTES= 32;
        SABER_EQ= 13;
        SABER_EP= 10;
        SABER_HASHBYTES = 32;
        SABER_KEYBYTES = 32;
        SABER_POLYCOMPRESSEDBYTES= SABER_EP * SABER_N / 8;
        SABER_POLYVECCOMPRESSEDBYTES = SABER_L * SABER_POLYCOMPRESSEDBYTES;
        SABER_POLYBYTES= SABER_EQ * SABER_N / 8;
        SABER_POLYVECBYTES= SABER_L * SABER_POLYBYTES;
        SABER_POLYCOINBYTES = (SABER_MU * SABER_N / 8);
        N_RES = SABER_N << 1;
        N_SB = SABER_N >> 2;
        N_SB_RES = (2*N_SB) - 1;
        KARATSUBA_N = 64;
        h1  = (1 << (SABER_EQ - SABER_EP - 1));
        SABER_INDCPA_SECRETKEYBYTES = (SABER_POLYVECBYTES);
        SABER_INDCPA_PUBLICKEYBYTES  = (SABER_POLYVECCOMPRESSEDBYTES + SABER_SEEDBYTES);
        SABER_SCALEBYTES_KEM = (SABER_ET * SABER_N / 8);
        SABER_BYTES_CCA_DEC = SABER_POLYVECCOMPRESSEDBYTES + SABER_SCALEBYTES_KEM;
        CRYPTO_CIPHERTEXTBYTES = SABER_BYTES_CCA_DEC;
        CRYPTO_BYTES = SABER_KEYBYTES;
        SABER_SECRETKEYBYTES = (SABER_INDCPA_SECRETKEYBYTES + SABER_INDCPA_PUBLICKEYBYTES + SABER_HASHBYTES + SABER_KEYBYTES);
        h2  = ((1 << (SABER_EP - 2)) - (1 << (SABER_EP - SABER_ET - 1)) + (1 << (SABER_EQ - SABER_EP - 1)));
        isRandom = false;

    }




}
