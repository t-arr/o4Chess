package com.example.chess;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
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
        }else {
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
        if(prevButton == null){
            System.out.println("row: " + GridPane.getRowIndex(button));
            System.out.println("Column: " + GridPane.getColumnIndex(button));
            System.out.println();
            System.out.println();
            List<int[]> arrays = BOARD.validMoves(to);
            for (int[] array : arrays) {
                System.out.print("[ ");
                for (int num : array) {
                    System.out.print(num + " ");
                }
                System.out.println("]");
            }
            prevButton = button;
        }else{
            BOARD.swap(new int[]{GridPane.getRowIndex(prevButton), GridPane.getColumnIndex(prevButton)}, to);
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
}
