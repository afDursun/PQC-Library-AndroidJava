package com.sak.pqclibrary.ntru;

import com.sak.pqclibrary.saber.models.EncapsulationModel;
import com.sak.pqclibrary.saber.models.Keys;

public class NTRUKem {
    public static Keys crypto_kem_keypair(){
        return OWCPA.owcpa_keypair();
    }
    public static EncapsulationModel crypto_kem_enc(byte[] pk){
        return OWCPA.crypto_kem_enc(pk);
    }
    public static byte[] crypto_kem_dec(byte[] c, byte[] sk){
        return OWCPA.crypto_kem_dec(c, sk);
    }
}
