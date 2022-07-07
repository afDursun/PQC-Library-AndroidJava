package com.sak.pqclibary.saber;

import static com.sak.pqclibary.saber.Params.KARATSUBA_N;
import static com.sak.pqclibary.saber.Params.N_SB;
import static com.sak.pqclibary.saber.Params.N_SB_RES;
import static com.sak.pqclibary.saber.Params.SABER_N;

import java.util.Arrays;

public class PolyMul {
    public static short[] toom_cook_4way(short[] a, short[] b, short[] c) {
        short inv3 = (short) 43691, inv9 = (short) 36409, inv15 = (short) 61167;

        short[] aw1 = new short[N_SB];
        short[] aw2 = new short[N_SB];
        short[] aw3 = new short[N_SB];
        short[] aw4 = new short[N_SB];
        short[] aw5 = new short[N_SB];
        short[] aw6 = new short[N_SB];
        short[] aw7 = new short[N_SB];
        short[] bw1 = new short[N_SB];
        short[] bw2 = new short[N_SB];
        short[] bw3 = new short[N_SB];
        short[] bw4 = new short[N_SB];
        short[] bw5 = new short[N_SB];
        short[] bw6 = new short[N_SB];
        short[] bw7 = new short[N_SB];

        short[] w1 = new short[N_SB_RES];
        short[] w2 = new short[N_SB_RES];
        short[] w3 = new short[N_SB_RES];
        short[] w4 = new short[N_SB_RES];
        short[] w5 = new short[N_SB_RES];
        short[] w6 = new short[N_SB_RES];
        short[] w7 = new short[N_SB_RES];

        Arrays.fill(w1, (short) 0);
        Arrays.fill(w2, (short) 0);
        Arrays.fill(w3, (short) 0);
        Arrays.fill(w4, (short) 0);
        Arrays.fill(w5, (short) 0);
        Arrays.fill(w6, (short) 0);
        Arrays.fill(w7, (short) 0);

        short r0, r1, r2, r3, r4, r5, r6, r7;
        short[] A0;
        short[] A1;
        short[] A2;
        short[] A3;
        short[] B0;
        short[] B1;
        short[] B2;
        short[] B3;

        A0 = a;
        A1 = Arrays.copyOfRange(a,N_SB,a.length);
        A2 = Arrays.copyOfRange(a,2 *N_SB,a.length);
        A3 = Arrays.copyOfRange(a,3 *N_SB,a.length);

        B0 = b;
        B1 = Arrays.copyOfRange(b,N_SB,b.length);
        B2 = Arrays.copyOfRange(b,2*N_SB,b.length);
        B3 = Arrays.copyOfRange(b,3*N_SB,b.length);

        short[] C = c;

        for (int j = 0; j < N_SB; ++j) {
            r0 = A0[j];
            r1 = A1[j];
            r2 = A2[j];
            r3 = A3[j];
            r4 = (short) (r0 + r2);
            r5 = (short) (r1 + r3);
            r6 = (short) (r4 + r5);
            r7 = (short) (r4 - r5);
            aw3[j] = r6;
            aw4[j] = r7;
            r4 = (short) (((r0 << 2) + r2) << 1);
            r5 = (short) ((r1 << 2) + r3);
            r6 = (short) (r4 + r5);
            r7 = (short) (r4 - r5);
            aw5[j] = r6;
            aw6[j] = r7;
            r4 = (short) ((r3 << 3) + (r2 << 2) + (r1 << 1) + r0);
            aw2[j] = r4;
            aw7[j] = r0;
            aw1[j] = r3;
        }
        for (int j = 0; j < N_SB; ++j) {
            r0 = B0[j];
            r1 = B1[j];
            r2 = B2[j];
            r3 = B3[j];
            r4 = (short) (r0 + r2);
            r5 = (short) (r1 + r3);
            r6 = (short) (r4 + r5);
            r7 = (short) (r4 - r5);
            bw3[j] = r6;
            bw4[j] = r7;
            r4 = (short) (((r0 << 2) + r2) << 1);
            r5 = (short) ((r1 << 2) + r3);
            r6 = (short) (r4 + r5);
            r7 = (short) (r4 - r5);
            bw5[j] = r6;
            bw6[j] = r7;
            r4 = (short) ((r3 << 3) + (r2 << 2) + (r1 << 1) + r0);
            bw2[j] = r4;
            bw7[j] = r0;
            bw1[j] = r3;
        }

        w1 = karatsuba_simple(aw1, bw1, w1);
        w2 = karatsuba_simple(aw2, bw2, w2);
        w3 = karatsuba_simple(aw3, bw3, w3);
        w4 = karatsuba_simple(aw4, bw4, w4);
        w5 = karatsuba_simple(aw5, bw5, w5);
        w6 = karatsuba_simple(aw6, bw6, w6);
        w7 = karatsuba_simple(aw7, bw7, w7);

        for (int i = 0; i < N_SB_RES; ++i) {
            r0 = w1[i];
            r1 = w2[i];
            r2 = w3[i];
            r3 = w4[i];
            r4 = w5[i];
            r5 = w6[i];
            r6 = w7[i];

            r1 = (short) (r1 + r4);
            r5 = (short) (r5 - r4);
            r3 = (short) ((r3 - r2) >> 1);
            r4 = (short) (r4 - r0);
            r4 = (short) (r4 - (r6 << 6));
            r4 = (short) ((r4 << 1) + r5);
            r2 = (short) (r2 + r3);
            r1 = (short) (r1 - (r2 << 6) - r2);
            r2 = (short) (r2 - r6);
            r2 = (short) (r2 - r0);
            r1 = (short) (r1 + 45 * r2);
            r4 = (short) (((r4 - (r2 << 3)) * (int) inv3) >> 3);
            r5 = (short) (r5 + r1);
            r1 = (short) (((r1 + (r3 << 4)) * (int) inv9) >> 1);
            r3 = (short) -(r3 + r1);
            r5 = (short) (((30 * r1 - r5) * (int) inv15) >> 2);
            r2 = (short) (r2 - r4);
            r1 = (short) (r1 - r5);

            C[i] += r6;
            C[i + 64] += r5;
            C[i + 128] += r4;
            C[i + 192] += r3;
            C[i + 256] += r2;
            C[i + 320] += r1;
            C[i + 384] += r0;
        }
        return C;
    }

    static short[] karatsuba_simple(short[] a_1, short[] b_1, short[] result_final) {
        short[] d01 = new short[KARATSUBA_N / 2 - 1];
        short[] d0123 = new short[KARATSUBA_N / 2 - 1];
        short[] d23 = new short[KARATSUBA_N / 2 - 1];
        short[] result_d01 = new short[KARATSUBA_N - 1];

        int i, j;
        short acc1, acc2, acc3, acc4, acc5, acc6, acc7, acc8, acc9, acc10;

        for (i = 0; i < KARATSUBA_N / 4; i++) {
            acc1 = a_1[i]; //a0
            acc2 = a_1[i + KARATSUBA_N / 4]; //a1
            acc3 = a_1[i + 2 * KARATSUBA_N / 4]; //a2
            acc4 = a_1[i + 3 * KARATSUBA_N / 4]; //a3
            for (j = 0; j < KARATSUBA_N / 4; j++) {
                acc5 = b_1[j]; //b0
                acc6 = b_1[j + KARATSUBA_N / 4]; //b1

                result_final[i + j + 0 * KARATSUBA_N / 4] = (short) (result_final[i + j + 0 * KARATSUBA_N / 4] + OVERFLOWING_MUL(acc1, acc5));
                result_final[i + j + 2 * KARATSUBA_N / 4] = (short) (result_final[i + j + 2 * KARATSUBA_N / 4] + OVERFLOWING_MUL(acc2, acc6));

                acc7 = (short) (acc5 + acc6);
                acc8 = (short) (acc1 + acc2);
                d01[i + j] = (short) (d01[i + j] + (short) (acc7 * (short) acc8));

                acc7 = b_1[j + 2 * KARATSUBA_N / 4]; //b2
                acc8 = b_1[j + 3 * KARATSUBA_N / 4]; //b3
                result_final[i + j + 4 * KARATSUBA_N / 4] = (short) (result_final[i + j + 4 * KARATSUBA_N / 4] + OVERFLOWING_MUL(acc7, acc3));

                result_final[i + j + 6 * KARATSUBA_N / 4] = (short) (result_final[i + j + 6 * KARATSUBA_N / 4] + OVERFLOWING_MUL(acc8, acc4));

                acc9 = (short) (acc3 + acc4);
                acc10 = (short) (acc7 + acc8);
                d23[i + j] = (short) (d23[i + j] + OVERFLOWING_MUL(acc9, acc10));
                //--------------------------------------------------------

                acc5 = (short) (acc5 + acc7); //b02
                acc7 = (short) (acc1 + acc3); //a02
                result_d01[i + j + 0 * KARATSUBA_N / 4] = (short) (result_d01[i + j + 0 * KARATSUBA_N / 4] + OVERFLOWING_MUL(acc5, acc7));

                acc6 = (short) (acc6 + acc8); //b13
                acc8 = (short) (acc2 + acc4);
                result_d01[i + j + 2 * KARATSUBA_N / 4] = (short) (result_d01[i + j + 2 * KARATSUBA_N / 4] + OVERFLOWING_MUL(acc6, acc8));

                acc5 = (short) (acc5 + acc6);
                acc7 = (short) (acc7 + acc8);
                d0123[i + j] = (short) (d0123[i + j] + OVERFLOWING_MUL(acc5, acc7));


            }
        }

        for (i = 0; i < KARATSUBA_N / 2 - 1; i++) {
            d0123[i] = (short) (d0123[i] - result_d01[i + 0 * KARATSUBA_N / 4] - result_d01[i + 2 * KARATSUBA_N / 4]);
            d01[i] = (short) (d01[i] - result_final[i + 0 * KARATSUBA_N / 4] - result_final[i + 2 * KARATSUBA_N / 4]);
            d23[i] = (short) (d23[i] - result_final[i + 4 * KARATSUBA_N / 4] - result_final[i + 6 * KARATSUBA_N / 4]);
        }

        for (i = 0; i < KARATSUBA_N / 2 - 1; i++) {
            result_d01[i + 1 * KARATSUBA_N / 4] = (short) (result_d01[i + 1 * KARATSUBA_N / 4] + d0123[i]);
            result_final[i + 1 * KARATSUBA_N / 4] = (short) (result_final[i + 1 * KARATSUBA_N / 4] + d01[i]);
            result_final[i + 5 * KARATSUBA_N / 4] = (short) (result_final[i + 5 * KARATSUBA_N / 4] + d23[i]);
        }

        // Last stage
        for (i = 0; i < KARATSUBA_N - 1; i++) {
            result_d01[i] = (short) (result_d01[i] - result_final[i] - result_final[i + KARATSUBA_N]);
        }

        for (i = 0; i < KARATSUBA_N - 1; i++) {
            result_final[i + 1 * KARATSUBA_N / 2] = (short) (result_final[i + 1 * KARATSUBA_N / 2] + result_d01[i]);
        }
        return result_final;
    }

    public static short OVERFLOWING_MUL(short x, short y) {
        return (short) ((int) (x) * (int) (y));
    }

    public static short[] poly_mul_acc(short[] a, short[] b, short[] res) {
        short[] c = new short[2 * SABER_N];
        Arrays.fill(c, (short) 0);

        c = toom_cook_4way(a, b, c);
        for (int i = SABER_N; i < 2 * SABER_N; i++) {
            res[i - SABER_N] += (c[i - SABER_N] - c[i]);
        }
        return res;
    }

}
