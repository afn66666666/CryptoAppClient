module com.example.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;


    opens com.example.client to javafx.fxml, com.google.gson;
    exports com.example.client;
    exports com.example.client.Controllers;
    opens com.example.client.Controllers to javafx.fxml;
    exports com.example.client.Encryption;
    opens com.example.client.Encryption to javafx.fxml;

}