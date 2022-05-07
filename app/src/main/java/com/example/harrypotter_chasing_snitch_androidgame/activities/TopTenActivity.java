package com.example.harrypotter_chasing_snitch_androidgame.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.harrypotter_chasing_snitch_androidgame.R;
import com.example.harrypotter_chasing_snitch_androidgame.callBacks.CallBack_ShowLocation;
import com.example.harrypotter_chasing_snitch_androidgame.fragments.Fragment_List;
import com.example.harrypotter_chasing_snitch_androidgame.fragments.Fragment_Map;
import com.google.android.material.button.MaterialButton;

public class TopTenActivity extends AppCompatActivity {
    //views
    private ImageButton top_ten_BTN_back;
    private MaterialButton top_ten_BTN_play;
    //Fragments
    private Fragment_List fragment_list;
    private Fragment_Map fragment_map;

    private String modeGame, userName;

    private final CallBack_ShowLocation callBack_showLocation = new CallBack_ShowLocation() {
        @Override
        public void showLocationClicked(double lng, double lat, String playerName) {
            fragment_map.getLocation(lat, lng, playerName);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_ten);
        findViews();
        initFragments();
        initButtons();

        //if doesn't get to top 10 from the game - cant do a play again only back
        if (!getIntent().getExtras().getBoolean("isFromGame"))
            top_ten_BTN_play.setVisibility(View.INVISIBLE);
        else {
            modeGame = getIntent().getExtras().getString("gameMode");
            userName = getIntent().getExtras().getString("userName");
        }
    }

    private void initButtons() {
        top_ten_BTN_back.setOnClickListener(view -> onBackPressed());
        top_ten_BTN_play.setOnClickListener(view -> newPlayAgain());
    }

    private void newPlayAgain() {
        Intent intent = new Intent(this, GameActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("userName", userName);
        // witch game mode i want to play
        bundle.putString("modeGame", modeGame);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void initFragments() {
        //init List fragment
        fragment_list = new Fragment_List();
        fragment_list.setActivity(this);
        fragment_list.setCallBack_ShowLocation(callBack_showLocation);
        getSupportFragmentManager().beginTransaction().add(R.id.top_ten_LAY_list, fragment_list).commit();

        //init Map fragment
        fragment_map = new Fragment_Map();
        getSupportFragmentManager().beginTransaction().add(R.id.top_ten_LAY_map, fragment_map).commit();
    }

    private void findViews() {
        top_ten_BTN_back = findViewById(R.id.top_ten_BTN_back);
        top_ten_BTN_play = findViewById(R.id.menu_BTN_play);
    }
}
