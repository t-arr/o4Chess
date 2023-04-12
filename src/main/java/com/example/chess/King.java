package com.example.chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class King {
    private char color;
    private String [][] board;
    private boolean [] castlingList;
    private List<int[]> validMoves = new ArrayList<>();
    private List<int[]> validOpponentMoves;

    public King(char color, String [][] board, boolean [] castlingList, List<int[]> validOpponentMoves){
        this.color = color;
        this.board = board;
        this.castlingList = castlingList;
        this.validOpponentMoves = validOpponentMoves;
    }

    public List<int[]> getValidMoves(int [] coords){
        int row = coords[0];
        int col = coords[1];
        addValidMoves(row, col);
        appendCastlingMoves();
        return validMoves;
    }

    private void addValidMoves(int row, int col) {
        int[] dr = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dc = {-1, 0, 1, -1, 1, -1, 0, 1};
        for (int i = 0; i < 8; i++) {
            int newRow = row + dr[i];
            int newCol = col + dc[i];
            if (isValidPosition(newRow, newCol) && isNotSteppingToCheck(newRow, newCol) && isNotSteppingToPawnCheck(newRow, newCol)) {
                if (board[newRow][newCol].equals("-") || getColor(newRow, newCol) != color) {
                    validMoves.add(new int[]{newRow, newCol});
                }
            }
        }
    }
    private boolean isNotSteppingToCheck(int row, int col) {
        int [] coordinates = new int[]{row, col};
        for (int[] moves : validOpponentMoves) {
            if (Arrays.equals(moves, coordinates)) {
                return false;
            }
        }
        return true;
    }

    private boolean isNotSteppingToPawnCheck(int row, int col) {
        if(color == 'b'){
            return !board[row + 1][col - 1].equals("wpawn") && !board[row + 1][col + 1].equals("wpawn");
        } else if (color == 'w') {
            return !board[row - 1][col - 1].equals("bpawn") && !board[row - 1][col + 1].equals("bpawn");
        }
        return true;
    }

    private boolean isValidPosition(int row, int col) {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }

    public void preventFromSteppingToCheck(int row, int col){
        int [] coordinates = new int[]{row, col};

    }

    private void appendCastlingMoves(){
        if(color == 'b'){
            if(castlingList[0]){
                if(board[0][1].equals("-") && board[0][2].equals("-") && board[0][3].equals("-")){
                    validMoves.add(new int[]{0, 2});
                }
            }
            if (castlingList[1]){
                if(board[0][6].equals("-") && board[0][5].equals("-")){
                    validMoves.add(new int[]{0, 6});
                }
            }
        }else{
            if(castlingList[2]){
                if(board[7][1].equals("-") && board[7][2].equals("-") && board[7][3].equals("-")){
                    validMoves.add(new int[]{7, 2});
                }
            }
            if (castlingList[3]){
                if(board[7][6].equals("-") && board[7][5].equals("-")){
                    validMoves.add(new int[]{7, 6});
                }
            }
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
