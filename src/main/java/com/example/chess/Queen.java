package com.example.chess;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Queen {
    private char color;
    private String [][] board;
    private List<int[]> validMoves = new ArrayList<>();
    private Map<int[], String> threatList;
    public Queen(char color, String [][] board, Map<int[], String> threatList){
        this.color = color;
        this.board = board;
        this.threatList = threatList;
    }
    public List<int[]> getValidMoves(int [] coords){
        int row = coords[0];
        int col = coords[1];
        appendValidMoves(row, col);
        return validMoves;
    }

    public List<int[]> getValidMovesWhenCheck(int [] coords){
        if(threatList == null){
            return validMoves;
        }
        if(threatList.keySet().size() > 1){
            return validMoves;
        }
        return validMoves;
    }

    public void appendValidMoves(int row, int col){
        int [] targetRow = new int []{-1, 0, 1, 0, -1, 1, 1, -1};
        int [] targetCol = new int []{0, 1, 0, -1, 1, 1, -1, -1};
        for(int i = 0; i < 8; i++){
            int newRow = row;
            int newCol = col;
            while(true){
                newRow += targetRow[i];
                newCol += targetCol[i];
                if(!isValidPosition(newRow, newCol)){
                    break;
                }
                if(getColor(newRow, newCol) == '-'){
                    validMoves.add(new int[]{newRow, newCol});
                }else if (getColor(newRow, newCol) != color && getColor(newRow, newCol) != '-'){
                    validMoves.add(new int[]{newRow, newCol});
                    break;
                }else{
                    break;
                }
            }
        }
    }

    private boolean isValidPosition(int row, int col){
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }

    private char getColor(int row, int col){
        if(board[row][col].equals("-")){
            return '-';
        }
        String color = board[row][col];
        return color.charAt(0);
    }
}