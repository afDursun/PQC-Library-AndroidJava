package com.sak.pqclibary;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.sak.pqclibary.kyber.kyberencryption.provider.Kyber1024KeyPairGenerator;
import com.sak.pqclibary.kyber.kyberencryption.provider.Kyber512KeyPairGenerator;
import com.sak.pqclibary.kyber.kyberencryption.provider.Kyber768KeyPairGenerator;
import com.sak.pqclibary.kyber.kyberencryption.provider.KyberKeyPair;
import com.sak.pqclibary.kyber.kyberencryption.provider.KyberProcess;
import com.sak.pqclibary.kyber.kyberencryption.provider.kyber.KyberParams;
import com.sak.pqclibary.ntru.OWCPA;
import com.sak.pqclibary.saber.Kem;
import com.sak.pqclibary.saber.Params;
import com.sak.pqclibary.saber.models.EncapsulationModel;
import com.sak.pqclibary.saber.models.Keys;

public class PQCLibary {
    byte[] pk;
    byte[] sk;
    byte[] cipherText;
    byte[] ssk1;
    byte[] rnd1 = new byte[KyberParams.paramsSymBytes];
    int algorithmType;
    @RequiresApi(api = Build.VERSION_CODES.O)
    public PQCLibary(String algorithmName) {
        String[] a = algorithmName.split("_");
        Keys keys = null;

        if (a.length==1){
            this.algorithmType= 1;
            new Params(3,8,4);
            keys = Kem.crypto_kem_keypair();
        }
        else if(a.length == 2){
            if(a[0].equalsIgnoreCase("Saber")){
                if(a[1].equalsIgnoreCase("Light")){

                    new Params(2,10,3);
                }
                else if(a[1].equalsIgnoreCase("Fire")){
                    new Params(4,6,6);
                }
                this.algorithmType = 1;
                keys = Kem.crypto_kem_keypair();
            }
            else if(a[0].equalsIgnoreCase("NTRU")){
                if(a[1].equalsIgnoreCase("509")){

                    new com.sak.pqclibary.ntru.Params(509);

                }
                else if(a[1].equalsIgnoreCase("677")){
                    new com.sak.pqclibary.ntru.Params(677);
                }
                else if(a[1].equalsIgnoreCase("821")){
                    new com.sak.pqclibary.ntru.Params(821);
                }
                this.algorithmType = 2;
                keys = OWCPA.owcpa_keypair();
            }

            else if(a[0].equalsIgnoreCase("Kyber")){
                KyberKeyPair kk  =null;

                if(a[1].equalsIgnoreCase("512")){
                    kk  = Kyber512KeyPairGenerator.generateKeys512();
                    this.algorithmType = 3;
                }
                else if(a[1].equalsIgnoreCase("768")){
                    kk  = Kyber768KeyPairGenerator.generateKeys768();
                    this.algorithmType = 4;
                }
                else if(a[1].equalsIgnoreCase("1024")){
                    kk  = Kyber1024KeyPairGenerator.generateKeys1024();
                    this.algorithmType = 5;
                }
                keys = new Keys(kk.getKyberPublicKey(),kk.getKyberPrivateKey());
            }
        }
        this.pk = keys.getPk();
        this.sk = keys.getSk();
    }

    public EncapsulationModel Encapsulation(byte[] pk){
        EncapsulationModel enc;
        switch (algorithmType){
            case 1:
                enc = Kem.crypto_kem_enc(pk);
                break;
            case 2:
                enc = OWCPA.crypto_kem_enc(pk);
                break;
            case 3:
                enc = new EncapsulationModel(KyberProcess.encrypt512(rnd1,pk).getCipherText(),KyberProcess.encrypt512(rnd1,pk).getSecretKey());
                break;
            case 4:
                enc = new EncapsulationModel(KyberProcess.encrypt768(rnd1,pk).getCipherText(),KyberProcess.encrypt768(rnd1,pk).getSecretKey());
                break;
            case 5:
                enc = new EncapsulationModel(KyberProcess.encrypt1024(rnd1,pk).getCipherText(),KyberProcess.encrypt1024(rnd1,pk).getSecretKey());
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + algorithmType);
        }
        return new EncapsulationModel(enc.getCipherText(),enc.getSs());
    }



    public byte[] Decapsulation(byte[] ct , byte[] sk){
        byte[] ssk2 ;
        switch (algorithmType){
            case 1:
                ssk2 = Kem.crypto_kem_dec(sk,ct);
                break;
            case 2 :
                ssk2 = OWCPA.crypto_kem_dec(ct,sk);
                break;
            case 3 :
                ssk2 = KyberProcess.decrypt512(ct,sk);
                break;
            case 4 :
                ssk2 = KyberProcess.decrypt768(ct,sk);
                break;
            case 5 :
                ssk2 = KyberProcess.decrypt1024(ct,sk);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + algorithmType);
        }
        return ssk2;
    }
}
