package com.sak.pqclibary.ntru;

import static com.sak.pqclibary.ntru.Params.CRYPTO_BYTES;
import static com.sak.pqclibary.ntru.Params.CRYPTO_CIPHERTEXTBYTES;
import static com.sak.pqclibary.ntru.Params.CRYPTO_PUBLICKEYBYTES;
import static com.sak.pqclibary.ntru.Params.CRYPTO_SECRETKEYBYTES;
import static com.sak.pqclibary.ntru.Params.NTRU_CIPHERTEXTBYTES;
import static com.sak.pqclibary.ntru.Params.NTRU_LOGQ;
import static com.sak.pqclibary.ntru.Params.NTRU_N;
import static com.sak.pqclibary.ntru.Params.NTRU_OWCPA_MSGBYTES;
import static com.sak.pqclibary.ntru.Params.NTRU_OWCPA_SECRETKEYBYTES;
import static com.sak.pqclibary.ntru.Params.NTRU_PACK_DEG;
import static com.sak.pqclibary.ntru.Params.NTRU_PACK_TRINARY_BYTES;
import static com.sak.pqclibary.ntru.Params.NTRU_PRFKEYBYTES;
import static com.sak.pqclibary.ntru.Params.NTRU_Q;
import static com.sak.pqclibary.ntru.Params.NTRU_SAMPLE_FG_BYTES;
import static com.sak.pqclibary.ntru.Params.NTRU_SAMPLE_RM_BYTES;

import com.sak.pqclibary.saber.models.Keys;
import com.github.aelstad.keccakj.fips202.SHA3_256;
import com.sak.pqclibary.saber.models.EncapsulationModel;
import com.sak.pqclibary.ntru.models.TwoLongStruct;

import java.security.MessageDigest;
import java.util.Arrays;

public class OWCPA {
    public static String hex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte aByte : bytes) {
            result.append(String.format("%02x", aByte));
        }
        return result.toString();
    }

    public static Keys owcpa_keypair(){
        byte[] seed= new byte[NTRU_SAMPLE_FG_BYTES];

        for (int i = 0 ; i < NTRU_SAMPLE_FG_BYTES ; i++){
            seed[i] = 5;
        }
        byte[] sk_part4 = new byte[CRYPTO_SECRETKEYBYTES-32];
        byte[] sk_part1 ;
        byte[] sk_part2 ;
        byte[] sk_part3 ;
        byte[] sk_part5 = new byte[NTRU_PRFKEYBYTES] ;
        byte[] sk = new byte[CRYPTO_SECRETKEYBYTES] ;
        byte[] pk = new byte[CRYPTO_PUBLICKEYBYTES] ;

        short[] x1 = new short[NTRU_N]; short[] x2 = new short[NTRU_N]; short[] x3 = new short[NTRU_N];
        short[] x4 = new short[NTRU_N]; short[] x5 = new short[NTRU_N];

        short[] f = x1; short[] g = x2; short[] invf_mod3 = x3;
        short[] gf = x3; short[] invgf = x4 ; short[] tmp = x5;
        short[] invh = x3; short[] h = x3;

        TwoLongStruct fg = Sample.sample_fg(seed);
        f = fg.getA();
        g = fg.getB();

        invf_mod3 = Poly_s3_inv.poly_S3_inv(f); //aşırı tüketim
        sk_part1 = Pack3.poly_S3_tobytes(f);
        sk_part2 = Pack3.poly_S3_tobytes(invf_mod3);

        f = Poly.poly_Z3_to_Zq(f);
        g = Poly.poly_Z3_to_Zq(g);

        for(int i=0; i<NTRU_N; i++)
            g[i] = (short) (3 * g[i]);

        gf = Poly_req_mul.poly_Rq_mul(g,f);
        invgf = Poly.poly_Rq_inv(gf); // aşırı tüketim
        tmp = Poly_req_mul.poly_Rq_mul(invgf, f);
        invh = Poly.poly_Sq_mul( tmp, f);
        sk_part3 = Packq.poly_Sq_tobytes(invh);


        System.arraycopy(sk_part1, 0, sk_part4, 0, sk_part1.length);
        System.arraycopy(sk_part2, 0, sk_part4, sk_part1.length, sk_part2.length);
        System.arraycopy(sk_part3, 0, sk_part4, sk_part2.length+sk_part1.length, sk_part3.length);


        for (int i = 0 ; i < NTRU_PRFKEYBYTES ; i++){
            sk_part5[i] = 1;
         }

        System.arraycopy(sk_part4, 0, sk, 0, sk_part4.length);
        System.arraycopy(sk_part5, 0, sk, sk_part4.length, sk_part5.length);

        tmp = Poly_req_mul.poly_Rq_mul(invgf, g);
        h = Poly_req_mul.poly_Rq_mul(tmp, g);
        pk = Packq.poly_Rq_sum_zero_tobytes(h);
        return new Keys(pk,sk);
    }
    public static EncapsulationModel crypto_kem_enc(byte[] pk){
        short[] r = new short[NTRU_N] ;
        short[] b = new short[NTRU_N] ;
        byte[]  rm = new byte[NTRU_OWCPA_MSGBYTES];
        byte[]  c = new byte[CRYPTO_CIPHERTEXTBYTES];
        byte[]  rm_1 = new byte[NTRU_PACK_TRINARY_BYTES];
        byte[]  rm_2 = new byte[NTRU_PACK_TRINARY_BYTES];
        byte[]  rm_seed = new byte[NTRU_SAMPLE_RM_BYTES];

        for (int i = 0 ; i< NTRU_SAMPLE_RM_BYTES ; i++)
            rm_seed[i] = 1;

        TwoLongStruct twoLongStruct = Sample.sample_rm(rm_seed);

        rm_1 = Pack3.poly_S3_tobytes(twoLongStruct.getA());
        rm_2 = Pack3.poly_S3_tobytes(twoLongStruct.getB());

        System.arraycopy(rm_1, 0, rm, 0, rm_1.length);
        System.arraycopy(rm_2, 0, rm, rm_1.length, rm_2.length);


        MessageDigest md = new SHA3_256();
        byte[] k = md.digest(rm); //KEY_A
        r = Poly.poly_Z3_to_Zq(twoLongStruct.getA());

        c = owcpa_enc(r, twoLongStruct.getB(), pk);
        return new EncapsulationModel(c,k);
    }
    public static byte[] crypto_kem_dec(byte[] c, byte[] sk){
        byte[] k = new byte[CRYPTO_BYTES];

        byte[] rm = new byte[NTRU_OWCPA_MSGBYTES];
        byte[] buf = new byte[NTRU_PRFKEYBYTES+NTRU_CIPHERTEXTBYTES];

        rm = owcpa_dec(c, sk);

        MessageDigest md = new SHA3_256();
        k = md.digest(rm);

        for(int i =0;i<NTRU_PRFKEYBYTES;i++)
            buf[i] = sk[i+NTRU_OWCPA_SECRETKEYBYTES];
        for(int i =0;i<NTRU_CIPHERTEXTBYTES;i++)
            buf[NTRU_PRFKEYBYTES + i] = c[i];

        rm = md.digest(buf);

        //cmov(k, rm, NTRU_SHAREDKEYBYTES, (unsigned char) fail);

        return k;
    }

    private static byte[] owcpa_dec(byte[] ciphertext, byte[] secretkey) {
        byte[] rm = new byte[NTRU_OWCPA_MSGBYTES];
        byte[] rm_1 ;
        byte[] rm_2;

        int i;
        int fail;
        short[] x1 = new short[NTRU_N] ;
        short[] x2 = new short[NTRU_N] ;
        short[] x3 = new short[NTRU_N] ;
        short[] x4 = new short[NTRU_N] ;
        short[] c = new short[NTRU_N] ;
        short[] f = new short[NTRU_N] ;
        short[] cf = new short[NTRU_N] ;
        short[] liftm = new short[NTRU_N] ;
        short[] mf = new short[NTRU_N] ;
        short[] invh = new short[NTRU_N] ;
        short[] finv3 = new short[NTRU_N] ;
        short[] m = new short[NTRU_N] ;
        short[] r = new short[NTRU_N] ;
        short[] b = new short[NTRU_N] ;

        c = Packq.poly_Rq_sum_zero_frombytes(ciphertext);
        f = poly_S3_frombytes(secretkey);
        f = Poly.poly_Z3_to_Zq(f);
        
        cf = Poly_req_mul.poly_Rq_mul(c, f);
        mf = poly_Rq_to_S3(cf);
        finv3 = poly_S3_frombytes( Arrays.copyOfRange(secretkey,NTRU_PACK_TRINARY_BYTES,secretkey.length));
        m = poly_S3_mul(mf, finv3);
        rm_2 = Pack3.poly_S3_tobytes(m);

        fail = 0;
        fail |= owcpa_check_ciphertext(ciphertext);
        fail |= owcpa_check_r(r);
        liftm = Poly.poly_lift(m);
        for(i=0; i<NTRU_N; i++)
            b[i] = (short) (c[i] - liftm[i]);

        /* r = b / h mod (q, Phi_n) */
        invh = Poly.poly_Sq_frombytes( Arrays.copyOfRange(secretkey,2*NTRU_PACK_TRINARY_BYTES,secretkey.length));
        r = Poly.poly_Sq_mul(b, invh);

        r = poly_trinary_Zq_to_Z3(r);

        rm_1 = Pack3.poly_S3_tobytes(r);

        System.arraycopy(rm_1, 0, rm, 0, rm_1.length);
        System.arraycopy(rm_2, 0, rm, rm_1.length, rm_2.length);



        return rm;
    }

    private static int owcpa_check_r(short[] r) {
        int i;
        int t = 0;
        short c;
        for(i=0; i<NTRU_N-1; i++)
        {
            c = r[i];
            t |= (c + 1) & (NTRU_Q-4);  /* 0 iff c is in {-1,0,1,2} */
            t |= (c + 2) & 4;  /* 1 if c = 2, 0 if c is in {-1,0,1} */
        }
        t |= r[NTRU_N-1]; /* Coefficient n-1 must be zero */

        /* We have 0 <= t < 2^16. */
        /* Return 0 on success (t=0), 1 on failure */
        return (int) (1&((~t + 1) >> 31));
    }

    private static int owcpa_check_ciphertext(byte[] ciphertext) {
        short t = 0;

        t = ciphertext[NTRU_CIPHERTEXTBYTES-1];
        t &= 0xff << (8-(7 & (NTRU_LOGQ*NTRU_PACK_DEG)));

        return (int) (1&((~t + 1) >> 15));
    }

    private static short[] poly_trinary_Zq_to_Z3(short[] r) {
        short[] r1 = new short[NTRU_N] ;
        int i;
        for(i=0; i<NTRU_N; i++)
        {
            r1[i] = Poly.MODQ(r[i]);
            r1[i] = (short) (3 & (r1[i] ^ (r1[i]>>(NTRU_LOGQ-1))));
        }
        return r1;
    }

    private static short[] poly_S3_mul(short[] a, short[] b) {
        short[] r = new short[NTRU_N] ;
        r = Poly_req_mul.poly_Rq_mul(a, b);
        r = poly_mod_3_Phi_n(r);
        return r;
    }

    private static short[] poly_Rq_to_S3(short[] a) {
        short[] r = new short[NTRU_N] ;

        int i;
        short flag;

        /* The coefficients of a are stored as non-negative integers. */
        /* We must translate to representatives in [-q/2, q/2) before */
        /* reduction mod 3.                                           */
        for(i=0; i<NTRU_N; i++)
        {
            r[i] = Poly.MODQ(a[i]);
            flag = (short) (r[i] >> (NTRU_LOGQ-1));
            r[i] += flag << (1-(NTRU_LOGQ&1));
        }

        r = poly_mod_3_Phi_n(r);

        return r;
    }


    private static short[] poly_S3_frombytes(byte[] msg) {
        short[] r = new short[NTRU_N] ;
        short c;
        int i ;
        for(i =0; i<NTRU_PACK_DEG/5; i++)
        {
            if(msg[i]<0){
                c = (short) (msg[i]+256);
            }
            else{
                c = msg[i];
            }

            r[5*i+0] = c;
            r[5*i+1] = (short) (c * 171 >> 9);  // this is division by 3
            r[5*i+2] = (short) (c * 57 >> 9);  // division by 3^2
            r[5*i+3] = (short) (c * 19 >> 9);  // division by 3^3
            r[5*i+4] = (short) (c * 203 >> 14);  // etc.
        }

        if (NTRU_PACK_DEG > (NTRU_PACK_DEG / 5) * 5) {
            i = NTRU_PACK_DEG/5;
            if(msg[i]<0){
                c = (short) (msg[i]+256);
            }
            else{
                c = msg[i];
            }
            for(int j=0; (5*i+j)<NTRU_PACK_DEG; j++)
            {
                r[5*i+j] = c;
                c = (byte) (c * 171 >> 9);
            }
        }
        r[NTRU_N-1] = 0;
        r = poly_mod_3_Phi_n(r);

        return r;
    }

    private static short[] poly_mod_3_Phi_n(short[] r) {
        short[] a = new short[NTRU_N] ;
        int i;
        for(i=0; i <NTRU_N; i++)
            a[i] = Sample_iid.mod3((short) (r[i] + 2*r[NTRU_N-1]));
        return a;
    }

    private static byte[] owcpa_enc(short[] r, short[] m, byte[] pk) {
        byte[]  c = new byte[CRYPTO_CIPHERTEXTBYTES];

        short[] x1 = new short[NTRU_N]; short[] x2 = new short[NTRU_N];

        short[] h = x1; short[] liftm = x1;
        short[] ct = x2;

        h = Packq.poly_Rq_sum_zero_frombytes(pk);
        ct = Poly_req_mul.poly_Rq_mul(r, h);
        liftm = Poly.poly_lift(m);

        for(int i =0; i<NTRU_N; i++)
            ct[i] = (short) (ct[i] + liftm[i]);

        c = Packq.poly_Rq_sum_zero_tobytes(ct);

        return c;
    }























}
