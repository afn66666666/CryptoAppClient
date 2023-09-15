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
        var m = new MarsCBC();
        var s = new String("hbnvjfjfmncmcmzxmcmmvmnw");
        m.setKey(s.getBytes());
        var bytes = "heo heo heo heoh heoh heo".getBytes();
        var e = m.encrypt(bytes);
        var d = m.decrypt(e);
        System.out.println(new String(d));

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