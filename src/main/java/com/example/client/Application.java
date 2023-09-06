package com.example.client;

import com.example.client.Encryption.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Random;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        var xtr1 = new XTR(Entities.TestMode.Ferma, 45);
        var xtr2 = new XTR(Entities.TestMode.Ferma, 45);
        var pKeyArr = xtr1.getPublicKey();
        var pKey1 = new XTR.PublicKey(pKeyArr[0], pKeyArr[1],
                new GFP2(pKeyArr[0], pKeyArr[2], pKeyArr[3]), new GFP2(pKeyArr[0], pKeyArr[4], pKeyArr[5]));
        String test = "amoqewfk";


        var input = xtr2.encrypt(test.getBytes(),pKey1,pKeyArr[6]);

        var result = new String(xtr1.decrypt(input));
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("Authorization.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 500);
        stage.setTitle("Authorization");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }


}