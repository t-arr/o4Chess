package com.example.chess;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Board {
    private String[][] board;

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
    public List<int[]> validMoves(int[] coords){
        String type = getPiece(coords);
        char color = type.charAt(0);
        type = type.substring(1);
        if(type.equals("pawn")){
            Pawn p = new Pawn(color, board);
        } else if (type.equals("rook")) {
            Rook rook = new Rook(getPiece(coords).charAt(0), board);
            return rook.getValidMoves(coords);
        } else if (type.equals("knight")) {
            System.out.println("knight");
        } else if (type.equals("bishop")) {
            System.out.println("bishop");
        } else if (type.equals("queen")) {
            System.out.println("queen");
        } else if (type.equals("king")) {
            System.out.println("king");
        }
        return new ArrayList<>();
    }
}
