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

        return validMoves;
    }

    private char getColor(int row, int col){
        if(board[row][col].equals("-")){
            return 'n';
        }
        String color = board[row][col];
        return color.charAt(0);
    }












}
