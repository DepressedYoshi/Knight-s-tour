package com.yueshuya.knighttour;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class KnightTourApplication extends Application {
    private Location currentLoc = null;
    private AnimationTimer animationTimer;
    public void setCurrentLoc(Location loc) {
        currentLoc = loc;
    }
    public Location getCurrentLoc() {
        return currentLoc;

    }

    @Override
    public void start(Stage stage) throws IOException {
        KnightTourController controller = new KnightTourController(this);
        Scene rootScene = new Scene(controller.getAnchorPane(), 1024, 768);
        stage.setTitle("Knight's Tour");
        stage.setScene(rootScene);
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                controller.draw();
            }
        };
        animationTimer.start();
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}