package com.yueshuya.knighttour;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class KnightTourApplication extends Application {
    private Location currentLoc = null;
    public KnightTourController controller = new KnightTourController(this);
    private final int[][] board = new int[KnightTourController.NUM_ROWS][KnightTourController.NUM_COLS];
    private Stack<Location> stack = new Stack<>();
    private HashMap<Location, ArrayList<Location>> exhaustedList = new HashMap<>();
    private int move = 0;

    // M7 - Adding, Deleting, and Checking if a Location is in the Exhausted List
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

    // M8 - Finding Valid Neighbors
    public ArrayList<Location> findNeighbor(Location location) {
        ArrayList<Location> neighbors = new ArrayList<>();
        if (location == null)
            return neighbors;

        // All possible knight moves
        int[][] moves = {
                {2, 1}, {2, -1}, {-2, 1}, {-2, -1},
                {1, 2}, {1, -2}, {-1, 2}, {-1, -2}
        };

        for (int[] m : moves) {
            Location target = new Location(location.getRow() + m[0], location.getCol() + m[1]);
            if (isValidLoc(target) && notBeenThere(target)) {
                neighbors.add(target);
            }
        }
        removeWrongPath(neighbors, location);
        controller.setNeighbor(neighbors);
        return neighbors;
    }

    private void removeWrongPath(ArrayList<Location> neighbors, Location currentLoc) {
        if (isInMap(currentLoc)){
           for (Location l : exhaustedList.get(currentLoc)){
               neighbors.remove(l);
           }
        }
    }


    // M9
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

    // Milestone 10 -  Choosing a move, and moving forward
    public void move() {
        Location holder = getCurrentLoc();
        Location nextMove = chooseNextMove();

        if (nextMove == null && !tourComplete()) {
            backTrack();
        }else{
            setCurrentLoc(nextMove);
            addLoc(holder, getCurrentLoc());
        }

    }

    // Fix the condition for checking if the tour is complete
    private boolean tourComplete() {
        return move >= KnightTourController.NUM_ROWS * KnightTourController.NUM_COLS;
    }

    private void backTrack() {
        if (!stack.isEmpty()) {
            Location lastLocation = stack.pop();
            board[lastLocation.getRow()][lastLocation.getCol()] = 0;  // Mark as unvisited
            move--;
            if (!stack.isEmpty()) {
                currentLoc = stack.peek();
                findNeighbor(currentLoc);  // Find neighbors again after backtracking
            }
        }
    }

    private Location chooseNextMove() {
        if (controller.getNeighbor().isEmpty()) {
            return null;
        }
        return controller.getNeighbor().get(0);  // Fixed to use get(0) instead of getFirst()
    }

    // M4 - Set location and update the board
    public void setCurrentLoc(Location loc) {
        currentLoc = loc;
        board[currentLoc.getRow()][currentLoc.getCol()] = ++move;
        stack.push(loc);
        findNeighbor(currentLoc);
    }

    public Location getCurrentLoc() {
        return currentLoc;
    }

    // Helper to render text on screen
    public int getMoveNum(Location location) {
        return board[location.getRow()][location.getCol()];
    }

    // Handle the GUI feedback and changes
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

    public void printBoard() {
        for (int[] ints : board) {
            for (int j = 0; j < board[0].length; j++) {
                System.out.print(ints[j] + "  ");
            }
            System.out.println();
        }
        System.out.println(controller.getNeighbor());

    }
}
