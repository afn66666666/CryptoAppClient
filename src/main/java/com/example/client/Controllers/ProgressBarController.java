package com.example.client.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class ProgressBarController implements Initializable {
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Label progressLabel;
    @FXML
    private Button cancelButton;
    private static ProgressBar stProgressBar;
    private static Label stProgressLabel;
    private static Button stCancelButton;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        stProgressBar = progressBar;
        stProgressBar.setStyle("-fx-accent: #00FF00;");
        stProgressLabel = progressLabel;
        progressLabel.setText("0%");
    }

    public static void updateProgress(int percents){
        stProgressLabel.setText(percents + "%");
    }
    @FXML
    public void cancelButtonClicked(MouseEvent event){

    }

    public static ProgressBar getStProgressBar(){
        return stProgressBar;
    }
}
