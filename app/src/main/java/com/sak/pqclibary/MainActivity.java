package com.sak.pqclibary;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.sak.pqclibrary.PQCLibary;
import com.sak.pqclibrary.ParameterSets;
import com.sak.pqclibrary.saber.models.EncapsulationModel;
import com.sak.pqclibrary.saber.utils.Utils;

import java.lang.reflect.Array;
import java.lang.reflect.Parameter;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        PQCLibary pqc = new PQCLibary(ParameterSets.FIRE_SABER);
        EncapsulationModel enc = pqc.Encapsulation(pqc.getPk());
        byte[] sharedSecretKey = pqc.Decapsulation(enc.getCipherText(),pqc.getSk());


        Log.d("AFD-AFD", Utils.hex(sharedSecretKey));
        Log.d("AFD-AFD", Utils.hex(enc.getSs()));
        Log.d("AFD-AFD", Arrays.equals(sharedSecretKey, enc.getSs()) + "");



    }



}