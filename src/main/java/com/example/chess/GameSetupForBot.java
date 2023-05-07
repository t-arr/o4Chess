package com.example.chess;

public class GameSetupForBot {
    private static GameSetupForBot instance;
    private String color;
    private String duration;
    private boolean againstComp;

    private GameSetupForBot(String color, String duration, boolean againstComp){
        this.color = color;
        this.duration = duration;
        this.againstComp = againstComp;
    }

    public static GameSetupForBot getInstance(String color, String duration, boolean againstComp) {
        if (instance == null) {
            instance = new GameSetupForBot(color, duration, againstComp);
        }
        return instance;
    }

    public static GameSetupForBot getInstance(){
        return instance;
    }

    public String getColor() {
        return color;
    }

    public String getDuration() {
        return duration;
    }
    public boolean getAgainstComp() {
        return againstComp;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setAgainstComp(boolean againstComp) {
        this.againstComp = againstComp;
    }
}
