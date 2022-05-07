package com.example.harrypotter_chasing_snitch_androidgame.logic;

import java.util.Comparator;

public class sortScore implements Comparator<Record> {

    @Override
    public int compare(Record record, Record t1) {
        return t1.getScore() - record.getScore();
    }
}
