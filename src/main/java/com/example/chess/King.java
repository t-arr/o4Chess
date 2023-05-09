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
    private Board gameState;
    private boolean playAgainstBot;
    private boolean isBotTurn;


    public King(char color, String[][] board, boolean[] castlingList, char opponentColor, boolean isCheck, String gameMode, Board gameState, boolean playAgainstBot, boolean isBotTurn) {
        this.playAgainstBot = playAgainstBot;
        this.color = color;
        this.board = board;
        this.castlingList = castlingList;
        this.opponentColor = opponentColor;
        this.isCheck = isCheck;
        this.gameMode = gameMode;
        this.gameState = gameState;
    }

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
                if (isNotSteppingToPawnCheck(newRow, newCol) && isNotSteppingToKingCheck(newRow, newCol) && !leavesKingInCheck(row, col, newRow, newCol)) {
                    if (board[newRow][newCol].equals("-") || getColor(newRow, newCol) != color) {
                        validMoves.add(new int[]{newRow, newCol});
                    }
                }
            }
        }
    }

    private boolean leavesKingInCheck(int x, int y, int newX, int newY) {
        String temp = board[newX][newY];
        board[newX][newY] = board[x][y];
        board[x][y] = "-";
        boolean inCheck = isKingInCheck(newX, newY);
        board[x][y] = board[newX][newY];
        board[newX][newY] = temp;
        return inCheck;
    }

    private boolean leavesCastledInCheck(int x, int y, int newX, int newY, int rookX, int rookY, int newRookX, int newRookY) {
        String temp1 = board[x][y];
        String temp2 = board[rookX][rookY];
        board[x][y] = "-";
        board[newX][newY] = temp1;
        board[rookX][rookY] = "-";
        board[newRookX][newRookY] = temp2;
        boolean inCheck = isKingInCheck(newX, newY);
        board[newX][newY] = "-";
        board[x][y] = temp1;
        board[newRookX][newRookY] = "-";
        board[rookX][rookY] = temp2;
        return inCheck;
    }


    public boolean isKingInCheck(int x, int y) {
        int kingX = x;
        int kingY = y;
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


    private boolean isNotSteppingToPawnCheck(int row, int col) {
        if (color == 'b' && row + 1 < 8 && col - 1 >= 0 && col + 1 < 8) {
            return !board[row + 1][col - 1].equals("wpawn") && !board[row + 1][col + 1].equals("wpawn");
        }
        if (color == 'w' && row - 1 >= 0 && col - 1 >= 0 && col + 1 < 8) {
            return !board[row - 1][col - 1].equals("bpawn") && !board[row - 1][col + 1].equals("bpawn");
        }
        if (color == 'b' && row + 1 < 8 && col == 0) {
            return !board[row + 1][col + 1].equals("wpawn");
        }
        if (color == 'b' && row + 1 < 8 && col == 7) {
            return !board[row + 1][col - 1].equals("wpawn");
        }
        if (color == 'w' && row - 1 >= 0 && col == 0) {
            return !board[row - 1][col + 1].equals("bpawn");
        }
        if (color == 'w' && row - 1 >= 0 && col == 7) {
            return !board[row - 1][col - 1].equals("bpawn");
        }
        return true;
    }

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