package com.example.chess;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Pawn{

    private char color;
    private String [][] board;
    private List<int[]> validMoves = new ArrayList<>();
    private boolean enPassant;
    private int [] enPassantCoordinates;
    private Map<int[], String> threatList;

    public Pawn(char color, String [][] board, boolean enPassant, int [] enPassantCoordinates, Map<int[], String> threatList){
        this.color = color;
        this.board = board;
        this.enPassant = enPassant;
        this.enPassantCoordinates = new int[]{enPassantCoordinates[0], enPassantCoordinates[1]};
        this.threatList = threatList;
    }

    public Pawn(char color, String [][] board, Map<int[], String> threatList){
        this.color = color;
        this.board = board;
        this.enPassant = false;
        this.threatList = threatList;
    }


    public List<int[]> getValidMovesWhenCheck(int [] coords){
        if(threatList == null){
            return validMoves;
        }
        if(threatList.keySet().size() > 1){
            return validMoves;
        }
        return validMoves;
    }

    public List<int[]> getValidMoves(int [] coords){
        int row = coords[0];
        int col = coords[1];

        if(color == 'b'){
          if(board[row+1][col].equals("-")){
              validMoves.add(new int[]{row+1, col});
              if(row == 1 && board[3][col].equals("-")){
                  validMoves.add(new int[]{3, col});
              }
          }
          if(col > 0 && !board[row+1][col-1].equals("-") && getColor(row+1, col-1) == 'w'){
              validMoves.add(new int[]{row+1, col-1});
          }
          if(col < 7 && !board[row+1][col+1].equals("-") && getColor(row+1, col+1) == 'w'){
                validMoves.add(new int[]{row+1, col+1});
            }
        }else if (color == 'w'){
            if(board[row-1][col].equals("-")){
                validMoves.add(new int[]{row-1, col});
                if(row == 6 && board[row-2][col].equals("-")){
                    validMoves.add(new int[]{row-2, col});
                }
            }
            if(col > 0 && !board[row-1][col-1].equals("-") && getColor(row-1, col-1) == 'b'){
                validMoves.add(new int[]{row-1, col-1});
            }
            if(col < 7 && !board[row-1][col+1].equals("-") && getColor(row-1, col+1) == 'b'){
                validMoves.add(new int[]{row-1, col+1});
            }
        }
        addValidEnPassant(row, col);
        return validMoves;
    }

    private void addValidEnPassant(int row, int col){
        if(!enPassant){
            return;
        }
        if(color == 'b'){
            if(row == enPassantCoordinates[0] && (col == enPassantCoordinates[1] - 1 || col == enPassantCoordinates[1] + 1)){
                validMoves.add(new int[]{enPassantCoordinates[0] + 1, enPassantCoordinates[1]});
            }
        }else if(color == 'w'){
            if(row == enPassantCoordinates[0] && (col == enPassantCoordinates[1] - 1 || col == enPassantCoordinates[1] + 1)){
                validMoves.add(new int[]{enPassantCoordinates[0] - 1, enPassantCoordinates[1]});
            }
        }
    }

    private char getColor(int row, int col){
        if(board[row][col].equals("-")){
            return 'n';
        }
        String color = board[row][col];
        return color.charAt(0);
    }

}
