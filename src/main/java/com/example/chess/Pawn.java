package com.example.chess;

import java.util.ArrayList;
import java.util.List;

public class Pawn{

    private char color;
    private String [][] board;
    private List<int[]> validMoves = new ArrayList<>();

    public Pawn(char color, String [][] board){
        this.color = color;
        this.board = board;
    }

}
