package com.sak.pqclibary.saber;

import static com.sak.pqclibary.saber.Params.SABER_EP;
import static com.sak.pqclibary.saber.Params.SABER_ET;
import static com.sak.pqclibary.saber.Params.SABER_INDCPA_SECRETKEYBYTES;
import static com.sak.pqclibary.saber.Params.SABER_KEYBYTES;
import static com.sak.pqclibary.saber.Params.SABER_L;
import static com.sak.pqclibary.saber.Params.SABER_N;
import static com.sak.pqclibary.saber.Params.SABER_POLYBYTES;
import static com.sak.pqclibary.saber.Params.SABER_POLYVECCOMPRESSEDBYTES;
import static com.sak.pqclibary.saber.Params.SABER_SCALEBYTES_KEM;

import java.util.Arrays;

public class PackUnpack {

    public static short[][] BS2POLVECq(byte[] bytes, short[][] A) {
        short[][] retValue = new short[SABER_L][SABER_N];
        for (int i = 0; i < SABER_L; i++) {

            retValue[i] = BS2POLq(Arrays.copyOfRange(bytes,i*SABER_POLYBYTES,bytes.length), A[i]);
        }
        return retValue;
    }

    public static short[] BS2POLq(byte[] bytes, short[] data) {
        int j, offset_byte, offset_data;
        for (j = 0; j < SABER_N / 8; j++) {
            offset_byte = 13 * j;
            offset_data = 8 * j;
            data[offset_data + 0] = (short) ((bytes[offset_byte + 0] & (0xff)) | ((bytes[offset_byte + 1] & 0x1f) << 8));
            data[offset_data + 1] = (short) ((bytes[offset_byte + 1] >> 5 & (0x07)) | ((bytes[offset_byte + 2] & 0xff) << 3) | ((bytes[offset_byte + 3] & 0x03) << 11));
            data[offset_data + 2] = (short) ((bytes[offset_byte + 3] >> 2 & (0x3f)) | ((bytes[offset_byte + 4] & 0x7f) << 6));
            data[offset_data + 3] = (short) ((bytes[offset_byte + 4] >> 7 & (0x01)) | ((bytes[offset_byte + 5] & 0xff) << 1) | ((bytes[offset_byte + 6] & 0x0f) << 9));
            data[offset_data + 4] = (short) ((bytes[offset_byte + 6] >> 4 & (0x0f)) | ((bytes[offset_byte + 7] & 0xff) << 4) | ((bytes[offset_byte + 8] & 0x01) << 12));
            data[offset_data + 5] = (short) ((bytes[offset_byte + 8] >> 1 & (0x7f)) | ((bytes[offset_byte + 9] & 0x3f) << 7));
            data[offset_data + 6] = (short) ((bytes[offset_byte + 9] >> 6 & (0x03)) | ((bytes[offset_byte + 10] & 0xff) << 2) | ((bytes[offset_byte + 11] & 0x07) << 10));
            data[offset_data + 7] = (short) ((bytes[offset_byte + 11] >> 3 & (0x1f)) | ((bytes[offset_byte + 12] & 0xff) << 5));
        }
        return data;
    }

    public static byte[] POLVECq2BS(byte[] bytes, short[][] data) {
        byte[] retValue = new byte[SABER_INDCPA_SECRETKEYBYTES];
        for (int i = 0; i < SABER_L; i++) {
            short[] datas = data[i];
            int j, offset_byte, offset_data;
            for (j = 0; j < SABER_N / 8; j++) {
                offset_byte = 13 * j;
                offset_data = 8 * j;
                bytes[offset_byte + 0] = (byte) (datas[offset_data + 0] & (0xff));
                bytes[offset_byte + 1] = (byte) (((datas[offset_data + 0] >> 8) & 0x1f) | ((datas[offset_data + 1] & 0x07) << 5));
                bytes[offset_byte + 2] = (byte) ((datas[offset_data + 1] >> 3) & 0xff);
                bytes[offset_byte + 3] = (byte) (((datas[offset_data + 1] >> 11) & 0x03) | ((datas[offset_data + 2] & 0x3f) << 2));
                bytes[offset_byte + 4] = (byte) (((datas[offset_data + 2] >> 6) & 0x7f) | ((datas[offset_data + 3] & 0x01) << 7));
                bytes[offset_byte + 5] = (byte) ((datas[offset_data + 3] >> 1) & 0xff);
                bytes[offset_byte + 6] = (byte) (((datas[offset_data + 3] >> 9) & 0x0f) | ((datas[offset_data + 4] & 0x0f) << 4));
                bytes[offset_byte + 7] = (byte) ((datas[offset_data + 4] >> 4) & 0xff);
                bytes[offset_byte + 8] = (byte) (((datas[offset_data + 4] >> 12) & 0x01) | ((datas[offset_data + 5] & 0x7f) << 1));
                bytes[offset_byte + 9] = (byte) (((datas[offset_data + 5] >> 7) & 0x3f) | ((datas[offset_data + 6] & 0x03) << 6));
                bytes[offset_byte + 10] = (byte) ((datas[offset_data + 6] >> 2) & 0xff);
                bytes[offset_byte + 11] = (byte) (((datas[offset_data + 6] >> 10) & 0x07) | ((datas[offset_data + 7] & 0x1f) << 3));
                bytes[offset_byte + 12] = (byte) ((datas[offset_data + 7] >> 5) & 0xff);
            }

            switch (i) {
                case 0:
                    System.arraycopy(bytes, 0, retValue, 0, 416);
                    break;
                case 1:
                    System.arraycopy(bytes, 0, retValue, 416, 416);
                    break;
                case 2:
                    System.arraycopy(bytes, 0, retValue, 832, 416);
                    break;
            }
        }
        return retValue;
    }

    public static byte[] POLVECp2BS(byte[] bytes, short[][] data) {
        byte[] retValue = new byte[SABER_POLYVECCOMPRESSEDBYTES];
        for (int i = 0; i < SABER_L; i++) {
            short[] datas = data[i];
            int j, offset_byte, offset_data;
            for (j = 0; j < SABER_N / 4; j++) {
                offset_byte = 5 * j;
                offset_data = 4 * j;
                bytes[offset_byte + 0] = (byte) (datas[offset_data + 0] & (0xff));
                bytes[offset_byte + 1] = (byte) (((datas[offset_data + 0] >> 8) & 0x03) | ((datas[offset_data + 1] & 0x3f) << 2));
                bytes[offset_byte + 2] = (byte) (((datas[offset_data + 1] >> 6) & 0x0f) | ((datas[offset_data + 2] & 0x0f) << 4));
                bytes[offset_byte + 3] = (byte) (((datas[offset_data + 2] >> 4) & 0x3f) | ((datas[offset_data + 3] & 0x03) << 6));
                bytes[offset_byte + 4] = (byte) ((datas[offset_data + 3] >> 2) & 0xff);
            }
            switch (i) {
                case 0:
                    System.arraycopy(bytes, 0, retValue, 0, 320);
                    break;
                case 1:
                    System.arraycopy(bytes, 0, retValue, 320, 320);
                    break;
                case 2:
                    System.arraycopy(bytes, 0, retValue, 640, 320);
                    break;
            }
        }
        return retValue;
    }



    public static short[][] BS2POLVECp(byte[] pk) {
        short[][] retValue = new short[SABER_L][SABER_N];
        for (int i = 0; i < SABER_L; i++)
        {

            retValue[i] = BS2POLp(Arrays.copyOfRange(pk,i * (SABER_EP * SABER_N / 8),pk.length));
        }
        return  retValue;
    }

    private static short[] BS2POLp(byte[] bytes) {
        short[] data = new short[SABER_N] ;
        int j, offset_byte, offset_data;
        for (j = 0; j < SABER_N / 4; j++)
        {
            offset_byte = 5 * j;
            offset_data = 4 * j;
            data[offset_data + 0] = (short) ((bytes[offset_byte + 0] & (0xff)) | ((bytes[offset_byte + 1] & 0x03) << 8));
            data[offset_data + 1] = (short) (((bytes[offset_byte + 1] >> 2) & (0x3f)) | ((bytes[offset_byte + 2] & 0x0f) << 6));
            data[offset_data + 2] = (short) (((bytes[offset_byte + 2] >> 4) & (0x0f)) | ((bytes[offset_byte + 3] & 0x3f) << 4));
            data[offset_data + 3] = (short) (((bytes[offset_byte + 3] >> 6) & (0x03)) | ((bytes[offset_byte + 4] & 0xff) << 2));
        }
        return data;
    }

    public static short[] BS2POLmsg(byte[] bytes) {
        short[] retValue = new short[SABER_N];
        int i, j;
        for (j = 0; j < SABER_KEYBYTES; j++)
        {
            for (i = 0; i < 8; i++)
            {
                retValue[j * 8 + i] = (short) ((bytes[j] >> i) & 0x01);
            }
        }

        return retValue;
    }

    public static byte[] POLT2BS(short[] data) {
        byte[] bytes = new byte[SABER_SCALEBYTES_KEM];
        int j, offset_byte, offset_data;
        if(SABER_ET == 3){
            for (j = 0; j < SABER_N / 8; j++)
            {
                offset_byte = 3 * j;
                offset_data = 8 * j;
                bytes[offset_byte + 0] = (byte) ((data[offset_data + 0] & 0x7) | ((data[offset_data + 1] & 0x7) << 3) | ((data[offset_data + 2] & 0x3) << 6));
                bytes[offset_byte + 1] = (byte) (((data[offset_data + 2] >> 2) & 0x01) | ((data[offset_data + 3] & 0x7) << 1) | ((data[offset_data + 4] & 0x7) << 4) | (((data[offset_data + 5]) & 0x01) << 7));
                bytes[offset_byte + 2] = (byte) (((data[offset_data + 5] >> 1) & 0x03) | ((data[offset_data + 6] & 0x7) << 2) | ((data[offset_data + 7] & 0x7) << 5));
            }
        }else if(SABER_ET == 4){
            for (j = 0; j < SABER_N / 2; j++)
            {
                offset_byte = j;
                offset_data = 2 * j;
                bytes[offset_byte] = (byte) ((data[offset_data] & 0x0f) | ((data[offset_data + 1] & 0x0f) << 4));
            }
        }else{
            for (j = 0; j < SABER_N / 4; j++)
            {
                offset_byte = 3 * j;
                offset_data = 4 * j;
                bytes[offset_byte + 0] = (byte) ((data[offset_data + 0] & 0x3f) | ((data[offset_data + 1] & 0x03) << 6));
                bytes[offset_byte + 1] = (byte) (((data[offset_data + 1] >> 2) & 0x0f) | ((data[offset_data + 2] & 0x0f) << 4));
                bytes[offset_byte + 2] = (byte) (((data[offset_data + 2] >> 4) & 0x03) | ((data[offset_data + 3] & 0x3f) << 2));
            }
        }

        return bytes;
    }

    public static short[] BS2POLT(byte[] bytes) {
        short[] data = new short[SABER_N];
        int j, offset_byte, offset_data;
        if( SABER_ET == 3){
            for (j = 0; j < SABER_N / 8; j++)
            {
                offset_byte = 3 * j;
                offset_data = 8 * j;
                data[offset_data + 0] = (short) ((bytes[offset_byte + 0]) & 0x07);
                data[offset_data + 1] = (short) (((bytes[offset_byte + 0]) >> 3) & 0x07);
                data[offset_data + 2] = (short) ((((bytes[offset_byte + 0]) >> 6) & 0x03) | (((bytes[offset_byte + 1]) & 0x01) << 2));
                data[offset_data + 3] = (short) (((bytes[offset_byte + 1]) >> 1) & 0x07);
                data[offset_data + 4] = (short) (((bytes[offset_byte + 1]) >> 4) & 0x07);
                data[offset_data + 5] = (short) ((((bytes[offset_byte + 1]) >> 7) & 0x01) | (((bytes[offset_byte + 2]) & 0x03) << 1));
                data[offset_data + 6] = (short) ((bytes[offset_byte + 2] >> 2) & 0x07);
                data[offset_data + 7] = (short) ((bytes[offset_byte + 2] >> 5) & 0x07);
            }
        }else if(SABER_ET == 4){
            for (j = 0; j < SABER_N / 2; j++)
            {
                offset_byte = j;
                offset_data = 2 * j;
                data[offset_data] = (short) (bytes[offset_byte] & 0x0f);
                data[offset_data + 1] = (short) ((bytes[offset_byte] >> 4) & 0x0f);
            }
        }else{
            for (j = 0; j < SABER_N / 4; j++)
            {
                offset_byte = 3 * j;
                offset_data = 4 * j;
                data[offset_data + 0] = (short) (bytes[offset_byte + 0] & 0x3f);
                data[offset_data + 1] = (short) (((bytes[offset_byte + 0] >> 6) & 0x03) | ((bytes[offset_byte + 1] & 0x0f) << 2));
                data[offset_data + 2] = (short) (((bytes[offset_byte + 1] & 0xff) >> 4) | ((bytes[offset_byte + 2] & 0x03) << 4));
                data[offset_data + 3] = (short) ((bytes[offset_byte + 2] & 0xff) >> 2);
            }
        }

        return data;
    }

    public static byte[] POLmsg2BS(short[] data) {
        byte[] bytes = new byte[32];
        int i, j;

        for (j = 0; j < SABER_KEYBYTES; j++)
        {
            for (i = 0; i < 8; i++)
            {
                bytes[j] = (byte) (bytes[j] | ((data[j * 8 + i] & 0x01) << i));
            }
        }
        return bytes;
    }
}
