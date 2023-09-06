package com.example.client.Controllers;

import com.example.client.Application;
import com.example.client.Client;
import com.example.client.Message;
import com.example.client.Settings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {
    @FXML
    private TextArea logArea;
    @FXML
    private TextField inputField;

    @FXML
    private ImageView sendFileBtn;
    @FXML
    private Button uploadBtn;

    private static TextArea stLogArea;
    private static TextField stInput;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        stLogArea = logArea;
        stInput = inputField;
//        sendFileBtn.setDisable(true);
//        uploadBtn.setDisable(true);
    }

    public static boolean log(String message) {
        if (stLogArea == null) {
            return false;
        }
        stLogArea.appendText(message);
        stLogArea.appendText("\n");
        return true;
    }


    @FXML
    public void sendTextClicked(MouseEvent event) {
        var data = stInput.getText();
        if (!data.isEmpty()) {
            try {
                var msg = new Message("", "text", "ready",data.getBytes().length, data.getBytes());
                Client.writeMessage(msg);
                stInput.clear();
            } catch (Exception e) {
            }
        }
    }

    @FXML
    public void settingsButtonClicked(MouseEvent event) {
        try {
            var fxmlLoader = new FXMLLoader(Application.class.getResource("SettingsReadOnly.fxml"));
            var scene = new Scene(fxmlLoader.load(), 500, 500);
//            var controller = fxmlLoader.<SettingsReadOnlyController>getController();
//            controller.setMode(Settings.getEncryptionMode());
            var stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Session setting");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {

        }
    }

    @FXML
    public void sendFileBtnClicked(MouseEvent event) {
        var fc = new FileChooser();
        var file = fc.showOpenDialog(null);
        try {
            var fxmlLoader = new FXMLLoader(Application.class.getResource("ProgressBar.fxml"));
            var scene = new Scene(fxmlLoader.load(), 425, 289);
            var stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Loading");
            stage.setScene(scene);
            stage.show();
            var bytes = Files.readAllBytes(file.toPath());
            Client.sendFile(file.getName(),bytes);

        } catch (FileNotFoundException e) {
            MainViewController.log("file choosing error : " + e.getMessage());
        } catch (IOException e) {
            MainViewController.log("file reading error : " + e.getMessage());
        }
    }

    @FXML
    public void uploadBtnClicked(MouseEvent event){
        var fc = new FileChooser();
        fc.setInitialDirectory(new File("C:/Users/china/IdeaProjects/Server/sessions"));
        var file = fc.showOpenDialog(null);
        if(file != null) {
            var chooser = new DirectoryChooser();
            var resDir = new File("C:/Users/china/IdeaProjects/Client/loads/" + Client.clientSocket.getLocalPort());
            resDir.mkdir();
            chooser.setInitialDirectory(resDir);
            var selectedDir = chooser.showDialog(null);
            Client.loadDir = selectedDir;
            Client.uploadFile(file);
        }
    }

}