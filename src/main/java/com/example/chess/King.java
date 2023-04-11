package com.example.chess;

import java.util.ArrayList;
import java.util.List;

public class King {
    private char color;
    private String [][] board;
    private boolean [] castlingList;
    private List<int[]> validMoves = new ArrayList<>();
    public King(char color, String [][] board, boolean [] castlingList){
        this.color = color;
        this.board = board;
        this.castlingList = castlingList;
    }

    public List<int[]> getValidMoves(int [] coords){
        int row = coords[0];
        int col = coords[1];
        double correctDistance = calculateDistance(0, 0, 1, 1);
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                double distance = calculateDistance(row, col, i, j);
                if(distance == correctDistance || distance == 1){
                    if(board[i][j].equals("-")){
                        validMoves.add(new int[]{i, j});
                    }else if(getColor(i, j) != color){
                        validMoves.add(new int[]{i, j});
                    }
                }
            }
        }
        appendCastlingMoves();
        return validMoves;
    }

    private double calculateDistance(double firstX, double firstY, double secondX, double secondY) {
        return Math.sqrt(Math.pow((secondX - firstX), 2) + Math.pow((secondY - firstY), 2));
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
