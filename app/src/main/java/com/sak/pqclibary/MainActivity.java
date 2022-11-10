package com.sak.pqclibary;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.sak.pqclibrary.PQCLibary;
import com.sak.pqclibrary.saber.models.EncapsulationModel;
import com.sak.pqclibrary.saber.utils.Utils;

public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PQCLibary pqcLibary = new PQCLibary("Saber_Light");
        EncapsulationModel enc = pqcLibary.Encapsulation(pqcLibary.getPk());
        byte[] sharedSecretKey = pqcLibary.Decapsulation(enc.getCipherText(),pqcLibary.getSk());
        Log.d("AFD-AFD", Utils.hex(sharedSecretKey));
        Log.d("AFD-AFD", Utils.hex(enc.getSs()));



    }



}