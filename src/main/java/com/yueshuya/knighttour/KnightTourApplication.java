package com.yueshuya.knighttour;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
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


    //M7 - Adding, Deleting and Checking if a Location is in the Exhausted List
    private void addLoc(Location start, Location end){
        exhaustedList.put(start, end);
    }
    private void removeLoc(Location location){
        exhaustedList.remove(location);
    }

    private boolean isInMap(Location loc){
        return exhaustedList.containsKey(loc);
    }

//M8 - Finding Valid Neighbors
    public ArrayList<Location> findNeighbor(Location location){
        ArrayList<Location> neighbors = new ArrayList<>();
        if (location ==null)
            return neighbors;
        // All possible knight moves
        int[][] moves = {
                { 2, 1 }, { 2, -1 }, { -2, 1 }, { -2, -1 },
                { 1, 2 }, { 1, -2 }, { -1, 2 }, { -1, -2 }
        };
        //chekc all knight positon
        for (int[] m :moves){
            Location target = new Location(location.getRow()+m[0], location.getCol()+m[1]);
            if (isValidLoc(target, this.board)){
                neighbors.add(target);
            }
        }
        controller.setNeighbor(neighbors);
        System.out.println(neighbors);
        return neighbors;
    }
    //M9

    private boolean isValidLoc(Location location, int[][] board){
        int x = location.getCol();
        int y = location.getRow();
        boolean notTried = !isInMap(location);
        boolean inBound = x >=0 && x < controller.NUM_COLS && y >=0 && y < controller.NUM_ROWS;
//        boolean notBeenThere = board[y][x] == 0;
        return  inBound && notTried;
    }

//M9 - draw



//M4 - set location color
    public void setCurrentLoc(Location loc) {
        currentLoc = loc;
        findNeighbor(currentLoc);
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