package com.example.chess;

import java.util.ArrayList;
import java.util.List;

public class King {
    private char color;
    private String[][] board;
    private boolean[] castlingList;
    private List<int[]> validMoves = new ArrayList<>();
    private char opponentColor;
    private boolean isCheck;
    private String gameMode;
    private boolean playAgainstBot;


    public King(char color, String[][] board, boolean[] castlingList, char opponentColor, boolean isCheck, String gameMode, boolean playAgainstBot) {
        this.playAgainstBot = playAgainstBot;
        this.color = color;
        this.board = board;
        this.castlingList = castlingList;
        this.opponentColor = opponentColor;
        this.isCheck = isCheck;
        this.gameMode = gameMode;
    }

    //retruns all valid moves that a king can make
    public List<int[]> getValidMoves(int[] coords) {
        int row = coords[0];
        int col = coords[1];
        addValidMoves(row, col);
        if (!isCheck) {
            if(!playAgainstBot){
                appendCastlingMovesWhenPlayingWithWhite();
            }else{
                if(gameMode.equalsIgnoreCase("black")){
                    appendCastlingMovesWhenPlayingWithBlack();
                }else{
                    appendCastlingMovesWhenPlayingWithWhite();
                }
            }
            return validMoves;
        }
        return validMoves;
    }

    private void addValidMoves(int row, int col) {
        int[] targetRow = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] targetCol = {-1, 0, 1, -1, 1, -1, 0, 1};
        for (int i = 0; i < 8; i++) {
            int newRow = row + targetRow[i];
            int newCol = col + targetCol[i];
            if (isValidPosition(newRow, newCol)) {
                if (isNotSteppingToKingCheck(newRow, newCol) && !leavesKingInCheck(row, col, newRow, newCol)) {
                    if (board[newRow][newCol].equals("-") || getColor(newRow, newCol) != color) {
                        validMoves.add(new int[]{newRow, newCol});
                    }
                }
            }
        }
    }

    //Swaps temporarily the board to see if the move is in fact valid by calling is kingincheck method
    private boolean leavesKingInCheck(int row, int col, int newRow, int newCol) {
        String temp = board[newRow][newCol];
        board[newRow][newCol] = board[row][col];
        board[row][col] = "-";
        boolean inCheck = isKingInCheck(newRow, newCol);
        board[row][col] = board[newRow][newCol];
        board[newRow][newCol] = temp;
        return inCheck;
    }

    //Swaps temporarily the board to see if possible castling move is in fact valid by calling is kingincheck method
    private boolean leavesCastledInCheck(int row, int col, int newRow, int newCol, int rookRow, int rookCol, int newRookRow, int newRookCol) {
        String temp1 = board[row][col];
        String temp2 = board[rookRow][rookCol];
        board[row][col] = "-";
        board[newRow][newCol] = temp1;
        board[rookRow][rookCol] = "-";
        board[newRookRow][newRookCol] = temp2;
        boolean inCheck = isKingInCheck(newRow, newCol);
        board[newRow][newCol] = "-";
        board[row][col] = temp1;
        board[newRookRow][newRookCol] = "-";
        board[rookRow][rookCol] = temp2;
        return inCheck;
    }


    public boolean isKingInCheck(int x, int y) {
        int kingRow = x;
        int kingCol = y;
        int pawnDir = color == 'w' ? -1 : 1;
        int[][] pawnMoves = {{pawnDir, -1}, {pawnDir, 1}};
        for (int[] move : pawnMoves) {
            int dx = move[0];
            int dy = move[1];
            int newRow = kingRow + dx;
            int newCol = kingCol + dy;
            if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8 && board[newRow][newCol].startsWith(opponentColor + "pa")) {
                return true;
            }
        }

        int[][] knightMoves = {{-2, -1}, {-2, 1}, {-1, -2}, {-1, 2}, {1, -2}, {1, 2}, {2, -1}, {2, 1}};
        for (int[] move : knightMoves) {
            int dx = move[0];
            int dy = move[1];
            int newRow = kingRow + dx;
            int newCol = kingCol + dy;
            if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8 && board[newRow][newCol].startsWith(opponentColor + "kn")) {
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
                int newRow = kingRow + dx;
                int newCol = kingCol + dy;
                while (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8) {
                    String piece = board[newRow][newCol];
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
                    newRow += dx;
                    newCol += dy;
                }
            }
        }
        return false;
    }

    //Makes sure that kings have at least 1 square between them
    private boolean isNotSteppingToKingCheck(int row, int col) {
        int[] offsets = {-1, 0, 1};
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int newRow = row + offsets[i];
                int newCol = col + offsets[j];
                if (isValidPosition(newRow, newCol) && (newRow != row || newCol != col)) {
                    if (board[newRow][newCol].substring(1).equals("king") && color != board[newRow][newCol].charAt(0)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    private boolean isValidPosition(int row, int col) {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }

    private void appendCastlingMovesWhenPlayingWithWhite() {
        if (color == 'b') {
            if (castlingList[0]) {
                if (board[0][1].equals("-") && board[0][2].equals("-") && board[0][3].equals("-") && !leavesCastledInCheck(0, 4, 0, 2, 0, 0, 0, 3)) {
                    validMoves.add(new int[]{0, 2});
                }
            }
            if (castlingList[1]) {
                if (board[0][6].equals("-") && board[0][5].equals("-") && !leavesCastledInCheck(0, 4, 0, 6, 0, 7, 0, 5)) {
                    validMoves.add(new int[]{0, 6});
                }
            }
        } else {
            if (castlingList[2]) {
                if (board[7][1].equals("-") && board[7][2].equals("-") && board[7][3].equals("-") && !leavesCastledInCheck(7, 4, 7, 2, 7, 0, 7, 3)) {
                    validMoves.add(new int[]{7, 2});
                }
            }
            if (castlingList[3]) {
                if (board[7][6].equals("-") && board[7][5].equals("-") && !leavesCastledInCheck(7, 4, 7, 6, 7, 7, 7, 5)) {
                    validMoves.add(new int[]{7, 6});
                }
            }
        }
    }

    private void appendCastlingMovesWhenPlayingWithBlack() {
        if (color == 'b') {
            if (castlingList[0]) {
                if (board[0][1].equals("-") && board[0][2].equals("-") && !leavesCastledInCheck(0, 3, 0, 1, 0, 0, 0, 2)) {
                    validMoves.add(new int[]{0, 1});
                }
            }
            if (castlingList[1]) {
                if (board[0][6].equals("-") && board[0][5].equals("-") && board[0][4].equals("-") && !leavesCastledInCheck(0, 3, 0, 5, 0, 7, 0, 4)) {
                    validMoves.add(new int[]{0, 5});
                }
            }
        } else {
            if (castlingList[2]) {
                if (board[7][1].equals("-") && board[7][2].equals("-") && !leavesCastledInCheck(7, 3, 7, 1, 7, 0, 7, 2)) {
                    validMoves.add(new int[]{7, 1});
                }
            }
            if (castlingList[3]) {
                if (board[7][6].equals("-") && board[7][5].equals("-") && board[7][4].equals("-") && !leavesCastledInCheck(7, 3, 7, 5, 7, 7, 7, 4)) {
                    validMoves.add(new int[]{7, 5});
                }
            }
        }
    }

    private char getColor(int row, int col) {
        if (board[row][col].equals("-")) {
            return 'n';
        }
        String color = board[row][col];
        return color.charAt(0);
    }
}