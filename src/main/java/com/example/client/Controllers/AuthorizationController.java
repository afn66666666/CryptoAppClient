package com.example.client.Controllers;

import com.example.client.Application;
import com.example.client.Client;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class AuthorizationController implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    private TextField sessionIdField;
    @FXML
    private Button enterSessionBtn;
    @FXML
    private Button newSessionBtn;

    public void newSessionBtnClicked(MouseEvent event){
        try {
            var prevStage = (Stage)newSessionBtn.getScene().getWindow();
            prevStage.close();
            var fxmlLoader = new FXMLLoader(Application.class.getResource("Settings.fxml"));
            var scene = new Scene(fxmlLoader.load(), 600, 500);
            var stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Client settings");
            stage.setScene(scene);
            stage.show();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void joinBtnClicked(MouseEvent event){
        try {
            var prevStage = (Stage)newSessionBtn.getScene().getWindow();
            prevStage.close();
            var fxmlLoader = new FXMLLoader(Application.class.getResource("mainView.fxml"));
            var scene = new Scene(fxmlLoader.load(), 600, 500);
            var stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Client");
            stage.setScene(scene);
            stage.setOnCloseRequest(new EventHandler<WindowEvent>(){
                public void handle(WindowEvent we){
                    Client.closeResources();
                }
            });
            stage.show();
            var sessionId = sessionIdField.getText();
            var intVal = Integer.valueOf(sessionId);
            Client.joinToSession(intVal);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

}
