package com.example.chess;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private String[][] board;
    private char turn = 'w';

    public Board() {
        this.board = new String[][]{{"brook", "bknight", "bbishop", "bqueen", "bking", "bbishop", "bknight", "brook"},
                {"bpawn", "bpawn", "bpawn", "bpawn", "bpawn", "bpawn", "bpawn", "bpawn"},
                {"-", "-", "-", "-", "-", "-", "-", "-"},
                {"-", "-", "-", "-", "-", "-", "-", "-"},
                {"-", "-", "-", "-", "-", "-", "-", "-"},
                {"-", "-", "-", "-", "-", "-", "-", "-"},
                {"wpawn", "wpawn", "wpawn", "wpawn", "wpawn", "wpawn", "wpawn", "wpawn"},
                {"wrook", "wknight", "wbishop", "wqueen", "wking", "wbishop", "wknight", "wrook"}};
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
        if(type.equals("pawn")){
            Pawn pawn = new Pawn(color, board);
            return pawn.getValidMoves(coords);
        } else if (type.equals("rook")) {
            Rook rook = new Rook(color, board);
            return rook.getValidMoves(coords);
        } else if (type.equals("knight")) {
            Knight knight = new Knight(color, board);
            return knight.getValidMoves(coords);
        } else if (type.equals("bishop")) {
            Bishop bishop = new Bishop(color, board);
            return bishop.getValidMoves(coords);
        } else if (type.equals("queen")) {
            Queen queen = new Queen(color, board);
            return queen.getValidMoves(coords);
        } else if (type.equals("king")) {
            King king = new King(color, board);
            return king.getValidMoves(coords);
        }
        return new ArrayList<>();
    }


}