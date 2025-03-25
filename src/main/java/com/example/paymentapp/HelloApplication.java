package com.example.paymentapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.File;
import java.net.URL;
import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("paymentapp.fxml"));
            Group root = new Group();
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("PRZETWARZANIE PŁATNOSCI");
            Image icon = new Image("logo2.jpg");
            stage.getIcons().add(icon);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch(Exception e){
            e.printStackTrace();
        }

        //File fxmlFile = new File("C:/Users/lenar/IdeaProjects/PaymentApp/src/main/resources/com/example/paymentapp/paymentapp.fxml");
        //FXMLLoader fxmlLoader = new FXMLLoader(fxmlFile.toURI().toURL());

    }


    public static void main(String[] args) {
        launch();
    }
}