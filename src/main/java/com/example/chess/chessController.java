package com.example.chess;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;
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
    private boolean hasGameBegun = false;
    private boolean playAgainstBot;
    private GameSetupForBot instance;
    private int upperImageRow = 1;
    private int upperImageCol = 0;
    private int lowerImageRow = 6;
    private int lowerImageCol = 0;
    private String gameMode;
    private String whitePlayerName;
    private String blackPlayerName;
    VeryBadBot bot;


    @FXML
    public void initialize() {
        instance = GameSetupForBot.getInstance();
        this.whitePlayerName = instance.getWhitePieces();
        this.blackPlayerName = instance.getBlackPieces();
        this.playAgainstBot = instance.getAgainstComp();
        gameMode = instance.getColor();
        board = new Board(gameMode, playAgainstBot);
        bot = new VeryBadBot();
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
        for (int i = 0; i < 5; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPercentWidth(20); // set the width of each column to 20% of the grid
            boardInformation.getColumnConstraints().add(column);
        }

        // Add row constraints
        for (int i = 0; i < 10; i++) {
            RowConstraints row = new RowConstraints();
            row.setPercentHeight(10); // set the height of each row to 10% of the grid
            boardInformation.getRowConstraints().add(row);
        }

        Label playerLabels = new Label("White: " + whitePlayerName + "  Black: " + blackPlayerName);
        playerLabels.getStyleClass().add("board-information-label");
        GridPane.setConstraints(playerLabels, 0, 0, 5, 1);
        boardInformation.getChildren().add(playerLabels);

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
            correctColoringAgainstWhiteBot();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Do you want to restart the game?");
        alert.setTitle("restart?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            board = new Board(gameMode, playAgainstBot);
            correctColoringAgainstWhiteBot();
            prevButton = null;
            clearBoardInformation();
            upperImageRow = 1;
            upperImageCol = 0;
            lowerImageRow = 6;
            lowerImageCol = 0;
            updateBoardGUI();
        }
    }

    //handles correct coloring when playing with black pieces
    private void correctColoringAgainstWhiteBot(){
        if(playAgainstBot && gameMode.equalsIgnoreCase("black")){
            board.swapTurn();
            bot.makeMove(board.getBoard(), board);
            board.swapTurn();
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

    //Handles movement during the game by keeping track of clicked button and previously clicked button
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
                        addImageToBoardInformation(board.getPiece(clickedButton));
                        showPromotionOptions(previousClick, clickedButton);
                    }else if(board.isMoveEnPassant(previousClick, clickedButton)){
                        addImageToBoardInformation(board.getOpponentColor() + "pawn");
                        board.swapEnPassant(previousClick, clickedButton);
                    } else if (board.getPiece(previousClick).substring(1).equalsIgnoreCase("king") && isMoveCastling(previousClick, clickedButton)) {
                        board.swapCastle(previousClick, clickedButton);
                        board.swap(previousClick, clickedButton);
                    } else{
                        addImageToBoardInformation(board.getPiece(clickedButton));
                        board.swap(previousClick, clickedButton);
                    }
                    updateBoardGUI();
                    isValidMove = true;
                    break;
                }
            }
            if(isValidMove){
                board.swapTurn();
                board.lookForChecks();
                String gameState = board.isGameOver();
                if(gameState.equals("checkmate") || gameState.equals("stalemate") || gameState.equals("insufficient material")){
                    displayGameOver(gameState);
                    updateBoardGUI();
                    return;
                }
                board.updateCastlingVariables(previousClick, clickedButton);
                updateBoardGUI();
                //Handle opponent movement when playing against a bot
                if(playAgainstBot){
                    bot.makeMove(board.getBoard(), board);
                    board.swapTurn();
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

    private boolean isMoveCastling(int[] from, int [] to){
        return Math.abs(from[1]-to[1]) == 2;
    }

    //iterates board and calls draw images to update view according to player movement
    private void updateBoardGUI() {
        for (Node node : boardGrid.getChildren()) {
            if (node instanceof Button button) {
                int row = GridPane.getRowIndex(button);
                int col = GridPane.getColumnIndex(button);
                drawImages(button, row, col);
            }
        }
    }

    private void addImageToBoardInformation(String piece){
        if(!piece.equalsIgnoreCase("-")){
            String imageUrl = System.getProperty("user.dir") + "/src/main/java/com/example/chess/pawns/" + piece + ".png";
            ImageView imageView = new ImageView(imageUrl);
            imageView.setFitWidth(50);
            imageView.setFitHeight(50);
            if(piece.charAt(0) == 'w'){
                GridPane.setConstraints(imageView, upperImageCol, upperImageRow);
                boardInformation.getChildren().addAll(imageView);
                if(upperImageCol == 4){
                    upperImageCol = 0;
                    upperImageRow++;
                    return;
                }
                upperImageCol++;
            }
            if(piece.charAt(0) == 'b'){
                GridPane.setConstraints(imageView, lowerImageCol, lowerImageRow);
                boardInformation.getChildren().addAll(imageView);
                if(lowerImageCol == 4){
                    lowerImageCol = 0;
                    lowerImageRow++;
                    return;
                }
                lowerImageCol++;
            }
        }
    }

    private void clearBoardInformation() {
        List<Node> nodesToRemove = new ArrayList<>();
        for (Node node : boardInformation.getChildren()) {
            if (!(node instanceof Button) && !(node instanceof Label)) {
                nodesToRemove.add(node);
            }
        }
        boardInformation.getChildren().removeAll(nodesToRemove);
    }

    //displays valid moves of certain pawn by drawing circle on board
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
                        //if king can castle -> display movement as blue circle to differentiate movement
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

    //handles promoting
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

    //handles situations when game is over
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
                prevButton = null;
                clearBoardInformation();
                upperImageRow = 1;
                upperImageCol = 0;
                lowerImageRow = 6;
                lowerImageCol = 0;
                correctColoringAgainstWhiteBot();
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