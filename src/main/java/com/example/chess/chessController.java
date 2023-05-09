package com.example.chess;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
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
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class chessController {

    private Board board;
    @FXML
    private GridPane boardGrid;
    @FXML
    private GridPane boardInformation;
    private Button prevButton = null;
    private boolean inCheck = false;
    private boolean hasGameBegun = false;
    private boolean playAgainstBot;
    private GameSetupForBot instance;
    private String gameMode;
    VeryBadBot bot;


    @FXML
    public void initialize() {
        instance = GameSetupForBot.getInstance();
        this.playAgainstBot = instance.getAgainstComp();
        gameMode = instance.getColor();
        board = new Board(gameMode, playAgainstBot);
        bot = new VeryBadBot(gameMode);
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
        Button startButton = new Button("Start");
        startButton.setOnAction(event -> handleStartClick(startButton));
        startButton.getStyleClass().add("startMenuButtons");
        GridPane.setConstraints(startButton, 1, 4, 3, 1);

        Button menuButton = new Button("Main menu");
        menuButton.setOnAction(event -> {
            try {
                goToMenu();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        menuButton.getStyleClass().add("startMenuButtons");
        GridPane.setConstraints(menuButton, 1, 5, 3, 1);

        GridPane.setMargin(startButton, new Insets(0, 0, 10, 0));
        GridPane.setMargin(menuButton, new Insets(10, 0, 0, 0));
        boardInformation.setGridLinesVisible(false);
        boardInformation.getChildren().addAll(startButton, menuButton);
    }

    private void handleStartClick(Button btn) {
        if(!hasGameBegun){
            hasGameBegun = true;
            btn.setText("restart");
            if(playAgainstBot && gameMode.equalsIgnoreCase("black")){
                board.swapTurn();
                board.setBotTurn(true);
                bot.makeMove(board.getBoard(), board, gameMode);
                board.setBotTurn(false);
                board.swapTurn();
                updateBoardGUI();
            }
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Do you want to restart the game?");
        alert.setTitle("restart?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            board = new Board(gameMode, playAgainstBot);
            if(playAgainstBot && gameMode.equalsIgnoreCase("black")){
                board.swapTurn();
                board.setBotTurn(true);
                bot.makeMove(board.getBoard(), board, gameMode);
                board.setBotTurn(false);
                board.swapTurn();
                updateBoardGUI();
            }
            updateBoardGUI();
        }
    }

    private void goToMenu() throws Exception {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Do you want to go back to menu?");
        alert.setTitle("Back to menu");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            executeMenuCommand();
        }
    }

    private void executeMenuCommand() throws Exception {
        Stage currentStage = (Stage) boardGrid.getScene().getWindow();
        ChessApplication ca = new ChessApplication();
        ca.start(new Stage());
        currentStage.close();
    }


    private void drawImages(Button button, int row, int col) {
        String[][] state = board.getBoard();

        if (state[row][col].equals("-")) {
            button.setGraphic(null);
        } else {
            String imageUrl;
            if(gameMode.equals("Black")){
                imageUrl = System.getProperty("user.dir") + "/src/main/java/com/example/chess/invertedPawns/" + state[row][col] + ".png";
            }else{
                imageUrl = System.getProperty("user.dir") + "/src/main/java/com/example/chess/pawns/" + state[row][col] + ".png";
            }
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
            if(board.getPiece(clickedButton).equals("-") || board.getTurn() != board.getPiece(clickedButton).charAt(0) || !hasGameBegun){
                return;
            }
            List<int[]> validMoves = board.validMoves(clickedButton);
            prevButton = button;
            for(int[] validCoords : validMoves){
                displayValidMoves(validCoords[0], validCoords[1], board.getPiece(clickedButton), clickedButton);
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
                if(gameState.equals("checkmate") || gameState.equals("stalemate") || gameState.equals("insufficient material")){
                    displayGameOver(gameState);
                    updateBoardGUI();
                    return;
                }
                board.updateCastlingVariables(previousClick, clickedButton);
                board.castle(previousClick, clickedButton);
                if(playAgainstBot){
                    board.setBotTurn(true);
                }
                updateBoardGUI();
                if(playAgainstBot){
                    bot.makeMove(board.getBoard(), board, gameMode);
                    board.swapTurn();
                    board.setBotTurn(false);
                    updateBoardGUI();
                    board.lookForChecks();
                    gameState = board.isGameOver();
                    if(gameState.equals("checkmate") || gameState.equals("stalemate") || gameState.equals("insufficient material")){
                        displayGameOver(gameState);
                        updateBoardGUI();
                        return;
                    }
                }
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
    private void displayValidMoves(int validRow, int validCol, String type, int [] coordinates) {
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
                        Circle circle;
                        if(type.substring(1).equals("king") && Math.abs(validCol-coordinates[1]) == 2){
                            circle = new Circle(10, 10, 10, Color.BLUE);
                        }else{
                            circle = new Circle(10, 10, 10, Color.RED);
                        }
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
            playerColor = gameMode.equalsIgnoreCase("black") ? "black" : "white";
            alert.setHeaderText(playerColor + " won by checkmate");
        }else if (playerWon == 'b' && state.equals("checkmate")){
            playerColor = gameMode.equalsIgnoreCase("black") ? "white" : "black";
            alert.setHeaderText(playerColor + " won by checkmate");
        } else if (state.equals("insufficient material") || state.equals("stalemate")) {
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
                board = new Board(gameMode, playAgainstBot);
                if(playAgainstBot && gameMode.equalsIgnoreCase("black")){
                    board.setBotTurn(true);
                    board.swapTurn();
                    bot.makeMove(board.getBoard(), board, gameMode);
                    board.setBotTurn(false);
                    board.swapTurn();
                    updateBoardGUI();
                }
            } else if (buttonType == mainMenu) {
                try {
                    executeMenuCommand();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}