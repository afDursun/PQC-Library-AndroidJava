package com.sak.pqclibrary.ntru;

public class Params {

    public static int CRYPTO_SECRETKEYBYTES ;
    public static int CRYPTO_PUBLICKEYBYTES ;
    public static int CRYPTO_CIPHERTEXTBYTES;
    public static int CRYPTO_BYTES = 32;
    public static int NTRU_N ;
    public static int NTRU_LOGQ ;
    public static int NTRU_Q =(1 << NTRU_LOGQ);
    public static int NTRU_WEIGHT =(NTRU_Q/8 - 2);
    public static int NTRU_SEEDBYTES     =  32;
    public static int NTRU_PRFKEYBYTES   =  32;
    public static int NTRU_SHAREDKEYBYTES = 32;
    public static int NTRU_SAMPLE_IID_BYTES = (NTRU_N-1);
    public static int NTRU_SAMPLE_FT_BYTES =  ((30*(NTRU_N-1)+7)/8);
    public static int NTRU_SAMPLE_FG_BYTES =  (NTRU_SAMPLE_IID_BYTES+NTRU_SAMPLE_FT_BYTES);
    public static int NTRU_SAMPLE_RM_BYTES  = (NTRU_SAMPLE_IID_BYTES+NTRU_SAMPLE_FT_BYTES);
    public static int NTRU_PACK_DEG= (NTRU_N-1);
    public static int NTRU_PACK_TRINARY_BYTES =   ((NTRU_PACK_DEG+4)/5);
    public static int NTRU_OWCPA_MSGBYTES  =     (2*NTRU_PACK_TRINARY_BYTES);
    public static int NTRU_OWCPA_PUBLICKEYBYTES =((NTRU_LOGQ*NTRU_PACK_DEG+7)/8);
    public static int NTRU_OWCPA_SECRETKEYBYTES =(2*NTRU_PACK_TRINARY_BYTES + NTRU_OWCPA_PUBLICKEYBYTES);
    public static int NTRU_OWCPA_BYTES      =    ((NTRU_LOGQ*NTRU_PACK_DEG+7)/8);
    public static int NTRU_PUBLICKEYBYTES = (NTRU_OWCPA_PUBLICKEYBYTES);
    public static int NTRU_SECRETKEYBYTES = (NTRU_OWCPA_SECRETKEYBYTES + NTRU_PRFKEYBYTES);
    public static int NTRU_CIPHERTEXTBYTES= (NTRU_OWCPA_BYTES);

    public static int PAD32(int x){
        return ((((x) + 31)/32)*32);
    }


    public Params(int N){
        NTRU_N = N;
        if(N == 509){
            CRYPTO_SECRETKEYBYTES = 935;
            CRYPTO_PUBLICKEYBYTES = 699;
            CRYPTO_CIPHERTEXTBYTES = 699;
            NTRU_LOGQ = 11;
        }
        else if(N  == 677){
            CRYPTO_SECRETKEYBYTES = 1234;
            CRYPTO_PUBLICKEYBYTES = 930;
            CRYPTO_CIPHERTEXTBYTES = 930;
            NTRU_LOGQ = 11;
        }
        else if(N == 821){
            NTRU_LOGQ = 12;
            CRYPTO_SECRETKEYBYTES = 1590;
            CRYPTO_PUBLICKEYBYTES = 1230;
            CRYPTO_CIPHERTEXTBYTES = 1230;
        }

         CRYPTO_BYTES = 32;
         NTRU_Q =(1 << NTRU_LOGQ);
         NTRU_WEIGHT =(NTRU_Q/8 - 2);
         NTRU_SEEDBYTES     =  32;
         NTRU_PRFKEYBYTES   =  32;
         NTRU_SHAREDKEYBYTES = 32;
         NTRU_SAMPLE_IID_BYTES = (NTRU_N-1);
         NTRU_SAMPLE_FT_BYTES =  ((30*(NTRU_N-1)+7)/8);
         NTRU_SAMPLE_FG_BYTES =  (NTRU_SAMPLE_IID_BYTES+NTRU_SAMPLE_FT_BYTES);
         NTRU_SAMPLE_RM_BYTES  = (NTRU_SAMPLE_IID_BYTES+NTRU_SAMPLE_FT_BYTES);
         NTRU_PACK_DEG= (NTRU_N-1);
         NTRU_PACK_TRINARY_BYTES =   ((NTRU_PACK_DEG+4)/5);
         NTRU_OWCPA_MSGBYTES  =     (2*NTRU_PACK_TRINARY_BYTES);
         NTRU_OWCPA_PUBLICKEYBYTES =((NTRU_LOGQ*NTRU_PACK_DEG+7)/8);
         NTRU_OWCPA_SECRETKEYBYTES =(2*NTRU_PACK_TRINARY_BYTES + NTRU_OWCPA_PUBLICKEYBYTES);
         NTRU_OWCPA_BYTES      =    ((NTRU_LOGQ*NTRU_PACK_DEG+7)/8);
         NTRU_PUBLICKEYBYTES = (NTRU_OWCPA_PUBLICKEYBYTES);
         NTRU_SECRETKEYBYTES = (NTRU_OWCPA_SECRETKEYBYTES + NTRU_PRFKEYBYTES);
         NTRU_CIPHERTEXTBYTES= (NTRU_OWCPA_BYTES);

    }



}
