package com.example.harrypotter_chasing_snitch_androidgame.logic;

// Builder
public class Record {

    private String name;
    private int score = 0;
    private double lat = 0.0;
    private double lng = 0.0;
    private String time ;

    public Record() { }

    public String getName() {
        return name;
    }

    public Record setName(String name) {
        this.name = name;
        return this;
    }

    public String getTime() {
        return time;
    }

    public Record setTime(String time) {
        this.time = time;
        return this;
    }
    public int getScore() {
        return score;
    }

    public Record setScore(int score) {
        this.score = score;
        return this;
    }
    public double getLat() {
        return lat;
    }

    public Record setLat(double lat) {
        this.lat = lat;
        return this;
    }

    public double getLng() {
        return lng;
    }

    public Record setLng(double lng) {
        this.lng = lng;
        return this;
    }

}