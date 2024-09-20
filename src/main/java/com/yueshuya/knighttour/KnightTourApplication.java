package com.yueshuya.knighttour;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Stack;

public class KnightTourApplication extends Application {
    public static final int SIZE = 5;
    private Location currentLoc = null;
    private AnimationTimer animationTimer;
    public KnightTourController controller = new KnightTourController(this);
    private int[][] board = new int[controller.NUM_ROWS][controller.NUM_COLS];;
    private Stack stack = new Stack<>();
    private HashMap<Location, Location> exhaustedList = new HashMap<>();


    //M5 - Adding, Deleting and Checking if a Location is in the Exhausted List
    private void addLoc(Location start, Location end){
        exhaustedList.put(start, end);
    }
    private void removeLoc(Location location){
        exhaustedList.remove(location);
    }

    private boolean isInMap(Location loc){
        return exhaustedList.containsKey(loc);
    }




//M4 - set location color
    public void setCurrentLoc(Location loc) {
        currentLoc = loc;
    }
    public Location getCurrentLoc() {
        return currentLoc;

    }
//Handle the gui feedback and changes
    @Override
    public void start(Stage stage) throws IOException {

        //gui
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