package com.example.chess;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class chessController {
    @FXML
    private GridPane boardGrid;
    private Board board = new Board();

    @FXML
    public void initialize() {
        String[][] state = board.getBoard();
        boolean isWhite = true;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                boardGrid.setGridLinesVisible(true);
                Button button = new Button();
                button.setPrefSize(70, 70);
                if (isWhite) {
                    button.getStyleClass().add("white-button");
                } else {
                    button.getStyleClass().add("black-button");
                }
                if(!state[row][col].equals("-")){
                    String imageUrl = System.getProperty("user.dir") + "/src/main/java/com/example/chess/pawns/" + state[row][col] + ".png";
                    Image image = new Image(imageUrl);
                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(50);
                    imageView.setFitHeight(50);
                    button.setGraphic(imageView);
                }
                boardGrid.add(button, col, row);
                isWhite = !isWhite;
                button.setOnAction(e -> printCoordinates(button));
            }
            isWhite = !isWhite;
        }
    }
    private void printCoordinates(Button button) {
        int row = GridPane.getRowIndex(button);
        int col = GridPane.getColumnIndex(button);
        System.out.println("Button pressed at row " + row + ", column " + col);
    }
}
