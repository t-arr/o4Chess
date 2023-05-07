package com.example.chess;

import java.util.*;

public class VeryBadBot {
    private GameSetupForBot setupSingleton = GameSetupForBot.getInstance();
    private char color;

    public VeryBadBot(){
        if(setupSingleton.getColor().equals("White")){
            this.color = 'b';
        }else{
            this.color = 'w';
        }
    }
    public void makeMove(String[][] board, Board gameState){
        Map<int[], List<int []>> piecesAndMoves = new HashMap<>();
        for(int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                int [] coordinates = new int[]{i, j};
                if(gameState.getPiece(coordinates).charAt(0) == color){
                    List<int[]> validMoves = gameState.validMoves(coordinates);
                    if(!validMoves.isEmpty()){
                        piecesAndMoves.put(coordinates, validMoves);
                    }
                }
            }
        }
        Set<int[]> keySet = piecesAndMoves.keySet();
        int randomIndex = (int) (Math.random() * keySet.size());
        int[] randomKey = (int[]) keySet.toArray()[randomIndex];
        List<int[]> values = piecesAndMoves.get(randomKey);
        randomIndex = (int) (Math.random() * values.size());
        int[] randomValue = values.get(randomIndex);
        gameState.updateCastlingVariables(randomKey, randomValue);
        gameState.swap(randomKey, randomValue);
    }
}
