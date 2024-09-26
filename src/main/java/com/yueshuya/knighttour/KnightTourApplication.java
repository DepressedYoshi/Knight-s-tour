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
    private Location currentLoc = null;
    public KnightTourController controller = new KnightTourController(this);
    private int[][] board = new int[KnightTourController.NUM_ROWS][KnightTourController.NUM_COLS];
    private Stack<Location> stack = new Stack<>();
    private HashMap<Location, ArrayList<Location>> exhaustedList = new HashMap<>();
    private int attemptMove = 0;
    private ArrayList<Location> neighbor = new ArrayList<>();
    private boolean mode = true;
    // All possible knight moves
    private final int[][] moves = {
            {2, 1}, {2, -1}, {-2, 1}, {-2, -1},
            {1, 2}, {1, -2}, {-1, 2}, {-1, -2}
    };

    private void addLoc(Location start, Location end) {
        if (isInMap(start)){
            exhaustedList.get(start).add(end);
        }else {
            ArrayList<Location> list = new ArrayList<>();
            list.add(end);
            exhaustedList.put(start, list);
        }
    }
    private void removeLoc(Location location) {
        exhaustedList.remove(location);
    }

    private boolean isInMap(Location loc) {
        return exhaustedList.containsKey(loc);
    }
    public ArrayList<Location> findNeighbor(Location location) {
        if (location == null) {
            return null;
        }
        ArrayList<Location> neighbors = new ArrayList<>();
        for (int[] m : moves) {
            Location target = new Location(location.getRow() + m[0], location.getCol() + m[1]);
            if (isValidLoc(target) && notBeenThere(target)) {
                neighbors.add(target);
            }
        }
        removeWrongPath(neighbors, location);
        setNeighbor(neighbors);
        return neighbors;
    }
    private void removeWrongPath(ArrayList<Location> neighbors, Location currentLoc) {
        if (isInMap(currentLoc)){
           for (Location l : exhaustedList.get(currentLoc)){
               neighbors.remove(l);
           }
        }
    }
    private boolean isValidLoc(Location location) {
        int x = location.getCol();
        int y = location.getRow();
        boolean notTried = !isInMap(location);
        boolean inBound = x >= 0 && x < KnightTourController.NUM_COLS && y >= 0 && y < KnightTourController.NUM_ROWS;
        return inBound && notTried;
    }
    private boolean notBeenThere(Location target) {
        return board[target.getRow()][target.getCol()] == 0;
    }
    public void move() {
        if (tourComplete()){
            return;
        }
        Location holder = getCurrentLoc();
        Location nextMove = chooseNextMove(neighbor);

        if (nextMove == null) {
            backTrack();
        }else{
            setCurrentLoc(nextMove);
            addLoc(holder, getCurrentLoc());
        }
        attemptMove ++;

    }
    private boolean tourComplete() {
        return stack.size() >= (KnightTourController.NUM_ROWS * KnightTourController.NUM_COLS);
    }
    private void backTrack() {
        if (!stack.isEmpty()) {
            Location lastLocation = stack.pop();
            board[lastLocation.getRow()][lastLocation.getCol()] = 0;
            if (!stack.isEmpty()) {
                currentLoc = stack.peek();
                // Find neighbors again after backtracking - only remove from map if still no move available after backtracking one step
                findNeighbor(currentLoc);
                if (neighbor.isEmpty()){
                    removeLoc(currentLoc);
                    backTrack();
                }
            }
        }
    }

    private Location chooseNextMove(ArrayList<Location> neighbor){
        if (neighbor.isEmpty()) {
            return null;
        }
        Location nextMove = neighbor.get(0);
        if (mode){
            int initA = evalAccessibility(nextMove);
            for(Location l : neighbor){
                int test = evalAccessibility(l);
                if ( test < initA){
                    initA = test;
                    nextMove = l;
                };
            }
        }
        return nextMove;
    }

    private int evalAccessibility(Location l) {
        return findNeighbor(l).size();
    }


    public void setCurrentLoc(Location loc) {
        currentLoc = loc;
        stack.push(loc);
        board[currentLoc.getRow()][currentLoc.getCol()] = stack.size();
        findNeighbor(currentLoc);
    }
    public Location getCurrentLoc() {
        return currentLoc;
    }

    public int getMoveNum(int i, int j) {
        return board[i][j];
    }
    public int getAttemptMove() {
        return attemptMove;
    }

    public int getMove() {
        return stack.size();
    }

    public ArrayList<Location> getNeighbor() {
        return neighbor;
    }

    public void setNeighbor(ArrayList<Location> neighbor) {
        this.neighbor = neighbor;
    }

    public void setMode(boolean mode) {
        this.mode = mode;
    }

    public void reset(){
        currentLoc = null;
        controller = new KnightTourController(this);
        board = new int[KnightTourController.NUM_ROWS][KnightTourController.NUM_COLS];
        stack = new Stack<>();
        exhaustedList = new HashMap<>();
        attemptMove = 0;
        neighbor = new ArrayList<>();
        controller = new KnightTourController(this);
    }

    @Override
    public void start(Stage stage) throws IOException {
        Scene rootScene = new Scene(controller.getAnchorPane(), 1024, 768);
        stage.setTitle("Knight's Tour");
        stage.setScene(rootScene);
        AnimationTimer animationTimer = new AnimationTimer() {
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
