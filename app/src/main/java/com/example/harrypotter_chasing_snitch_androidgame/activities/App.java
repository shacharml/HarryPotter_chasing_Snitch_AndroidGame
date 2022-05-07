package com.example.harrypotter_chasing_snitch_androidgame.activities;

import android.app.Application;

import com.example.harrypotter_chasing_snitch_androidgame.logic.Data;
import com.example.harrypotter_chasing_snitch_androidgame.utilities.GameUtils;
import com.example.harrypotter_chasing_snitch_androidgame.utilities.MSP;
import com.google.gson.Gson;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        GameUtils.initHelper(this);
        MSP.initHelper(this);
        String js = MSP.getMe().getString("MY_RECORDS", "");
        Data fromJson = new Gson().fromJson(js, Data.class);

        if(fromJson == null){
            Data data = new Data();
            String json = new Gson().toJson(data);
            MSP.getMe().putString("MY_RECORDS", json);
        }
    }
}
