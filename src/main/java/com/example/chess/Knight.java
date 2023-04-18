package com.example.chess;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Knight {
    private char color;
    private String [][] board;
    private String [][] copyBoard;
    private List<int[]> validMoves = new ArrayList<>();
    private Map<int[], String> threatList;

    public Knight(char color, String [][] board, Map<int[], String> threatList){
        this.color = color;
        this.board = board;
        this.threatList = threatList;
    }

    public List<int[]> getValidMoves(int [] coords){
        int row = coords[0];
        int col = coords[1];
        if(threatList != null){
            appendValidMovesWhenCheck(row, col);
        }else{
            appendValidMoves(row, col);
        }
        return validMoves;
    }

    public void appendValidMoves(int row, int col){
        int [] targetRow = new int[]{-2, -2, 2, 2, -1, -1, 1, 1};
        int [] targetCol = new int[]{1, -1, 1, -1, 2, -2, 2, -2};
        for(int i = 0; i < 8; i++){
            int newRow = row + targetRow[i];
            int newCol = col + targetCol[i];
            if(isValidPosition(newRow, newCol)){
                if(board[newRow][newCol].charAt(0) != color){
                    validMoves.add(new int[]{newRow, newCol});
                }
            }
        }
    }

    private boolean isValidPosition(int row, int col){
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }

    public void appendValidMovesWhenCheck(int row, int col){
        if (threatList.keySet().size() > 1){
            return;
        }
    }
}
