package com.example.harrypotter_chasing_snitch_androidgame;

public class PlayerCharacter {

    private int col;
    private int row;
    private int direction;
    private int icon; //src of image icon

    //----- Constructors ------

    public PlayerCharacter() {
    }

    public PlayerCharacter( int row,int col, int direction, int icon) {
        this.col = col;
        this.row = row;
        this.direction = direction;
        this.icon = icon;
    }


    //----- Getters & Setters ------
    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }
}
