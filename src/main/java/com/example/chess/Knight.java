package com.example.chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Knight {
    private char color;
    private String [][] board;
    private List<int[]> validMoves = new ArrayList<>();
    private Map<int[], String> threatList;
    private boolean isCheck;
    private int[] kingCoordinates;
    private char opponentColor;

    public Knight(char color, String [][] board, Map<int[], String> threatList, boolean isCheck, int [] kingCoordinates, char opponentColor){
        this.color = color;
        this.board = board;
        this.threatList = threatList;
        this.isCheck = isCheck;
        this.kingCoordinates = kingCoordinates;
        this.opponentColor = opponentColor;
    }

    public List<int[]> getValidMoves(int [] coords){
        int row = coords[0];
        int col = coords[1];
        appendValidMoves(row, col);
        return validMoves;
    }

    public void appendValidMoves(int row, int col){
        int [] targetRow = new int[]{-2, -2, 2, 2, -1, -1, 1, 1};
        int [] targetCol = new int[]{1, -1, 1, -1, 2, -2, 2, -2};
        for(int i = 0; i < 8; i++) {
            int newRow = row + targetRow[i];
            int newCol = col + targetCol[i];
            if (isValidPosition(newRow, newCol)) {
                if (isCheck) {
                    if (shouldBreak()) {
                        break;
                    }
                    appendValidMovesWhenCheck(newRow, newCol, row, col);
                }
                if (board[newRow][newCol].charAt(0) != color && !isCheck && !leavesKingInCheck(row, col, newRow, newCol)) {
                    validMoves.add(new int[]{newRow, newCol});
                }
            }
        }
    }

    private boolean leavesKingInCheck(int x, int y, int newX, int newY) {
        String temp = board[newX][newY];
        board[newX][newY] = board[x][y];
        board[x][y] = "-";
        boolean inCheck = isKingInCheck();
        board[x][y] = board[newX][newY];
        board[newX][newY] = temp;
        return inCheck;
    }

    private boolean isKingInCheck() {
        int kingX = kingCoordinates[0];
        int kingY = kingCoordinates[1];
        int[][] bishopMoves = {{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};
        int[][] rookMoves = {{-1, 0}, {0, -1}, {0, 1}, {1, 0}};
        int[][] queenMoves = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};
        int[][][] slidingMoves = {bishopMoves, rookMoves, queenMoves};
        for (int[][] moves : slidingMoves) {
            for (int[] move : moves) {
                int dx = move[0];
                int dy = move[1];
                int newX = kingX + dx;
                int newY = kingY + dy;
                while (newX >= 0 && newX < 8 && newY >= 0 && newY < 8) {
                    String piece = board[newX][newY];
                    if (!piece.equals("-")) {
                        if (piece.startsWith(opponentColor + "b") && (dx != 0 && dy != 0)) {
                            return true;
                        }
                        if (piece.startsWith(opponentColor + "r") && (dx == 0 || dy == 0)) {
                            return true;
                        }
                        if (piece.startsWith(opponentColor + "q")) {
                            return true;
                        }
                        break;
                    }
                    newX += dx;
                    newY += dy;
                }
            }
        }
        return false;
    }

    private boolean shouldBreak(){
        return threatList.keySet().size() > 1;
    }

    private boolean isValidPosition(int row, int col){
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }

    public void appendValidMovesWhenCheck(int row, int col, int oldRow, int oldCol){
        if (threatList.keySet().size() > 1){
            return;
        }
        int[] threatCoordinates = new int[]{0, 0};
        for (int[] key : threatList.keySet()) {
            threatCoordinates = key;
            if (Arrays.equals(key, new int[]{row, col})) {
                validMoves.add(new int[]{row, col});
                return;
            }
        }
        String threatType = board[threatCoordinates[0]][threatCoordinates[1]].substring(1);
        if(threatType.equals("queen") || threatType.equals("rook") || threatType.equals("bishop")){
            System.out.println("ass");
        }
    }
}