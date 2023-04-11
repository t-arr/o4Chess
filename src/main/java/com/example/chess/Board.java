package com.example.chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {
    private String[][] board;
    private char turn = 'w';
    private boolean [] castlingList;


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
    }

    public void swap(int[] from, int[] to){
        String temp = board[from[0]][from[1]];
        board[from[0]][from[1]] = "-";
        board[to[0]][to[1]] = temp;
    }
    public String[][] getBoard(){
        return board;
    }

    public String getPiece(int[] coords){
        return board[coords[0]][coords[1]];
    }

    public char getTurn(){
        return turn;
    }
    public void swapTurn(){
        if (turn == 'w'){
            turn = 'b';
        }else{
            turn = 'w';
        }
    }

    public List<int[]> validMoves(int[] coords){
        String type = getPiece(coords);
        char color = type.charAt(0);
        type = type.substring(1);
        switch (type) {
            case "pawn" -> {
                Pawn pawn = new Pawn(color, board);
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
                King king = new King(color, board, castlingList);
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

    public void castle(int[] from, int[] to){
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
}