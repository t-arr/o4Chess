package com.example.chess;

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

}
