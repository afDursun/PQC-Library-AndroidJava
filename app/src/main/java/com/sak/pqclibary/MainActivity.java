package com.sak.pqclibary;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.sak.pqclibary.kyber.kyberencryption.provider.Kyber1024KeyPairGenerator;
import com.sak.pqclibary.kyber.kyberencryption.provider.KyberKeyPair;
import com.sak.pqclibary.kyber.kyberencryption.provider.KyberProcess;
import com.sak.pqclibary.kyber.kyberencryption.provider.KyberSecretKey;
import com.sak.pqclibary.kyber.kyberencryption.provider.kyber.KyberParams;
import com.sak.pqclibary.saber.Kem;
import com.sak.pqclibary.saber.models.EncapsulationModel;
import com.sak.pqclibary.saber.models.Keys;
import com.sak.pqclibary.saber.utils.Utils;

public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PQCLibary pqcLibary = new PQCLibary("Kyber_1024");
        EncapsulationModel enc = pqcLibary.Encapsulation(pqcLibary.pk);
        byte[] sharedSecretKey = pqcLibary.Decapsulation(enc.getCipherText(),pqcLibary.sk);
        Log.d("AFD-AFD", Utils.hex(sharedSecretKey));



    }



}