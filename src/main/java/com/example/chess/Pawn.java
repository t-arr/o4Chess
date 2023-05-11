package com.example.chess;

import java.util.ArrayList;
import java.util.List;

public class Pawn{

    private char color;
    private String [][] board;
    private List<int[]> validMoves = new ArrayList<>();
    private boolean enPassant;
    private int [] enPassantCoordinates;
    private char opponentColor;
    private int [] kingCoordinates;

    public Pawn(char color, String [][] board, boolean enPassant, int [] enPassantCoordinates, char opponentColor, int [] kingCoordinates){
        this.color = color;
        this.board = board;
        this.enPassant = enPassant;
        this.enPassantCoordinates = new int[]{enPassantCoordinates[0], enPassantCoordinates[1]};
        this.opponentColor = opponentColor;
        this.kingCoordinates = kingCoordinates;

    }

    //retruns all valid moves that a pawn can make based on their color
    public List<int[]> getValidMoves(int [] coords){
        int row = coords[0];
        int col = coords[1];
        int direction = (color == 'b') ? 1 : -1;

        if (board[row+direction][col].equals("-")) {
            validMoves.add(new int[]{row+direction, col});
            if ((row == 1 && direction == 1) || (row == 6 && direction == -1)) {
                if (board[row+2*direction][col].equals("-")) {
                    validMoves.add(new int[]{row+2*direction, col});
                }
            }
        }

        for (int i = -1; i <= 1; i += 2) {
            int newCol = col + i;
            if (newCol < 0 || newCol > 7) continue;
            if (!board[row+direction][newCol].equals("-") && getColor(row+direction, newCol) != color) {
                validMoves.add(new int[]{row+direction, newCol});
            }
        }
        List<int[]> movesToRemove = new ArrayList<>();
        for (int[] move : validMoves) {
            if (leavesKingInCheck(row, col, move[0], move[1])) {
                movesToRemove.add(move);
            }
        }
        validMoves.removeAll(movesToRemove);

        addValidEnPassant(row, col);
        return validMoves;
    }

    //Appends valid en passant move to valid moves if detected
    private void addValidEnPassant(int row, int col){
        int direction = (color == 'b') ? 1 : -1;
        if(!enPassant){
            return;
        }
        if(color == 'b'){
            if(row == enPassantCoordinates[0] && (col == enPassantCoordinates[1] - 1 || col == enPassantCoordinates[1] + 1) && !leavesEnPassantInCheck(row, col, row+1, enPassantCoordinates[1])){
                validMoves.add(new int[]{enPassantCoordinates[0] + 1, enPassantCoordinates[1]});
            }
        }else if(color == 'w'){
            if(row == enPassantCoordinates[0] && (col == enPassantCoordinates[1] - 1 || col == enPassantCoordinates[1] + 1) && !leavesEnPassantInCheck(row, col, row-1, enPassantCoordinates[1])){
                validMoves.add(new int[]{enPassantCoordinates[0] - 1, enPassantCoordinates[1]});
            }
        }
    }

    //Swaps temporarily the board to see if possible en passant is in fact valid by calling is kingincheck method
    private boolean leavesEnPassantInCheck(int row, int col, int newRow, int newCol) {
        String pawn = board[row][col];
        String opponent = board[row][newCol];
        board[row][col] = "-";
        board[newRow][newCol] = pawn;
        board[row][newCol] = "-";
        boolean inCheck = isKingInCheck();
        board[newRow][newCol] = "-";
        board[row][col] = pawn;
        board[row][newCol] = opponent;
        return inCheck;
    }

    //Swaps temporarily the board to see if the move is in fact valid by calling is kingincheck method
    private boolean leavesKingInCheck(int row, int col, int newRow, int newCol) {
        String temp = board[newRow][newCol];
        board[newRow][newCol] = board[row][col];
        board[row][col] = "-";
        boolean inCheck = isKingInCheck();
        board[row][col] = board[newRow][newCol];
        board[newRow][newCol] = temp;
        return inCheck;
    }

    private boolean isKingInCheck() {
        int kingRow = kingCoordinates[0];
        int kingCol = kingCoordinates[1];
        int pawnDir = color == 'w' ? -1 : 1;
        int[][] pawnMoves = {{pawnDir, -1}, {pawnDir, 1}};
        for (int[] move : pawnMoves) {
            int dx = move[0];
            int dy = move[1];
            int newRow = kingRow + dx;
            int newCol = kingCol + dy;
            if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8 && board[newRow][newCol].startsWith(opponentColor + "pa")) {
                return true;
            }
        }

        int[][] knightMoves = {{-2, -1}, {-2, 1}, {-1, -2}, {-1, 2}, {1, -2}, {1, 2}, {2, -1}, {2, 1}};
        for (int[] move : knightMoves) {
            int dx = move[0];
            int dy = move[1];
            int newRow = kingRow + dx;
            int newCol = kingCol + dy;
            if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8 && board[newRow][newCol].startsWith(opponentColor + "kn")) {
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
                int newRow = kingRow + dx;
                int newCol = kingCol + dy;
                while (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8) {
                    String piece = board[newRow][newCol];
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
                    newRow += dx;
                    newCol += dy;
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
