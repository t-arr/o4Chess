package com.example.chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Knight {
    private char color;
    private String [][] board;
    private List<int[]> validMoves = new ArrayList<>();
    private boolean isCheck;
    private int[] kingCoordinates;
    private char opponentColor;

    public Knight(char color, String [][] board, boolean isCheck, int [] kingCoordinates, char opponentColor){
        this.color = color;
        this.board = board;
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
                if (board[newRow][newCol].charAt(0) != color && !leavesKingInCheck(row, col, newRow, newCol)) {
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
        int pawnDir = color == 'w' ? -1 : 1;
        int[][] pawnMoves = {{pawnDir, -1}, {pawnDir, 1}};
        for (int[] move : pawnMoves) {
            int dx = move[0];
            int dy = move[1];
            int newX = kingX + dx;
            int newY = kingY + dy;
            if (newX >= 0 && newX < 8 && newY >= 0 && newY < 8 && board[newX][newY].startsWith(opponentColor + "pa")) {
                return true;
            }
        }

        int[][] knightMoves = {{-2, -1}, {-2, 1}, {-1, -2}, {-1, 2}, {1, -2}, {1, 2}, {2, -1}, {2, 1}};
        for (int[] move : knightMoves) {
            int dx = move[0];
            int dy = move[1];
            int newX = kingX + dx;
            int newY = kingY + dy;
            if (newX >= 0 && newX < 8 && newY >= 0 && newY < 8 && board[newX][newY].startsWith(opponentColor + "kn")) {
                return true;
            }
        }
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


    private boolean isValidPosition(int row, int col){
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }

  
}