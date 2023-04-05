package com.example.chess;

import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

import java.io.IOException;
import java.util.Optional;

public class MainMenuController {
    private HostServices hostServices;

    @FXML
    public void switchScreen(ActionEvent event) throws IOException {
        String btnId = ((Button) event.getSource()).getId();
        if(btnId.equalsIgnoreCase("quit")){
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirm Exit");
            alert.setHeaderText("Are you sure you want to quit the game?");
            alert.setContentText("Press OK to exit, or Cancel to go back.");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                Platform.exit();
            }
        } else if (btnId.equalsIgnoreCase("rules")) {
            hostServices.showDocument("https://en.wikipedia.org/wiki/Rules_of_chess");
        }else{
            FXMLLoader loader = new FXMLLoader(getClass().getResource(btnId + "-view.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage currentStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();
            currentStage.close();
        }
    }

    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }

}
