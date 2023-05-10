package com.example.chess;

import java.util.*;

public class Board {
    private String[][] board;
    private char turn = 'w';
    private boolean[] castlingList;
    private boolean validEnPassant;
    private int[] enPassantCoordinates = new int[]{0, 0};
    private boolean isCheck = false;
    private String gameMode;
    private boolean playAgainstBot;

    public Board(String gameMode, boolean playAgainstBot) {
        this.gameMode = gameMode;
        this.playAgainstBot = playAgainstBot;
        if(gameMode.equalsIgnoreCase("black")){
            this.board = new String[][]{{"brook", "bknight", "bbishop", "bking", "bqueen", "bbishop", "bknight", "brook"},
                    {"bpawn", "bpawn", "bpawn", "bpawn", "bpawn", "bpawn", "bpawn", "bpawn"},
                    {"-", "-", "-", "-", "-", "-", "-", "-"},
                    {"-", "-", "-", "-", "-", "-", "-", "-"},
                    {"-", "-", "-", "-", "-", "-", "-", "-"},
                    {"-", "-", "-", "-", "-", "-", "-", "-"},
                    {"wpawn", "wpawn", "wpawn", "wpawn", "wpawn", "wpawn", "wpawn", "wpawn"},
                    {"wrook", "wknight", "wbishop", "wking", "wqueen", "wbishop", "wknight", "wrook"}};
        }else{
            this.board = new String[][]{{"brook", "bknight", "bbishop", "bqueen", "bking", "bbishop", "bknight", "brook"},
                    {"bpawn", "bpawn", "bpawn", "bpawn", "bpawn", "bpawn", "bpawn", "bpawn"},
                    {"-", "-", "-", "-", "-", "-", "-", "-"},
                    {"-", "-", "-", "-", "-", "-", "-", "-"},
                    {"-", "-", "-", "-", "-", "-", "-", "-"},
                    {"-", "-", "-", "-", "-", "-", "-", "-"},
                    {"wpawn", "wpawn", "wpawn", "wpawn", "wpawn", "wpawn", "wpawn", "wpawn"},
                    {"wrook", "wknight", "wbishop", "wqueen", "wking", "wbishop", "wknight", "wrook"}};
        }
        this.castlingList = new boolean[]{true, true, true, true};
        validEnPassant = false;
    }

    public String[][] getBoard() {
        return board;
    }

    public String getPiece(int[] coords) {
        return board[coords[0]][coords[1]];
    }

    public int[] getKingCoordinates() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j].equals(getTurn() + "king")) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    public char getTurn() {
        return turn;
    }

    public char getOpponentColor() {
        if (getTurn() == 'w') {
            return 'b';
        }
        return 'w';
    }

    public void swapTurn() {
        if (turn == 'w') {
            turn = 'b';
        } else {
            turn = 'w';
        }
    }

    public void swap(int[] from, int[] to) {
        String temp = board[from[0]][from[1]];
        board[from[0]][from[1]] = "-";
        board[to[0]][to[1]] = temp;
    }

    //Promotion methods
    public void swapPromotion(int[] from, int[] to, String wantedPiece) {
        char color = getPiece(from).charAt(0);
        board[to[0]][to[1]] = color + wantedPiece;
        board[from[0]][from[1]] = "-";
    }

    public boolean isPromotion(int[] from, int[] to) {
        String piece = getPiece(from).substring(1);
        char color = getPiece(from).charAt(0);
        if (piece.equals("pawn") && color == 'b' && to[0] == 7) {
            return true;
        } else return piece.equals("pawn") && color == 'w' && to[0] == 0;
    }

    //Castling methods
    public void swapCastle(int[] from, int[] to) {
        int fromRow = from[0];
        int fromCol = from[1];
        int toRow = to[0];
        int toCol = to[1];
        //updates castling when bot is white
        if(gameMode.equalsIgnoreCase("black")){
            if (toCol < fromCol) {
                String tmp = board[fromRow][0];
                board[fromRow][0] = "-";
                board[fromRow][2] = tmp;
            } else {
                String tmp = board[fromRow][7];
                board[fromRow][7] = "-";
                board[fromRow][4] = tmp;
            }

        }else{
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

    public void updateCastlingVariables(int[] from, int[] to) {

        updateKingCastlingVariables(from, to);

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

    private void updateKingCastlingVariables(int [] from, int [] to){
        if(gameMode.equalsIgnoreCase("black")){
            if (Arrays.equals(from, new int[]{0, 3}) && (!Arrays.equals(to, new int[]{0, 1}) || !Arrays.equals(to, new int[]{0, 5}))) {
                castlingList[0] = false;
                castlingList[1] = false;
            }
            if (Arrays.equals(from, new int[]{7, 3}) && (!Arrays.equals(to, new int[]{7, 1}) || !Arrays.equals(to, new int[]{7, 5}))) {
                castlingList[2] = false;
                castlingList[3] = false;
            }
        }else{
            if (Arrays.equals(from, new int[]{0, 4}) && (!Arrays.equals(to, new int[]{0, 2}) || !Arrays.equals(to, new int[]{0, 6}))) {
                castlingList[0] = false;
                castlingList[1] = false;
            }
            if (Arrays.equals(from, new int[]{7, 4}) && (!Arrays.equals(to, new int[]{7, 2}) || !Arrays.equals(to, new int[]{7, 6}))) {
                castlingList[2] = false;
                castlingList[3] = false;
            }
        }
    }

    //En passant methods
    public void swapEnPassant(int[] from, int[] to) {
        char color = getPiece(from).charAt(0);
        String temp = board[from[0]][from[1]];
        board[from[0]][from[1]] = "-";
        board[to[0]][to[1]] = temp;
        if (color == 'b') {
            board[to[0] - 1][to[1]] = "-";
        } else {
            board[to[0] + 1][to[1]] = "-";
        }
    }

    public void setEnPassant(int[] from, int[] to) {
        String type = getPiece(from).substring(1);
        System.out.println();
        if (type.equals("pawn") && (Math.abs(from[0] - to[0]) == 2)) {
            validEnPassant = true;
            enPassantCoordinates = new int[]{to[0], to[1]};
        } else {
            validEnPassant = false;
        }
    }

    public boolean isMoveEnPassant(int[] from, int[] to) {
        String type = getPiece(from).substring(1);
        return type.equals("pawn") && getPiece(to).equals("-") && from[1] != to[1];
    }

    //Retruns all legal moves from selected coordinates
    public List<int[]> validMoves(int[] coords) {
        String type = getPiece(coords);
        char color = type.charAt(0);
        char opponentColor = getOpponentColor();
        type = type.substring(1);
        switch (type) {
            case "pawn" -> {
                Pawn pawn = new Pawn(color, board, validEnPassant, enPassantCoordinates, opponentColor, getKingCoordinates());
                return pawn.getValidMoves(coords);
            }
            case "rook" -> {
                Rook rook = new Rook(color, board, opponentColor, getKingCoordinates());
                return rook.getValidMoves(coords);
            }
            case "knight" -> {
                Knight knight = new Knight(color, board, opponentColor, getKingCoordinates());
                return knight.getValidMoves(coords);
            }
            case "bishop" -> {
                Bishop bishop = new Bishop(color, board, opponentColor, getKingCoordinates());
                return bishop.getValidMoves(coords);
            }
            case "queen" -> {
                Queen queen = new Queen(color, board, opponentColor, getKingCoordinates());
                return queen.getValidMoves(coords);
            }
            case "king" -> {
                King king = new King(color, board, castlingList, opponentColor, isCheck, gameMode, this, playAgainstBot);
                return king.getValidMoves(coords);
            }
            default -> {
                return new ArrayList<>();
            }
        }
    }

    //Updates isCheck variable if king is in check
    public void lookForChecks() {
        int[] kingCoordinates = getKingCoordinates();
        int row = kingCoordinates[0];
        int col = kingCoordinates[1];
        char color = getTurn();
        char oppColor = getOpponentColor();
        King k = new King(color, board, castlingList, oppColor, false, gameMode, this, playAgainstBot);
        isCheck = k.isKingInCheck(row, col);
    }

    //Returns empty string if game is not over or string that tells why game is over
    public String isGameOver() {
        boolean isDraw = true;
        List<int[]> validOpponentMoves = new ArrayList<>();
        List<String> piecesOnBoard = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                int[] coordinates = new int[]{i, j};
                if(!board[i][j].equals("-")){
                    piecesOnBoard.add(board[i][j]);
                }
                if (getPiece(coordinates).charAt(0) == getTurn() && !board[i][j].equals("-")) {
                    validOpponentMoves.addAll(validMoves(coordinates));
                }
            }
        }
        if (validOpponentMoves.isEmpty() && isCheck) {
            return "checkmate";
        }
        for(String piece : piecesOnBoard){
            if (!piece.equals("wking") && !piece.equals("bking")) {
                isDraw = false;
                break;
            }
        }
        if (validOpponentMoves.isEmpty()) {
            return "stalemate";
        }
        if(isDraw){
            return "insufficient material";
        }
        return "";
    }
}