package com.example.harrypotter_chasing_snitch_androidgame.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.harrypotter_chasing_snitch_androidgame.R;
import com.example.harrypotter_chasing_snitch_androidgame.callBacks.CallBack_ShowLocation;
import com.example.harrypotter_chasing_snitch_androidgame.logic.Data;
import com.example.harrypotter_chasing_snitch_androidgame.logic.Record;
import com.example.harrypotter_chasing_snitch_androidgame.utilities.MSP;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;

import java.util.ArrayList;

public class Fragment_List extends Fragment {

    public static final int NUM_OF_PLAYERS = 10;
    private AppCompatActivity activity;
    //List of all the top ten records
    private ArrayList<Record> allRecords;
    // views
    private final ArrayList<MaterialTextView> allNames;
    private final ArrayList<MaterialTextView> allDates;
    private final ArrayList<MaterialTextView> allScores;
    private final ArrayList<ImageButton> allLocations;
    //callBack
    private CallBack_ShowLocation callBack_showLocation;

    public Fragment_List() {
        allNames = new ArrayList<>();
        allDates = new ArrayList<>();
        allScores = new ArrayList<>();
        allLocations = new ArrayList<>();
    }

    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void setCallBack_ShowLocation(CallBack_ShowLocation callBack_showLocation) {
        this.callBack_showLocation = callBack_showLocation;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        findViews(view);
        initViews();
        return view;
    }

    private void initViews() {
        //get all the data from the MSP file
        String jason = MSP.getMe().getString("MY_RECORDS", "");
        Data data = new Gson().fromJson(jason, Data.class);
        allRecords = data.getRecords(); // save the data at allRecords
        //run on the size of records - if less then 10 show less
        for (int i = 0; i < allRecords.size() && i < NUM_OF_PLAYERS; i++) {
            Record temp = allRecords.get(i);
            allNames.get(i).setText(temp.getName());
            allScores.get(i).setText("" + temp.getScore());
            allDates.get(i).setText(temp.getTime());
            allLocations.get(i).setVisibility(View.VISIBLE); //showing the button
            allLocations.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callBack_showLocation.showLocationClicked(temp.getLng(), temp.getLat(), temp.getName());
                }
            });
        }

    }

    private void findViews(View view) {
        for (int i = 0; i < NUM_OF_PLAYERS; i++) {
            allNames.add(view.findViewById(view.getResources().getIdentifier("fragment_LBL_name_" + i, "id", getActivity().getPackageName())));
            allDates.add(view.findViewById(view.getResources().getIdentifier("fragment_LBL_date_" + i, "id", getActivity().getPackageName())));
            allScores.add(view.findViewById(view.getResources().getIdentifier("fragment_LBL_score_" + i, "id", getActivity().getPackageName())));
            allLocations.add(view.findViewById(view.getResources().getIdentifier("fragment_BTN_pin_" + i, "id", getActivity().getPackageName())));
        }
    }

}
