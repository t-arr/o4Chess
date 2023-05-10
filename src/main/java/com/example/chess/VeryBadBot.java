package com.example.chess;

import java.util.*;

public class VeryBadBot {

    private char color;

    public VeryBadBot(){
        this.color = 'b';
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
        gameState.setEnPassant(randomKey, randomValue);
        if(gameState.getPiece(randomKey).substring(1).equals("pawn")){
            if(randomValue[0] == 0 || randomValue[0] == 7){
                board[randomKey[0]][randomKey[1]] = color + "queen";
            }
        }
        if(gameState.isMoveEnPassant(randomKey, randomValue)){
            gameState.swapEnPassant(randomKey, randomValue);
        }else if(gameState.getPiece(randomKey).substring(1).equals("king") && isMoveCastling(randomKey, randomValue)){
            gameState.swap(randomKey, randomValue);
            gameState.swapCastle(randomKey, randomValue);
        }else{
            gameState.swap(randomKey, randomValue);
        }
        gameState.updateCastlingVariables(randomKey, randomValue);
    }
    private boolean isMoveCastling(int[] from, int [] to){
        return Math.abs(from[1]-to[1]) == 2;
    }
}