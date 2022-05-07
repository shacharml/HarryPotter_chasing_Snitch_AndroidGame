package com.example.harrypotter_chasing_snitch_androidgame.logic;

import static com.example.harrypotter_chasing_snitch_androidgame.fragments.Fragment_List.NUM_OF_PLAYERS;

import java.util.ArrayList;

public class Data {

    private ArrayList<Record> records;

    public Data() { };

    public ArrayList<Record> getRecords() {
        if(records == null){
            records = new ArrayList<Record>();
        }
        return records;
    }

    public Data setRecords(ArrayList<Record> records) {
        this.records = records;
        return this;
    }

    public void cleanUp(){
        if (records.size()>NUM_OF_PLAYERS)
            for (int j =NUM_OF_PLAYERS ; j < records.size() ; j++)
                records.remove(j);
//        records.clear();
    }
}
