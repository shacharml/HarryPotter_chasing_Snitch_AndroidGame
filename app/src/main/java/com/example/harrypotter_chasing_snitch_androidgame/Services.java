package com.example.harrypotter_chasing_snitch_androidgame;



import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

import androidx.appcompat.app.AppCompatActivity;

public class Services {
    AppCompatActivity appCompatActivity;

    public Services() {
    }
    public Services(AppCompatActivity activity) {
        this.appCompatActivity = activity;
    }

    public void doVibrate(){
        Vibrator v = (Vibrator) appCompatActivity.getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(500);
        }
    }
}
