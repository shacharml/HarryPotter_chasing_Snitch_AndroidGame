package com.example.harrypotter_chasing_snitch_androidgame.logic;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GameManager {

    private String userName;
    private int score ;
    private Record record;
    private int live ;

    //----- Constructors ------
     public GameManager(int score, int live) {
        this.score = score;
        this.live = live;
         record = new Record();
     }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public void addScoreEveryTick(){
        score++;
    }
    public void collectMagicWand(){
        score += 10;
    }
    public void reduceLives() {
        live--;
    }
    public boolean isDead() {
        return live <= 0;
    }
    public void initialScore(){
        score = 0;
    }
    public int getScore() {
        return score;
    }
    public int getLive() {
        return live;
    }
    public Record getRecord() {
        return record;
    }
    public void setRecord(Record record) {
        this.record = record;
    }
    /**
     *
     * @return yyyy-MM-dd HH:mm:ss formate date as string
     */
    public static String getCurrentTimeStamp(){
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentDateTime = dateFormat.format(new Date()); // Find todays date
            return currentDateTime;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void saveRecord(double lat, double lng) {
        record.setName(userName);
        record.setScore(score);
        record.setLat(lat);
        record.setLng(lng);
        record.setTime(getCurrentTimeStamp());
    }
}
