package com.yueshuya.knighttour;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class KnightTourApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        KnightTourController controller = new KnightTourController(this);
        Scene rootScene = new Scene(controller.getAnchorPane(), 1024, 768);
        stage.setTitle("Knight's Tour");
        stage.setScene(rootScene);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}