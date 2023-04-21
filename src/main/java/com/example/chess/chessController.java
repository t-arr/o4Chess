package com.example.chess;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.StageStyle;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class chessController {

    private final Board BOARD = new Board();
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
        String[][] state = BOARD.getBoard();

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
            if(BOARD.getPiece(clickedButton).equals("-") || BOARD.getTurn() != BOARD.getPiece(clickedButton).charAt(0)){
                return;
            }
            List<int[]> validMoves = BOARD.validMoves(clickedButton);
            prevButton = button;
            for(int[] validCoords : validMoves){
                displayValidMoves(validCoords[0], validCoords[1]);
            }
        } else {
            int [] previousClick = new int[]{GridPane.getRowIndex(prevButton), GridPane.getColumnIndex(prevButton)};
            List<int[]> validMoves = BOARD.validMoves(previousClick);
            if(BOARD.getPiece(clickedButton).charAt(0) == BOARD.getTurn() && !Arrays.equals(previousClick, clickedButton)){
                prevButton = null;
                updateBoardGUI();
                handleButtonClick(button);
                return;
            }
            boolean isValidMove = false;
            for(int [] validCoords : validMoves){
                if(validCoords[0] == clickedButton[0] && validCoords[1] == clickedButton[1]){
                    BOARD.setEnPassant(previousClick, clickedButton);
                    if(BOARD.isPromotion(previousClick, clickedButton)){
                        showPromotionOptions(previousClick, clickedButton);
                    }else if(BOARD.isMoveEnPassant(previousClick, clickedButton)){
                        BOARD.swapEnPassant(previousClick, clickedButton);
                    }else{
                        BOARD.swap(previousClick, clickedButton);
                    }
                    updateBoardGUI();
                    isValidMove = true;
                    break;
                }
            }
            if(isValidMove){
                BOARD.swapTurn();
                inCheck = BOARD.lookForChecks();
                System.out.println(BOARD.isGameOver());
                BOARD.updateCastlingVariables(previousClick, clickedButton);
                BOARD.castle(previousClick, clickedButton);
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
    private void displayValidMoves(int validRow, int validCol) {
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
                BOARD.swapPromotion(from, to, "queen");
            } else if (buttonType == rookButton) {
                BOARD.swapPromotion(from, to, "rook");
            } else if (buttonType == bishopButton) {
                BOARD.swapPromotion(from, to, "bishop");
            } else if (buttonType == knightButton) {
                BOARD.swapPromotion(from, to, "knight");
            }
        });
    }
}