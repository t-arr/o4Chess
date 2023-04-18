package com.example.chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class King {
    private char color;
    private String [][] board;
    private String [][] copyBoard;
    private boolean [] castlingList;
    private List<int[]> validMoves = new ArrayList<>();
    private List<int[]> validOpponentMoves;
    private Map<int[], String> threatList;
    private char opponentColor;
    private boolean isCheck;

    public King(char color, String [][] board, boolean [] castlingList, List<int[]> validOpponentMoves, Map<int[], String> threatList, char opponentColor, boolean isCheck){
        this.color = color;
        this.board = board;
        this.castlingList = castlingList;
        this.threatList = threatList;
        this.validOpponentMoves = validOpponentMoves;
        this.opponentColor = opponentColor;
        this.isCheck = isCheck;
    }

    public List<int[]> getValidMoves(int [] coords){
        int row = coords[0];
        int col = coords[1];
        addValidMoves(row, col);
        if(!isCheck){
            appendCastlingMoves();
        }
        return validMoves;
    }

    private void addValidMoves(int row, int col){
        int[] targetRow = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] targetCol = {-1, 0, 1, -1, 1, -1, 0, 1};
        for (int i = 0; i < 8; i++) {
            int newRow = row + targetRow[i];
            int newCol = col + targetCol[i];
            if (isValidPosition(newRow, newCol) && isNotSteppingToCheck(newRow, newCol, row, col) && isNotSteppingToPawnCheck(newRow, newCol) && isNotSteppingToKingCheck(newRow, newCol)) {
                if (board[newRow][newCol].equals("-") || getColor(newRow, newCol) != color) {
                    validMoves.add(new int[]{newRow, newCol});
                }
            }
        }
    }

    private boolean isNotSteppingToCheck(int row, int col, int oldRow, int oldCol){
        int [] newKingCoords = new int[]{row, col};
        copyBoard = copyGameState(board);
        copyBoard[row][col] = color + "king";
        copyBoard[oldRow][oldCol] = "-";
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if(board[i][j].charAt(0) == opponentColor && !board[i][j].substring(1).equals("king")){
                    List<int[]> oppMoves = isNewPositionValid(copyBoard[i][j], new int[]{i, j});
                    for (int [] moves : oppMoves){
                        if (Arrays.equals(moves, newKingCoords)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private List<int[]> isNewPositionValid(String type, int [] opponentPieceCoordinates){
        type = type.substring(1);
        switch (type) {
            case "pawn" -> {
                Pawn pawn = new Pawn(opponentColor, copyBoard, threatList);
                return pawn.getValidMoves(opponentPieceCoordinates);
            }
            case "rook" -> {
                Rook rook = new Rook(opponentColor, copyBoard, threatList);
                return rook.getValidMoves(opponentPieceCoordinates);
            }
            case "knight" -> {
                Knight knight = new Knight(opponentColor, copyBoard, threatList);
                return knight.getValidMoves(opponentPieceCoordinates);
            }
            case "bishop" -> {
                Bishop bishop = new Bishop(opponentColor, copyBoard, threatList);
                return bishop.getValidMoves(opponentPieceCoordinates);
            }
            case "queen" -> {
                Queen queen = new Queen(opponentColor, copyBoard, threatList);
                return queen.getValidMoves(opponentPieceCoordinates);
            }
            default -> {
                return new ArrayList<>();
            }
        }
    }

    public static String[][] copyGameState(String[][] input) {
        if (input == null) {
            return null;
        }
        String[][] result = new String[input.length][];
        for (int i = 0; i < input.length; i++) {
            result[i] = Arrays.copyOf(input[i], input[i].length);
        }
        return result;
    }

    private boolean isNotSteppingToPawnCheck(int row, int col) {
        if(validOpponentMoves == null){
            return true;
        }
        if(color == 'b' && row + 1 < 8 && col - 1 >= 0 && col + 1 < 8){
            return !board[row + 1][col - 1].equals("wpawn") && !board[row + 1][col + 1].equals("wpawn");
        }
        if (color == 'w' && row - 1 >= 0 && col - 1 >= 0 && col + 1 < 8) {
            return !board[row - 1][col - 1].equals("bpawn") && !board[row - 1][col + 1].equals("bpawn");
        }
        if (color == 'b' && row + 1 < 8 && col == 0) {
            return !board[row + 1][col + 1].equals("wpawn");
        }
        if (color == 'b' && row + 1 < 8 && col == 7) {
            return !board[row + 1][col - 1].equals("wpawn");
        }
        if (color == 'w' && row - 1 >= 0 && col == 0) {
            return !board[row - 1][col + 1].equals("bpawn");
        }
        if (color == 'w' && row - 1 >= 0 && col == 7) {
            return !board[row - 1][col - 1].equals("bpawn");
        }
        return true;
    }

    private boolean isNotSteppingToKingCheck(int row, int col){
        double firstDistance = calculateDistance(0, 0, 1, 1);
        double secondDistance = calculateDistance(0, 0, 0, 1);
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if(calculateDistance(row, col, i, j) == firstDistance || calculateDistance(row, col, i, j) == secondDistance){
                    if(board[i][j].substring(1).equals("king") && color != board[i][j].charAt(0)){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private double calculateDistance(double firstX, double firstY, double secondX, double secondY) {
        return Math.sqrt(Math.pow((secondX - firstX), 2) + Math.pow((secondY - firstY), 2));
    }

    private boolean isValidPosition(int row, int col) {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }

    private void appendCastlingMoves(){
        copyBoard = copyGameState(board);
        if(color == 'b'){
            if(castlingList[0]){
                if(board[0][1].equals("-") && board[0][2].equals("-") && board[0][3].equals("-")){
                    test(0, 2, 0, 3, "bking", "brook");
                    if(testValidCastling(copyBoard, 0, 2)){
                        validMoves.add(new int[]{0, 2});
                    }
                }
            }
            if (castlingList[1]){
                if(board[0][6].equals("-") && board[0][5].equals("-")){
                    test(0, 6, 0, 5, "bking", "brook");
                    if(testValidCastling(copyBoard, 0, 6)){
                        validMoves.add(new int[]{0, 6});
                    }
                }
            }
        }else{
            if(castlingList[2]){
                if(board[7][1].equals("-") && board[7][2].equals("-") && board[7][3].equals("-")){
                    test(7, 2, 7, 3, "wking", "wrook");
                    if(testValidCastling(copyBoard, 7, 2)){
                        validMoves.add(new int[]{7, 2});
                    }
                }
            }
            if (castlingList[3]){
                if(board[7][6].equals("-") && board[7][5].equals("-")){
                    test(7, 6, 7, 5, "wking", "wrook");
                    if(testValidCastling(copyBoard, 7, 6)){
                        validMoves.add(new int[]{7, 6});
                    }
                }
            }
        }
    }

    private boolean testValidCastling(String [][] copyBoard, int row, int col){
        int[] kingCoordinates = new int[]{row, col};
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if(copyBoard[i][j].charAt(0) == opponentColor && !board[i][j].substring(1).equals("king")){
                    List<int[]> oppMoves = isNewPositionValid(copyBoard[i][j], new int[]{i, j});
                    for (int [] moves : oppMoves){
                        if (Arrays.equals(moves, kingCoordinates)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private void test(int kr, int kc, int rr, int rc, String kingType, String rookType){
        copyBoard[kr][kc] = kingType;
        copyBoard[rr][rc] = rookType;
        copyBoard[kr][4] = "-";
    }

    private char getColor(int row, int col){
        if(board[row][col].equals("-")){
            return 'n';
        }
        String color = board[row][col];
        return color.charAt(0);
    }
}
