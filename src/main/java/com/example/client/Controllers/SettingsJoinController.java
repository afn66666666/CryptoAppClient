package com.example.client.Controllers;

import com.example.client.Application;
import com.example.client.Client;
import com.example.client.Definitions;
import com.example.client.Encryption.Mars;
import com.example.client.Settings;
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

public class SettingsJoinController implements Initializable {
    @FXML
    private Button generateBtn;
    @FXML
    private ChoiceBox<String> encryptModeBox;
    @FXML
    private TextField sessionKeyField;
    @FXML
    private Button acceptBtn;
    @FXML
    private ChoiceBox<String> keyLengthBox;

    private int session;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String keyLengths[] = {String.valueOf(Definitions.KEY_LENGTH_1),
                String.valueOf(Definitions.KEY_LENGTH_2),
                String.valueOf(Definitions.KEY_LENGTH_3)};
        keyLengthBox.setItems(FXCollections.observableArrayList(keyLengths));
        keyLengthBox.setValue(String.valueOf(Definitions.KEY_LENGTH_1));

    }


    public void setInfo(int sessionId,String mode){
        this.session = sessionId;
        encryptModeBox.setValue(mode);
    }

    @FXML
    public void acceptBtnClicked(MouseEvent event) {
        try {
            if (sessionKeyField.getText().isEmpty()) {
                var warning = new Alert(Alert.AlertType.ERROR, "fill key fields");
                warning.showAndWait();
            } else {
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
                Settings.setSessionKey(sessionKeyField.getText());
                Client.exchangeEncryptionData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void generateBtnClicked(MouseEvent event) {

        var bytes = Mars.generatePublicKey(Integer.valueOf(keyLengthBox.getValue())/8);
        sessionKeyField.setText(new String(bytes));
    }
}
