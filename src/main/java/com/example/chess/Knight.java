package com.example.chess;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Knight {
    private char color;
    private String [][] board;
    private List<int[]> validMoves = new ArrayList<>();
    private Map<int[], String> threatList;

    public Knight(char color, String [][] board, Map<int[], String> threatList){
        this.color = color;
        this.board = board;
        this.threatList = threatList;
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

    public List<int[]> getValidMoves(int [] coords){
        int row = coords[0];
        int col = coords[1];
        double correctDistance = calculateDistance(5, 3, 3, 4);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                double distance = calculateDistance(row, col, i, j);
                if (distance == correctDistance) {
                    if (board[i][j] == null) {
                        validMoves.add(new int[]{i, j});
                    } else if (getColor(i, j) != color) {
                        validMoves.add(new int[]{i, j});
                    }
                }
            }
        }
        return validMoves;
    }

    private double calculateDistance(double firstX, double firstY, double secondX, double secondY) {
        return Math.sqrt(Math.pow((secondX - firstX), 2) + Math.pow((secondY - firstY), 2));
    }

    private char getColor(int row, int col){
        if(board[row][col].equals("-")){
            return 'n';
        }
        String color = board[row][col];
        return color.charAt(0);
    }
}
