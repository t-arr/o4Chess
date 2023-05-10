package com.example.chess;

public class GameSetupForBot {
    private static GameSetupForBot instance;
    private String color;
    private boolean againstComp;
    private String blackPieces;
    private String whitePieces;


    private GameSetupForBot(String color, boolean againstComp){
        this.color = color;
        this.againstComp = againstComp;
    }

    public static GameSetupForBot getInstance(String color, boolean againstComp) {
        if (instance == null) {
            instance = new GameSetupForBot(color, againstComp);
        }
        return instance;
    }

    public static GameSetupForBot getInstance(){
        return instance;
    }

    public String getColor() {
        return color;
    }

    public boolean getAgainstComp() {
        return againstComp;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setWhitePieces(String whitePieces){
        this.whitePieces = whitePieces;
    }

    public void setBlackPieces(String blackPieces){
        this.blackPieces = blackPieces;
    }

    public String getWhitePieces(){
        return whitePieces;
    }

    public String getBlackPieces(){
        return blackPieces;
    }

    public void setAgainstComp(boolean againstComp) {
        this.againstComp = againstComp;
    }
}
