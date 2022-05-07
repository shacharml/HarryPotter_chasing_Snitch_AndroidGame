package com.example.harrypotter_chasing_snitch_androidgame.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.harrypotter_chasing_snitch_androidgame.R;
import com.google.android.material.button.MaterialButton;

public class MenuActivity extends AppCompatActivity {

    public static final String SENSOR_MODE = "SENSOR_MODE";
    public static final String REGULAR_MODE = "REGULAR_MODE";
    private MaterialButton menu_BTN_play;
    private MaterialButton menu_BTN_playSensor;
    private MaterialButton menu_BTN_top_ten;
    private EditText menu_EDT_name;
    private ImageButton menu_BTN_next;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        findViews();
        initButtons();
    }

    private void initButtons() {
        menu_BTN_next.setOnClickListener(view -> setVisibilityButtons());
        menu_BTN_play.setOnClickListener(view -> moveToGameActivity(REGULAR_MODE));
        menu_BTN_playSensor.setOnClickListener(view -> moveToGameActivity(SENSOR_MODE));
        menu_BTN_top_ten.setOnClickListener(view -> moveToTopTenActivity());
    }

    private void setVisibilityButtons() {
        // save the user name that was enter
        userName = menu_EDT_name.getText().toString();
        //  hide the user name an next button
        menu_EDT_name.setVisibility(View.GONE);
        menu_BTN_next.setVisibility(View.GONE);
        //set visible the play buttons
        menu_BTN_play.setVisibility(View.VISIBLE);
        menu_BTN_playSensor.setVisibility(View.VISIBLE);
        menu_BTN_top_ten.setVisibility(View.VISIBLE);
    }

    private void moveToTopTenActivity() {
        Intent intent = new Intent(this, TopTenActivity.class);
        Bundle bundle = new Bundle();
        //if i moved from game to Top10 or from menu
        bundle.putBoolean("isFromGame", false);
        bundle.putString("userName", userName);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void moveToGameActivity(String modeGame) {
        Intent intent = new Intent(this, GameActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("userName", userName);
        // witch game mode i want to play
        bundle.putString("modeGame", modeGame);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void findViews() {
        menu_BTN_play = findViewById(R.id.menu_BTN_play);
        menu_BTN_top_ten = findViewById(R.id.menu_BTN_top_ten);
        menu_BTN_next = findViewById(R.id.menu_BTN_next);
        menu_EDT_name = findViewById(R.id.menu_EDT_name);
        menu_BTN_playSensor = findViewById(R.id.menu_BTN_playSensor);
    }
}