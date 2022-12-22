package com.example.fileencoder;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class main extends Application {
    static public Stage  mainStage;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(main.class.getResource("mainView.fxml"));
        mainStage = stage ;
        Scene scene = new Scene(fxmlLoader.load(), 1000, 700);
        stage.setTitle("Manchester Encoder");
        stage.setScene(scene);
        stage.show();
    }
    public static Stage getStage(){
        return mainStage ;
    }

    public static void main(String[] args) {
        launch();
    }
}