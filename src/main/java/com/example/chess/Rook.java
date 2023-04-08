package com.example.chess;

import java.util.ArrayList;
import java.util.List;

public class Rook {

    private char color;
    private String [][] board;
    private List<int[]> validMoves = new ArrayList<>();
    public Rook(char color, String [][] board){
        this.color = color;
        this.board = board;
    }

    public List<int[]> getValidMoves(int [] coords){
        int row = coords[0];
        int col = coords[1];
        northMovement(row, col);
        southMovement(row, col);
        eastMovement(row, col);
        westMovement(row, col);
        return validMoves;
    }

    private void northMovement(int row, int col){
        for (int i = row - 1; i >= 0; i--) {
            if (movement(col, i)) break;
        }
    }
    private void southMovement(int row, int col){
        for (int i = row + 1; i <= 7; i++) {
            if (movement(col, i)) break;
        }
    }
    private void eastMovement(int row, int col){
        for (int i = col + 1; i <= 7; i++) {
            if (board[row][i].equals("-")) {
                validMoves.add(new int[]{row, i});
            } else if (getColor(row, i) != color) {
                validMoves.add(new int[]{row, i});
                break;
            } else {
                break;
            }
        }
    }

    private void westMovement(int row, int col){
        for (int i = col - 1; i >= 0; i--) {
            if (board[row][i].equals("-")) {
                validMoves.add(new int[]{row, i});
            } else if (getColor(row, i) != color) {
                validMoves.add(new int[]{row, i});
                break;
            } else {
                break;
            }
        }
    }


    private boolean movement(int col, int i) {
        if (this.board[i][col].equals("-")) {
            validMoves.add(new int[]{i, col});
        } else if (getColor(i, col) != this.color) {
            validMoves.add(new int[]{i, col});
            return true;
        } else {
            return true;
        }
        return false;
    }
    private char getColor(int row, int col){
        if(board[row][col].equals("-")){
            return 'n';
        }
        String color = board[row][col];
        return color.charAt(0);
    }
}
