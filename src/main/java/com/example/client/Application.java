package com.example.client;

import com.example.client.Encryption.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Random;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("Authorization.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 500);
        stage.setTitle("Authorization");
        stage.setScene(scene);
        stage.show();
        var m = new MarsCBC();
        m.setKey(new String("lolika").getBytes());
        var t1 = m.encrypt(new String("johnny12johnny12johnny12johnny12johnny12johnny12john").getBytes());
        var t2 = m.decrypt(t1);
        var res = new String(t2);
    }

    public static void main(String[] args) {
        launch();
    }


}