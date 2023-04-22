package com.example.chess;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class chessController {

    private Board board = new Board();
    @FXML
    private GridPane boardGrid;
    private Button prevButton = null;
    private boolean inCheck = false;


    @FXML
    public void initialize() {
        boolean isWhite = true;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Button button = new Button();
                button.setPrefSize(70, 70);
                if (isWhite) {
                    button.getStyleClass().add("white-button");
                } else {
                    button.getStyleClass().add("black-button");
                }
                drawImages(button, row, col);
                boardGrid.add(button, col, row);
                isWhite = !isWhite;
                button.setOnAction(e -> handleButtonClick(button));
            }
            isWhite = !isWhite;
        }
    }

    private void drawImages(Button button, int row, int col) {
        String[][] state = board.getBoard();

        if (state[row][col].equals("-")) {
            button.setGraphic(null);
        } else {
            String imageUrl = System.getProperty("user.dir") + "/src/main/java/com/example/chess/pawns/" + state[row][col] + ".png";
            Image image = new Image(imageUrl);
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(50);
            imageView.setFitHeight(50);
            button.setGraphic(imageView);
        }
    }

    private void handleButtonClick(Button button) {
        int[] clickedButton = new int[]{GridPane.getRowIndex(button), GridPane.getColumnIndex(button)};
        if (prevButton == null) {
            if(board.getPiece(clickedButton).equals("-") || board.getTurn() != board.getPiece(clickedButton).charAt(0)){
                return;
            }
            List<int[]> validMoves = board.validMoves(clickedButton);
            prevButton = button;
            for(int[] validCoords : validMoves){
                displayValidMoves(validCoords[0], validCoords[1], board.getPiece(clickedButton));
            }
        } else {
            int [] previousClick = new int[]{GridPane.getRowIndex(prevButton), GridPane.getColumnIndex(prevButton)};
            List<int[]> validMoves = board.validMoves(previousClick);
            if(board.getPiece(clickedButton).charAt(0) == board.getTurn() && !Arrays.equals(previousClick, clickedButton)){
                prevButton = null;
                updateBoardGUI();
                handleButtonClick(button);
                return;
            }
            boolean isValidMove = false;
            for(int [] validCoords : validMoves){
                if(validCoords[0] == clickedButton[0] && validCoords[1] == clickedButton[1]){
                    board.setEnPassant(previousClick, clickedButton);
                    if(board.isPromotion(previousClick, clickedButton)){
                        showPromotionOptions(previousClick, clickedButton);
                    }else if(board.isMoveEnPassant(previousClick, clickedButton)){
                        board.swapEnPassant(previousClick, clickedButton);
                    }else{
                        board.swap(previousClick, clickedButton);
                    }
                    updateBoardGUI();
                    isValidMove = true;
                    break;
                }
            }
            if(isValidMove){
                board.swapTurn();
                inCheck = board.lookForChecks();
                String gameState = board.isGameOver();
                System.out.println(board.isGameOver());
                if(gameState.equals("checkmate") || gameState.equals("stalemate") || gameState.equals("insufficient material")){
                    displayGameOver(gameState);
                }
                board.updateCastlingVariables(previousClick, clickedButton);
                board.castle(previousClick, clickedButton);
            }
            updateBoardGUI();
            prevButton = null;
        }
    }

    private void updateBoardGUI() {
        for (Node node : boardGrid.getChildren()) {
            if (node instanceof Button button) {
                int row = GridPane.getRowIndex(button);
                int col = GridPane.getColumnIndex(button);
                drawImages(button, row, col);
            }
        }
    }
    private void displayValidMoves(int validRow, int validCol, String type) {
        for (Node node : boardGrid.getChildren()) {
            if (node instanceof Button button) {
                int row = GridPane.getRowIndex(button);
                int col = GridPane.getColumnIndex(button);
                if (row == validRow && col == validCol) {
                    if (button.getGraphic() != null) {
                        StackPane stackPane = new StackPane();
                        stackPane.getChildren().addAll(button.getGraphic(), new Circle(10, 10, 10, Color.RED));
                        button.setGraphic(stackPane);
                    } else {
                        Circle circle = new Circle(10, 10, 10, Color.RED);
                        button.setGraphic(circle);
                    }
                }
            }
        }
    }
    public void showPromotionOptions(int [] from, int [] to){
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setTitle("Promotion");
        alert.setHeaderText("Promote the pawn to: ");

        ButtonType queenButton = new ButtonType("Queen");
        ButtonType rookButton = new ButtonType("Rook");
        ButtonType bishopButton = new ButtonType("Bishop");
        ButtonType knightButton = new ButtonType("Knight");
        alert.getButtonTypes().setAll(queenButton, rookButton, bishopButton, knightButton);
        List<ButtonType> allButtons = alert.getButtonTypes();
        DialogPane dialogPane = alert.getDialogPane();

        for (ButtonType btn : allButtons){
            Node buttonNode = dialogPane.lookupButton(btn);
            if (buttonNode instanceof Button) {
                ((Button) buttonNode).setPrefHeight(70);
            }
        }

        Node header = alert.getDialogPane().lookup(".header-panel");
        header.setOnMousePressed(event -> header.setOnMouseDragged(event1 -> {
            alert.setX(event1.getScreenX() - event.getSceneX());
            alert.setY(event1.getScreenY() - event.getSceneY());
        }));

        Optional<ButtonType> result = alert.showAndWait();

        result.ifPresent(buttonType -> {
            if (buttonType == queenButton) {
                board.swapPromotion(from, to, "queen");
            } else if (buttonType == rookButton) {
                board.swapPromotion(from, to, "rook");
            } else if (buttonType == bishopButton) {
                board.swapPromotion(from, to, "bishop");
            } else if (buttonType == knightButton) {
                board.swapPromotion(from, to, "knight");
            }
        });
    }

    public void displayGameOver(String state){
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.initStyle(StageStyle.DECORATED);
        alert.setTitle("Game Over");
        char playerWon = board.getOpponentColor();
        String playerColor;
        if(playerWon == 'w' && state.equals("checkmate")){
            playerColor = "white";
            alert.setHeaderText(playerColor + " won by checkmate");
        }else if (playerWon == 'b' && state.equals("checkmate")){
            playerColor = "black";
            alert.setHeaderText(playerColor + " won by checkmate");
        } else if (state.equals("draw") || state.equals("stalemate")) {
            alert.setHeaderText("Draw by " + state);
        }

        ButtonType newGame = new ButtonType("New game");
        ButtonType mainMenu = new ButtonType("Back to menu");
        alert.getButtonTypes().setAll(newGame, mainMenu);
        List<ButtonType> allButtons = alert.getButtonTypes();
        DialogPane dialogPane = alert.getDialogPane();

        for (ButtonType btn : allButtons){
            Node buttonNode = dialogPane.lookupButton(btn);
            if (buttonNode instanceof Button) {
                ((Button) buttonNode).setPrefHeight(30);
            }
        }

        Node header = alert.getDialogPane().lookup(".header-panel");
        header.setOnMousePressed(event -> header.setOnMouseDragged(event1 -> {
            alert.setX(event1.getScreenX() - event.getSceneX());
            alert.setY(event1.getScreenY() - event.getSceneY());
        }));

        Optional<ButtonType> result = alert.showAndWait();

        result.ifPresent(buttonType -> {
            if (buttonType == newGame) {
                board = new Board();
            } else if (buttonType == mainMenu) {
                System.out.println("pinkkupinsku");
            }
        });
    }
}