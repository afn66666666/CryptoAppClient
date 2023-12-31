package com.example.client.Controllers;

import com.example.client.*;
import com.example.client.Encryption.Entities;
import com.example.client.Encryption.Mars;
import com.example.client.Encryption.XTR;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {
    @FXML
    private ChoiceBox<String> encryptModeBox;
    @FXML
    private Button acceptBtn;
    @FXML
    private ChoiceBox<String> keyLengthBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String encryptionModes[] = {"MARS(EBC)", "MARS(CBC)"};
        String keyLengths[] = {String.valueOf(Definitions.KEY_LENGTH_1),
                String.valueOf(Definitions.KEY_LENGTH_2),
                String.valueOf(Definitions.KEY_LENGTH_3)};
        encryptModeBox.setItems(FXCollections.observableArrayList(encryptionModes));
        keyLengthBox.setItems(FXCollections.observableArrayList(keyLengths));
        keyLengthBox.setValue(String.valueOf(Definitions.KEY_LENGTH_1));
        encryptModeBox.setValue(Definitions.encryptionModeName(Definitions.EncryptionMods.MARS_EBC));

    }

    @FXML
    public void acceptBtnClicked(MouseEvent event) {
        try {

                var prevStage = (Stage) acceptBtn.getScene().getWindow();
                prevStage.close();
                var fxmlLoader = new FXMLLoader(Application.class.getResource("mainView.fxml"));
                var scene = new Scene(fxmlLoader.load(), 600, 500);
                var stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setTitle("Client");
                stage.setScene(scene);
                stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    public void handle(WindowEvent we) {
                        Client.closeResources();
                    }
                });
                stage.show();
                var mode = encryptModeBox.getValue();
                Settings.setEncryptionMode(Definitions.encryptionMode(mode));
                Settings.setKeyLength(Integer.parseInt(keyLengthBox.getValue()));
//                Settings.setSessionKey(sessionKeyField.getText());
                Client.startSession();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

