package com.sak.pqclibary;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Trace;
import android.util.Log;
import android.view.View;

import com.sak.pqclibrary.PQCLibary;
import com.sak.pqclibrary.ParameterSets;
import com.sak.pqclibrary.saber.models.EncapsulationModel;
import com.sak.pqclibrary.saber.utils.Utils;

import java.lang.reflect.Array;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    public static int NTESTS = 100;
    public static int C = 0;

    PQCLibary pqc;
    EncapsulationModel enc;
    byte[] sharedSecretKey;

    long start,end,e;
    long[] array = new long[NTESTS];
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.submit(new Runnable() {
                    public void run() {
                        for (int i = 0 ; i < NTESTS + C ;  i++){

                            if(i < C){
                                pqc = new PQCLibary(ParameterSets.LIGHT_SABER);
                            }
                            else{
                                start = System.nanoTime();
                                pqc = new PQCLibary(ParameterSets.LIGHT_SABER);
                                end = System.nanoTime();

                                e  = end - start;
                                array[i - C] = e;
                            }
                        }
                    }
                });
                Log.d("AFD-AFD keyGen: " , avg(array)/1000 + "");

            }
        });
        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for (int i = 0 ; i < NTESTS + C ;  i++){
                    if(i < C){
                        enc = pqc.encapsulation(pqc.getPk());
                    }
                    else{
                        start = System.nanoTime();
                        enc = pqc.encapsulation(pqc.getPk());
                        end = System.nanoTime();

                        e  = end - start;
                        array[i - C] = e;
                    }

                }
                Log.d("AFD-AFD Encapsulation: " , avg(array)/1000  + "");
            }
        });


        findViewById(R.id.btn3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0 ; i < NTESTS + C ;  i++){
                    if(i < C){
                        sharedSecretKey = pqc.decapsulation(enc.getCipherText(),pqc.getSk());
                    }
                    else{
                        start = System.nanoTime();
                        sharedSecretKey = pqc.decapsulation(enc.getCipherText(),pqc.getSk());
                        end = System.nanoTime();

                        e  = end - start;
                        array[i - C] = e;
                    }

                }
                Log.d("AFD-AFD Decapsulation: " , avg(array)/1000 + "");
            }
        });

    }

    public static double avg(long[] array){
        int sum = 0;
        for (long l : array) {
            sum += l;
        }
        return (double) sum / array.length;
    }
}