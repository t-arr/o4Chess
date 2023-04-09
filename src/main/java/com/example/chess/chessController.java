package com.example.chess;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.List;

public class chessController {
    private final Board BOARD = new Board();
    @FXML
    private GridPane boardGrid;

    private Button prevButton = null;

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
        boardGrid.setGridLinesVisible(true);
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
        int[] to = new int[]{GridPane.getRowIndex(button), GridPane.getColumnIndex(button)};
        if (prevButton == null) {
            if(BOARD.getPiece(to).equals("-") || BOARD.getTurn() != BOARD.getPiece(to).charAt(0)){
                return;
            }
            List<int[]> validMoves = BOARD.validMoves(to);
            prevButton = button;
            for(int[] validCoords : validMoves){
                displayValidMoves(validCoords[0], validCoords[1]);
            }
        } else {
            BOARD.swap(new int[]{GridPane.getRowIndex(prevButton), GridPane.getColumnIndex(prevButton)}, to);
            updateBoardGUI();
            BOARD.swapTurn();
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
                if(row == validRow && col == validCol){
                    if(button.getGraphic() != null){
                        Group group = new Group();
                        group.getChildren().addAll(button.getGraphic(), new Circle(15, 15, 15, Color.GREEN));
                        button.setGraphic(group);
                    }else{
                        Circle circle = new Circle(15, 15, 15, Color.GREEN);
                        button.setGraphic(circle);
                    }
                }
            }
        }
    }
}
