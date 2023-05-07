package com.example.chess;

import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

import java.io.IOException;

import java.util.List;
import java.util.Optional;

public class MainMenuController {
    @FXML
    public GridPane menuGrid;
    private HostServices hostServices;
    private GameSetupForBot instance;

    @FXML
    public void initialize(){
        Button playButton = new Button("Play");
        playButton.setId("play");
        Button helpButton = new Button("Help");
        helpButton.setId("help");
        Button rulesButton = new Button("Rules of chess");
        rulesButton.setId("rules");
        Button quitButton = new Button("Quit");
        quitButton.setId("quit");
        List<Button> menuBtns = List.of(playButton, helpButton, rulesButton, quitButton);
        menuGrid.add(playButton, 0, 1);
        menuGrid.add(helpButton, 0, 2);
        menuGrid.add(rulesButton, 0, 3);
        menuGrid.add(quitButton, 0, 4);
        for(Button btn : menuBtns){
            btn.getStyleClass().add("buttons");
            btn.setOnAction(event -> {
                try {
                    switchScreen(event);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        this.instance = GameSetupForBot.getInstance("White", "10 minutes", false);
    }
    @FXML
    public void switchScreen(ActionEvent event) throws IOException {
        String btnId = ((Button) event.getSource()).getId();
        if(btnId.equalsIgnoreCase("quit")){
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirm Exit");
            alert.setHeaderText("Are you sure you want to quit the game?");
            alert.setContentText("Press OK to exit, or Cancel to go back.");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK){
                Platform.exit();
            }
        } else if (btnId.equalsIgnoreCase("rules")) {
            hostServices.showDocument("https://en.wikipedia.org/wiki/Rules_of_chess");
        } else if (btnId.equalsIgnoreCase("help")) {
            VBox helpBox = new VBox();
            helpBox.getStyleClass().add("help-box");
            BorderPane helpBoxContainer = new BorderPane();
            helpBoxContainer.setCenter(helpBox);
            helpBoxContainer.getStyleClass().add("helpBoxContainer");
            Label howToPlay = new Label("How to play?");
            howToPlay.getStyleClass().add("question");
            howToPlay.setWrapText(true);
            helpBox.getChildren().add(howToPlay);
            Label howToPlayAnswer = new Label("From menu, click on the button named 'Play', then set up your game preferences and click 'Start game'. Now you should see a game view. Press start to initialize game and notice that white moves first.");
            howToPlayAnswer.getStyleClass().add("answer");
            howToPlayAnswer.setWrapText(true);
            helpBox.getChildren().add(howToPlayAnswer);
            Label howToMovePiece = new Label("How to move pieces?");
            howToMovePiece.getStyleClass().add("question");
            howToMovePiece.setWrapText(true);
            helpBox.getChildren().add(howToMovePiece);
            Label howToMovePieceAnswer = new Label("To move a piece you should first click a piece to be moved, then all the red dots that appear on the screen are valid squares where you can move. Now click one of them to successfully move a piece.");
            howToMovePieceAnswer.getStyleClass().add("answer");
            howToMovePieceAnswer.setWrapText(true);
            helpBox.getChildren().add(howToMovePieceAnswer);
            Label howChessIsPlayed = new Label("How chess works?");
            howChessIsPlayed.getStyleClass().add("question");
            howChessIsPlayed.setWrapText(true);
            helpBox.getChildren().add(howChessIsPlayed);
            Label howChessIsPlayedAnswer = new Label("Click 'Rules of chess' button in main menu to understand how the game is played. Make sure you are connected to the internet.");
            howChessIsPlayedAnswer.getStyleClass().add("answer");
            howChessIsPlayedAnswer.setWrapText(true);
            helpBox.getChildren().add(howChessIsPlayedAnswer);
            Label howToQuit = new Label("How to quit?");
            howToQuit.getStyleClass().add("question");
            howToQuit.setWrapText(true);
            helpBox.getChildren().add(howToQuit);
            Label howToQuitAnswer = new Label("To quit you can click the X on the right upper corner or by clicking quit in the main menu");
            howToQuitAnswer.getStyleClass().add("answer");
            howToQuitAnswer.setWrapText(true);
            helpBox.getChildren().add(howToQuitAnswer);
            Button closeButton = new Button("Back to menu");
            closeButton.setOnAction(closeEvent -> menuGrid.getChildren().remove(helpBoxContainer));
            closeButton.getStyleClass().add("close-button");
            helpBoxContainer.setBottom(closeButton);
            BorderPane.setAlignment(closeButton, Pos.CENTER);
            BorderPane.setMargin(closeButton, new Insets(0, 0, 10, 0));
            menuGrid.add(helpBoxContainer, 0, 0, 10, 10);
        }else{
            showGameOptions();
        }
    }

    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }
    public void showGameOptions(){
        VBox buttonBox = new VBox();
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setSpacing(20);

        Button twoPlayersButton = new Button("Two Players");
        twoPlayersButton.setOnAction(event -> {
            try {
                instance.setAgainstComp(false);
                launchGame();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        Button playAgainstComputerButton = new Button("Play against Computer");
        playAgainstComputerButton.setOnAction(event -> setupAgainstBot());
        twoPlayersButton.getStyleClass().add("buttons");
        playAgainstComputerButton.getStyleClass().add("buttons");
        Button closeButton = new Button("Back to menu");
        closeButton.getStyleClass().add("close-button");
        buttonBox.getChildren().addAll(twoPlayersButton, playAgainstComputerButton, closeButton);
        VBox helpBox = new VBox();
        helpBox.getStyleClass().add("help-box");
        helpBox.setAlignment(Pos.CENTER);
        helpBox.getChildren().add(buttonBox);
        BorderPane sidebarContainer = new BorderPane();
        sidebarContainer.setCenter(helpBox);
        closeButton.setOnAction(closeEvent -> menuGrid.getChildren().remove(sidebarContainer));
        menuGrid.add(sidebarContainer, 0, 0, 10, 10);
    }

    public void setupAgainstBot() {
        StackPane container = new StackPane();
        container.getStyleClass().add("helpBoxContainer");
        VBox setupBox = new VBox();
        setupBox.setAlignment(Pos.CENTER);
        setupBox.setSpacing(50);
        Label piecesLabel = new Label("Pieces to play with");
        piecesLabel.getStyleClass().add("section-label");
        HBox piecesBox = new HBox();
        piecesBox.setAlignment(Pos.CENTER);
        piecesBox.setSpacing(20);
        ToggleGroup piecesToggleGroup = new ToggleGroup();
        RadioButton whiteButton = new RadioButton("White");
        whiteButton.setSelected(true);
        whiteButton.setToggleGroup(piecesToggleGroup);
        RadioButton blackButton = new RadioButton("Black");
        blackButton.setToggleGroup(piecesToggleGroup);
        piecesBox.getChildren().addAll(whiteButton, blackButton);
        VBox piecesSectionBox = new VBox();
        piecesSectionBox.getStyleClass().add("section-boxes");
        piecesSectionBox.getChildren().addAll(piecesLabel, piecesBox);
        Label durationLabel = new Label("Choose game duration");
        durationLabel.getStyleClass().add("section-label");
        HBox durationBox = new HBox();
        durationBox.setAlignment(Pos.CENTER);
        durationBox.setSpacing(20);
        ToggleGroup durationToggleGroup = new ToggleGroup();
        RadioButton fiveMinButton = new RadioButton("5 minutes");
        fiveMinButton.setToggleGroup(durationToggleGroup);
        RadioButton tenMinButton = new RadioButton("10 minutes");
        tenMinButton.setSelected(true);
        tenMinButton.setToggleGroup(durationToggleGroup);
        RadioButton fifteenMinButton = new RadioButton("15 minutes");
        fifteenMinButton.setToggleGroup(durationToggleGroup);
        durationBox.getChildren().addAll(fiveMinButton, tenMinButton, fifteenMinButton);
        RadioButton [] rbList = new RadioButton[]{whiteButton, blackButton, fiveMinButton, tenMinButton, fifteenMinButton};
        for(RadioButton rb : rbList){
            rb.getStyleClass().add("radio-buttons");
        }
        VBox durationSectionBox = new VBox();
        durationSectionBox.getStyleClass().add("section-boxes");
        durationSectionBox.getChildren().addAll(durationLabel, durationBox);
        setupBox.getChildren().addAll(piecesSectionBox, durationSectionBox);
        HBox buttonsBox = new HBox();
        buttonsBox.setAlignment(Pos.CENTER);
        buttonsBox.setSpacing(20);
        Button playButton = new Button("Play");
        playButton.getStyleClass().add("close-button");
        playButton.setOnAction(event -> {
            try {
                RadioButton selectedPieces = (RadioButton) piecesToggleGroup.getSelectedToggle();
                String selectedColor = selectedPieces.getText();
                RadioButton selectedDuration = (RadioButton) durationToggleGroup.getSelectedToggle();
                String duration = selectedDuration.getText();
                instance.setColor(selectedColor);
                instance.setDuration(duration);
                instance.setAgainstComp(true);
                launchGame();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        Button cancelButton = new Button("Go back");
        cancelButton.getStyleClass().add("close-button");
        cancelButton.setOnAction(event -> menuGrid.getChildren().remove(container));
        buttonsBox.getChildren().addAll(playButton, cancelButton);

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(setupBox);
        borderPane.setBottom(buttonsBox);

        container.getChildren().add(borderPane);
        BorderPane.setMargin(buttonsBox, new Insets(0, 0, 10, 0));
        menuGrid.add(container, 0, 0, 10, 10);
    }

    public void launchGame() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("play-view.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage currentStage = (Stage) menuGrid.getScene().getWindow();
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setX(currentStage.getX());
        stage.setY(currentStage.getY());
        stage.setWidth(currentStage.getWidth());
        stage.setHeight(currentStage.getHeight());
        if (currentStage.isMaximized()) {
            stage.setMaximized(true);
        }
        stage.show();
        currentStage.close();
    }
}