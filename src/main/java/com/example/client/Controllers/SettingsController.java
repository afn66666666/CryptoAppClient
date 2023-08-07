package com.example.client.Controllers;

import com.example.client.Application;
import com.example.client.Client;
import com.example.client.Encryption.Encryptor;
import com.example.client.Encryption.Entities;
import com.example.client.Encryption.Mars;
import com.example.client.Encryption.XTR;
import com.example.client.Message;
import com.example.client.Settings;
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
import java.security.MessageDigest;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {
    @FXML
    private Button privateGenerateBtn;
    @FXML
    private Button publicGenerateBtn;
    @FXML
    private ChoiceBox<String> encryptModeBox;
    @FXML
    private TextField publicKeyField;
    @FXML
    private TextField privateKeyField;
    @FXML
    private Button acceptBtn;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String st[] = {"MARS(EBC)","MARS(CBC)", "XTR"};
        encryptModeBox.setItems(FXCollections.observableArrayList(st));
        privateKeyField.setDisable(true);
        privateGenerateBtn.setDisable(true);
        encryptModeBox.setValue("MARS(EBC)");
        encryptModeBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number newValue) {
                var val = st[newValue.intValue()];
                if(val.equals("MARS(EBC)") || val.equals("MARS(CBC)")){
                    privateKeyField.setDisable(true);
                    privateGenerateBtn.setDisable(true);

                }
                if(val.equals("XTR")){
                    privateKeyField.setDisable(false);
                    privateGenerateBtn.setDisable(false);
                }

            }
        });
    }

    @FXML
    public void acceptBtnClicked(MouseEvent event) {
        try {
            if(publicKeyField.getText().isEmpty() || (encryptModeBox.getValue().equals("XTR") && (privateKeyField.getText().isEmpty() ||
                    publicKeyField.getText().isEmpty()))){
                var warning = new Alert(Alert.AlertType.ERROR,"fill key fields");
                warning.showAndWait();
            }
            else {
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
                var encryptionMode = encryptModeBox.getValue();
                var s = new Settings(encryptionMode, publicKeyField.getText(), privateKeyField.getText());
                Client.startSession(s);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    public void privateGenerateBtnClicked(MouseEvent event){
        var xtr = new XTR(Entities.TestMode.MillerRabin,50);
        var s = xtr.getPublicKey();
        privateKeyField.setText(s[0].toString() + ";" + s[1].toString());
    }
    @FXML
    public void publicGenerateBtnClicked(MouseEvent event){
        var result = Mars.generatePublicKey(16);
        var s = new String(result);
        publicKeyField.setText(s);
    }
}

