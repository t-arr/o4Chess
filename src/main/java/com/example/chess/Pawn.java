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
    private boolean isCheck;
    private char opponentColor;
    private int [] kingCoordinates;

    public Pawn(char color, String [][] board, boolean enPassant, int [] enPassantCoordinates, Map<int[], String> threatList, boolean isCheck, char opponentColor, int [] kingCoordinates){
        this.color = color;
        this.board = board;
        this.enPassant = enPassant;
        this.enPassantCoordinates = new int[]{enPassantCoordinates[0], enPassantCoordinates[1]};
        this.threatList = threatList;
        this.isCheck = isCheck;
        this.opponentColor = opponentColor;
        this.kingCoordinates = kingCoordinates;

    }

    public Pawn(char color, String [][] board, Map<int[], String> threatList){
        this.color = color;
        this.board = board;
        this.enPassant = false;
        this.threatList = threatList;
    }

    public List<int[]> getValidMoves(int [] coords){
        int row = coords[0];
        int col = coords[1];
        int direction = (color == 'b') ? 1 : -1;

        if (board[row+direction][col].equals("-") && !leavesKingInCheck(row, col, row+direction, col)) {
            validMoves.add(new int[]{row+direction, col});
            if ((row == 1 && direction == 1) || (row == 6 && direction == -1)) {
                if (board[row+2*direction][col].equals("-") && !leavesKingInCheck(row, col, row+2*direction, col)) {
                    validMoves.add(new int[]{row+2*direction, col});
                }
            }
        }

        for (int i = -1; i <= 1; i += 2) {
            int newCol = col + i;
            if (newCol < 0 || newCol > 7) continue;
            if (!board[row+direction][newCol].equals("-") && getColor(row+direction, newCol) != color && !leavesKingInCheck(row, col, row+direction, newCol)) {
                validMoves.add(new int[]{row+direction, newCol});
            }
        }

        addValidEnPassant(row, col);
        return validMoves;
    }

    private void addValidEnPassant(int row, int col){
        int direction = (color == 'b') ? 1 : -1;
        if(!enPassant){
            return;
        }
        if(color == 'b'){
            if(row == enPassantCoordinates[0] && (col == enPassantCoordinates[1] - 1 || col == enPassantCoordinates[1] + 1) && !leavesEnPassantInCheck(row, col, row+1, enPassantCoordinates[1], direction)){
                validMoves.add(new int[]{enPassantCoordinates[0] + 1, enPassantCoordinates[1]});
            }
        }else if(color == 'w'){
            if(row == enPassantCoordinates[0] && (col == enPassantCoordinates[1] - 1 || col == enPassantCoordinates[1] + 1) && !leavesEnPassantInCheck(row, col, row-1, enPassantCoordinates[1], direction)){
                validMoves.add(new int[]{enPassantCoordinates[0] - 1, enPassantCoordinates[1]});
            }
        }
    }

    private boolean leavesEnPassantInCheck(int x, int y, int newX, int newY, int direction) {
        //pawn
        String pawn = board[x][y];
        String opponent = board[x][newY];
        board[x][y] = "-";
        board[newX][newY] = pawn;
        board[x][newY] = "-";
        boolean inCheck = isKingInCheck();
        board[newX][newY] = "-";
        board[x][y] = pawn;
        board[x][newY] = opponent;
        return inCheck;
    }


    private boolean leavesKingInCheck(int x, int y, int newX, int newY) {
        String temp = board[newX][newY];
        board[newX][newY] = board[x][y];
        board[x][y] = "-";
        boolean inCheck = isKingInCheck();
        board[x][y] = board[newX][newY];
        board[newX][newY] = temp;
        return inCheck;
    }

    private boolean isKingInCheck() {
        int kingX = kingCoordinates[0];
        int kingY = kingCoordinates[1];
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

    private char getColor(int row, int col){
        if(board[row][col].equals("-")){
            return 'n';
        }
        String color = board[row][col];
        return color.charAt(0);
    }

}
