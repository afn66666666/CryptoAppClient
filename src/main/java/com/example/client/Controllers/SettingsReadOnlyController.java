package com.example.client.Controllers;

import com.example.client.Definitions;
import com.example.client.Settings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsReadOnlyController implements Initializable {
    private Definitions.EncryptionMods mode;
    @FXML
    private ChoiceBox<String> encryptModeBox;
    @FXML
    private TextField publicKeyField;
    @FXML
    private TextField privateKeyField;
    @FXML
    private ChoiceBox<Integer> keyLengthBox;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        publicKeyField.setText(Settings.getEncryptedPKey());
        privateKeyField.setText(Settings.getPublicKey());
    }

    public Definitions.EncryptionMods getMode() {
        return mode;
    }

    public void setMode(Definitions.EncryptionMods mode) {
        this.mode = mode;
        encryptModeBox.setValue(Definitions.encryptionModeName(mode));
    }
}
