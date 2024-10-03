package com.yueshuya.knighttour;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Stack;

public class KnightTourApplication extends Application {
    private final KnightTourController controller = new KnightTourController(this);
    private final Stack<Location> stack = new Stack<>();
    private final HashMap<Location, ArrayList<Location>> exhaustedList = new HashMap<>();
    private final int[][] board = new int[KnightTourController.NUM_ROWS][KnightTourController.NUM_COLS];
    private Location currentLoc = null;
    private ArrayList<Location> neighbor = new ArrayList<>();
    private AnimationTimer animationTimer;  // Declare this outside start()
    private int attemptMove = 0;
    private boolean mode = true;
    // All possible knight moves
    private final int[][] moves = {
            {2, 1}, {2, -1}, {-2, 1}, {-2, -1},
            {1, 2}, {1, -2}, {-1, 2}, {-1, -2}
    };


    //Three helper method in dealing with the exhausted list - a map that track all the path that has been attempted
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

    //returns the sets of reachable positions of a given position
    private ArrayList<Location> findNeighbor(Location location) {
        if (location == null) {
            return null;
        }
        ArrayList<Location> neighbors = new ArrayList<>();
        for (int[] m : moves) {
            //Rules for how a piece move is treated a 2D array of possible change in tiles add/subtracted from the current position
            Location target = new Location(location.getRow() + m[0], location.getCol() + m[1]);
            if (isValidLoc(target) && notBeenThere(target)) {
                neighbors.add(target);
            }
        }
        removeWrongPath(neighbors, location);
        setNeighbor(neighbors);
        return neighbors;
    }
    //Hashmap look up to eliminate path chosen before
    private void removeWrongPath(ArrayList<Location> neighbors, Location currentLoc) {
        if (isInMap(currentLoc)){
           for (Location l : exhaustedList.get(currentLoc)){
               neighbors.remove(l);
           }
        }
    }
    //Valid = in bound of the indexes of the board
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
    //public method for controller to call - no obvious logic should be exposed
    public void move() {
        if (tourComplete()){
            return; // stops the attempts counter / freeze when the problem is solved
        }
        Location holder = getCurrentLoc();
        Location nextMove = newMove();
        if (nextMove == null) {
            backTrack();
        }else{
            setCurrentLoc(nextMove);
            addLoc(holder, getCurrentLoc());
        }
        attemptMove ++;
    }

    private Location newMove() {
        if (neighbor.isEmpty())
            return null;
        else if (mode)
            return chooseNextMove(neighbor);
        else
            return neighbor.getFirst();
    }

    public boolean tourComplete() {
        return stack.size() >= (KnightTourController.NUM_ROWS * KnightTourController.NUM_COLS);
    }

    private void backTrack() {
        if (!stack.isEmpty()) {
            Location lastLocation = stack.pop();
            board[lastLocation.getRow()][lastLocation.getCol()] = 0;
            if (!stack.isEmpty()) {
                currentLoc = stack.peek();
                // Find neighbors again after backtracking -
                // only remove from map if still no move available after backtracking one step -
                // we do this because you will not come back to the same location with same sequence of moves -
                // otherwise program will just back track all the way to move one and get stuck there
                findNeighbor(currentLoc);
                if (neighbor.isEmpty()){
                    removeLoc(currentLoc);
                    backTrack();
                }
            }
        }
    }
// Moves by default selects the first available positions of the Arraylist of neighbors
// but the mode by default is set to true - that ops for a Warnsdorff approach
    private Location chooseNextMove(ArrayList<Location> neighbor){
        Location nextMove = neighbor.getFirst();
        //Warnsdorff: select the move with lest accessibility
            int initA = evalAccessibility(nextMove);
            for(Location l : neighbor){ //basic min algo
                int test = evalAccessibility(l);
                if ( test < initA){
                    initA = test;
                    nextMove = l;
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
        findNeighbor(currentLoc); // update the list of neighbors for current position
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

    private void setNeighbor(ArrayList<Location> neighbor) {
        this.neighbor = neighbor;
    }

    public void setMode(boolean mode) {
        this.mode = mode;
    }

    public void reset() {
        currentLoc = null;
        // Reset the board to initial state by clearing values
        for (int[] ints : board) {
            Arrays.fill(ints, 0);
        }

        // Clear stack and exhausted paths
        stack.clear();
        exhaustedList.clear();
        attemptMove = 0;
        neighbor.clear();

        // Stop any active timers
        stopTimer();

        // Redraw the board
        controller.draw();
    }

    private void stopTimer() {
        if (animationTimer != null) {
            animationTimer.stop();
        }
    }


    @Override
    public void start(Stage stage) throws IOException {
        Scene rootScene = new Scene(controller.getAnchorPane(), 1024, 768);
        stage.setTitle("Knight's Tour");
        stage.setScene(rootScene);
        if (animationTimer == null){
            animationTimer = new AnimationTimer() {
                @Override
                public void handle(long l) {
                    controller.draw();
                }
            };
            animationTimer.start();
        }
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
