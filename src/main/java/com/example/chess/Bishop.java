package com.example.chess;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Bishop {

    private char color;

    private String [][] board;

    private List<int[]> validMoves = new ArrayList<>();
    private Map<int[], String> threatList;

    public Bishop(char color, String [][] board, Map<int[], String> threatList){
        this.color = color;
        this.board = board;
        this.threatList = threatList;
    }

    public List<int[]> getValidMoves(int [] coords){
        int row = coords[0];
        int col = coords[1];
        northeastMovement(row, col);
        southeastMovement(row, col);
        northwestMovement(row, col);
        southwestMovement(row, col);
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

    private void northeastMovement(int row, int col) {
        int i = row - 1;
        int j = col + 1;
        while (i >= 0 && j <= 7) {
            if (board[i][j].equals("-")) {
                validMoves.add(new int[]{i, j});
            } else if (getColor(i, j) != color) {
                validMoves.add(new int[]{i, j});
                break;
            } else {
                break;
            }
            i--;
            j++;
        }
    }



    private void southeastMovement(int row, int col){
        int i = row + 1;
        int j = col + 1;
        while (i <= 7 && j <= 7) {
            if (board[i][j].equals("-")) {
                validMoves.add(new int[]{i, j});
            } else if (getColor(i, j) != color) {
                validMoves.add(new int[]{i, j});
                break;
            } else {
                break;
            }
            i++;
            j++;
        }
    }

    private void northwestMovement(int row, int col){
        int i = row - 1;
        int j = col - 1;
        while (i >= 0 && j >= 0) {
            if (board[i][j].equals("-")) {
                validMoves.add(new int[]{i, j});
            } else if (getColor(i, j) != color) {
                validMoves.add(new int[]{i, j});
                break;
            } else {
                break;
            }
            i--;
            j--;
        }
    }

    private void southwestMovement(int row, int col){
        int i = row + 1;
        int j = col - 1;
        while (i <= 7 && j >= 0) {
            if (board[i][j].equals("-")) {
                validMoves.add(new int[]{i, j});
            } else if (getColor(i, j) != color) {
                validMoves.add(new int[]{i, j});
                break;
            } else {
                break;
            }
            i++;
            j--;
        }
    }

    private char getColor(int row, int col){
        if(board[row][col].equals("-")){
            return 'n';
        }
        String color = board[row][col];
        return color.charAt(0);
    }
}
