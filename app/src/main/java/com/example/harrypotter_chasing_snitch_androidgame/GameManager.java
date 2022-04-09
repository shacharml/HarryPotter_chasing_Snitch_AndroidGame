package com.example.harrypotter_chasing_snitch_androidgame;

public class GameManager {

    private int score ;
    private int live ;

    //----- Constructors ------
    public GameManager() {
    }

    public GameManager(int score, int live) {
        this.score = score;
        this.live = live;
    }

    // -------- -----------

    public void addScoreEveryTick(){
        score++;
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

    //----- Getters & Setters ------

    public int getScore() {
        return score;
    }
    public int getLive() {
        return live;
    }
}
