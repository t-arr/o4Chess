package com.example.chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {
    private String[][] board;
    private char turn = 'w';
    private boolean[] castlingList;
    private boolean validEnPassant;
    private int[] enPassantCoordinates = new int[]{0, 0};
    private List<int[]> validOpponentMoves;

    public Board() {
        this.board = new String[][]{{"brook", "bknight", "bbishop", "bqueen", "bking", "bbishop", "bknight", "brook"},
                {"bpawn", "bpawn", "bpawn", "bpawn", "bpawn", "bpawn", "bpawn", "bpawn"},
                {"-", "-", "-", "-", "-", "-", "-", "-"},
                {"-", "-", "-", "-", "-", "-", "-", "-"},
                {"-", "-", "-", "-", "-", "-", "-", "-"},
                {"-", "-", "-", "-", "-", "-", "-", "-"},
                {"wpawn", "wpawn", "wpawn", "wpawn", "wpawn", "wpawn", "wpawn", "wpawn"},
                {"wrook", "wknight", "wbishop", "wqueen", "wking", "wbishop", "wknight", "wrook"}};
        this.castlingList = new boolean[]{true, true, true, true};
        validEnPassant = false;
    }

    public void swap(int[] from, int[] to) {
        String temp = board[from[0]][from[1]];
        board[from[0]][from[1]] = "-";
        board[to[0]][to[1]] = temp;
    }

    public String[][] getBoard() {
        return board;
    }

    public String getPiece(int[] coords) {
        return board[coords[0]][coords[1]];
    }

    public char getTurn() {
        return turn;
    }

    public char getOpponentTurn() {
        if (getTurn() == 'w') {
            return 'b';
        }
        return 'w';
    }

    public void swapTurn() {
        if (turn == 'w') {
            turn = 'b';
        } else {
            turn = 'w';
        }
    }

    public List<int[]> validMoves(int[] coords) {
        String type = getPiece(coords);
        char color = type.charAt(0);
        type = type.substring(1);
        switch (type) {
            case "pawn" -> {
                Pawn pawn = new Pawn(color, board, validEnPassant, enPassantCoordinates);
                return pawn.getValidMoves(coords);
            }
            case "rook" -> {
                Rook rook = new Rook(color, board);
                return rook.getValidMoves(coords);
            }
            case "knight" -> {
                Knight knight = new Knight(color, board);
                return knight.getValidMoves(coords);
            }
            case "bishop" -> {
                Bishop bishop = new Bishop(color, board);
                return bishop.getValidMoves(coords);
            }
            case "queen" -> {
                Queen queen = new Queen(color, board);
                return queen.getValidMoves(coords);
            }
            case "king" -> {
                King king = new King(color, board, castlingList, validOpponentMoves);
                return king.getValidMoves(coords);
            }
            default -> {
                return new ArrayList<>();
            }
        }
    }

    public void updateCastlingVariables(int[] from, int[] to) {
        if (Arrays.equals(from, new int[]{0, 4}) && (!Arrays.equals(to, new int[]{0, 2}) || !Arrays.equals(to, new int[]{0, 6}))) {
            castlingList[0] = false;
            castlingList[1] = false;
        }
        if (Arrays.equals(from, new int[]{7, 4}) && (!Arrays.equals(to, new int[]{7, 2}) || !Arrays.equals(to, new int[]{7, 6}))) {
            castlingList[2] = false;
            castlingList[3] = false;
        }
        if (Arrays.equals(from, new int[]{0, 0})) {
            castlingList[0] = false;
        }
        if (Arrays.equals(from, new int[]{0, 7})) {
            castlingList[1] = false;
        }
        if (Arrays.equals(from, new int[]{7, 0})) {
            castlingList[2] = false;
        }
        if (Arrays.equals(from, new int[]{7, 7})) {
            castlingList[3] = false;
        }
    }

    public void castle(int[] from, int[] to) {
        int fromRow = from[0];
        int fromCol = from[1];
        int toRow = to[0];
        int toCol = to[1];

        if ((fromRow == 0 && fromCol == 4) || (fromRow == 7 && fromCol == 4)) {
            if ((toRow == 0 && (toCol == 2 || toCol == 6)) || (toRow == 7 && (toCol == 2 || toCol == 6))) {
                if (toCol < fromCol) {
                    String tmp = board[fromRow][0];
                    board[fromRow][0] = "-";
                    board[fromRow][3] = tmp;
                } else {
                    String tmp = board[fromRow][7];
                    board[fromRow][7] = "-";
                    board[fromRow][5] = tmp;
                }
            }
        }
    }

    public void setEnPassant(int[] from, int[] to) {
        String type = getPiece(from).substring(1);
        if (type.equals("pawn") && (Math.abs(from[0] - to[0]) == 2)) {
            validEnPassant = true;
            enPassantCoordinates = new int[]{to[0], to[1]};
        } else {
            validEnPassant = false;
        }
    }

    public void swapEnPassant(int[] from, int[] to) {
        char color = getPiece(from).charAt(0);
        String temp = board[from[0]][from[1]];
        board[from[0]][from[1]] = "-";
        board[to[0]][to[1]] = temp;
        if (color == 'b') {
            board[to[0] - 1][to[1]] = "-";
        } else {
            board[to[0] + 1][to[1]] = "-";
        }
    }

    public boolean isMoveEnPassant(int[] from, int[] to) {
        String type = getPiece(from).substring(1);
        return type.equals("pawn") && getPiece(to).equals("-") && from[1] != to[1];
    }

    public boolean isPromotion(int[] from, int[] to) {
        String piece = getPiece(from).substring(1);
        char color = getPiece(from).charAt(0);
        if (piece.equals("pawn") && color == 'b' && to[0] == 7) {
            return true;
        } else return piece.equals("pawn") && color == 'w' && to[0] == 0;
    }

    public void swapPromotion(int[] from, int[] to, String wantedPiece) {
        char color = getPiece(from).charAt(0);
        board[to[0]][to[1]] = color + wantedPiece;
        board[from[0]][from[1]] = "-";
    }

    public void allOpponentMoves(char color) {
        validOpponentMoves = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (color == board[i][j].charAt(0) && (!getPiece(new int[]{i, j}).substring(1).equals("pawn"))) {
                    validOpponentMoves.addAll(validMoves(new int[]{i, j}));
                }
            }
        }
    }

    public int[] getKingCoordinates() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j].equals(getTurn() + "king")) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    public boolean isKingInCheck() {
        if(isKingInPawnCheck()){
            return true;
        }
        int[] kingCoordinates = getKingCoordinates();
        for (int[] moves : validOpponentMoves) {
            if (Arrays.equals(moves, kingCoordinates)) {
                return true;
            }
        }
        return false;
    }

    private boolean isKingInPawnCheck() {
        int[] kingCoordinates = getKingCoordinates();
        int row = kingCoordinates[0];
        int col = kingCoordinates[1];
        char color = getTurn();
        char oppColor = getOpponentTurn();
        if (color == 'b') {
            if(row == 7){
                return false;
            }
            if (row + 1 <= 7 && col + 1 < 8 && col - 1 >= 0) {
                return board[row + 1][col - 1].equals(oppColor + "pawn") || board[row + 1][col + 1].equals(oppColor + "pawn");
            }
            if(col == 0){
                return board[row+1][col+1].equals(oppColor + "pawn");
            }
            if(col == 7){
                return board[row+1][col-1].equals(oppColor + "pawn");
            }
        } else if (color == 'w') {
            if(row == 0){
                return false;
            }
            if (row - 1 >= 0 && col - 1 >= 0 && col + 1 < 8) {
                return board[row - 1][col - 1].equals(oppColor + "pawn") || board[row - 1][col + 1].equals(oppColor + "pawn");
            }
            if(col == 0){
                return board[row-1][col+1].equals(oppColor + "pawn");
            }
            if(col == 7){
                return board[row-1][col-1].equals(oppColor + "pawn");
            }
        }
        return false;
    }
}
