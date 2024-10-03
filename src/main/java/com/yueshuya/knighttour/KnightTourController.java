package com.yueshuya.knighttour;

/*
we are using this file as both eh controller and the view
 */

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

public class KnightTourController {
    public static final int NUM_ROWS = 10;
    public static final int NUM_COLS = 10;
    public static final int SIZE = 60;


    private final KnightTourApplication app;
    private final AnchorPane anchorPane;
    //GUI components
    private Button startButton;
    private Button stepButton;
    private Button resetButton;
    private Button mode;
    private Label rowLabel;
    private Label colLabel;
    private TextField rowTextField;
    private TextField colTextField;
    private Canvas canvas;
    private GraphicsContext gc;
    private AnimationTimer timer;

    //Application must establish communication between the application and the controller  - IE pass in itself
    public KnightTourController(KnightTourApplication app)
    {   this.app = app;
        anchorPane = new AnchorPane();
        createGUI();
        attchListeners();
        setupAnimationTimer();
    }

//a separate animation timer for the algorithm visualization
    private void setupAnimationTimer() {
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                app.move();
                draw();
            }
        };
    }
    public AnchorPane getAnchorPane() {
        return anchorPane;
    }

    private void createGUI() {
        createCanvas();
        createLabels();
        createTextFields();
        createButtons();
    }

    private void createCanvas() {
        canvas = new Canvas(800, 900);
        gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.TRANSPARENT);
        gc.fillRect(0, 0, 600, 500);
        AnchorPane.setLeftAnchor(canvas, 50.0);
        AnchorPane.setTopAnchor(canvas, 100.0);
        anchorPane.getChildren().add(canvas);
    }

    private void createLabels() {
        rowLabel = createLabel("row", 100.0, 200.0);
        colLabel = createLabel("column", 130.0, 200.0);
    }

    private Label createLabel(String text, double topAnchor, double rightAnchor) {
        Label label = new Label(text);
        AnchorPane.setTopAnchor(label, topAnchor);
        AnchorPane.setRightAnchor(label, rightAnchor);
        anchorPane.getChildren().add(label);
        return label;
    }

    private void createTextFields() {
        rowTextField = createTextField(97.0, 135.0);
        colTextField = createTextField(127.0, 135.0);
    }

    private TextField createTextField(double topAnchor, double rightAnchor) {
        TextField textField = new TextField();
        textField.setPrefWidth(50);
        AnchorPane.setTopAnchor(textField, topAnchor);
        AnchorPane.setRightAnchor(textField, rightAnchor);
        anchorPane.getChildren().add(textField);
        return textField;
    }

    private void createButtons() {
        startButton = createButton("Start", 170.0, 140.0, 100);
        stepButton = createButton("Step", 210.0, 140.0, 100);
        resetButton = createButton("Reset", 250.0, 140.0, 100);
        mode = createButton("Mode: Warnsdorff", 290.0, 90.0, 200);
    }

    private Button createButton(String text, double topAnchor, double rightAnchor, double prefWidth) {
        Button button = new Button(text);
        button.setPrefWidth(prefWidth);
        AnchorPane.setTopAnchor(button, topAnchor);
        AnchorPane.setRightAnchor(button, rightAnchor);
        anchorPane.getChildren().add(button);
        return button;
    }

    //helper  method - color in a tile
    private void drawSingleSquare(int x, int y, Paint color) {
        gc.setFill(Color.GOLDENROD);
        gc.fillRect(x, y, KnightTourController.SIZE, KnightTourController.SIZE);
        gc.setFill(color);
        gc.fillRect(x+ 2, y+ 2, KnightTourController.SIZE -(2 *2), KnightTourController.SIZE -(2 *2));
    }
//helper methods for text based GUI
    private void drawNumber(String s, int x, int y, double fontSize){
        gc.setFill(Color.BLACK);
        gc.setFont(new Font("Arial", fontSize));
        gc.fillText(s, x, y);
    }
    //helper methods that handles all the text on screen
    private void drawTexts() {
        gc.clearRect(80, NUM_ROWS*SIZE+10, 800, 200);
        drawNumber("Moves solved: " + app.getMove(), 80, NUM_ROWS*SIZE+50, 18); // Larger font size for text
        drawNumber("Total Number Attempted: " + app.getAttemptMove(), 80, NUM_ROWS*SIZE+80, 18); // Set font size to 18
    }


    //This draws on the canvas
    public void draw() {
        Color color;
        //draw the chess board
        for (int i = 0; i < NUM_ROWS; i++) {
            for (int j = 0; j < NUM_COLS; j++) {
                Location me = new Location(i,j);
                Location currLoc = app.getCurrentLoc();
                int value = app.getMoveNum(i,j);
                if (currLoc != null && currLoc.equals(me)) // draw current location
                    color = Color.CYAN;
                else if (app.getNeighbor().contains(me)) { //draw the neighbors
                    color = Color.GREEN;
                } else
                    color = Color.BURLYWOOD;
                drawSingleSquare(50+j*SIZE , 10+i*SIZE, color);
                if (value > 0){ //show the steps
                    drawNumber(String.valueOf(value), 75+j*SIZE , 45+i*SIZE, 16);
                }
            }
        }
        drawTexts();
    }



    private void attchListeners() {
        startButton.setOnAction(this::handleButtonClicks);
        stepButton.setOnAction(this::handleButtonClicks);
        resetButton.setOnAction(this::handleButtonClicks);
        mode.setOnAction(this::handleButtonClicks);
    }
//master input switch
    private void handleButtonClicks(ActionEvent actionEvent) {
        if (actionEvent.getSource() == startButton) {
            if (startButton.getText().equals("Start")) {
                handleStartButton();
            } else {
                handleStopButton();
            }
        }

        if (actionEvent.getSource() == stepButton) {
            app.move();
            draw();
        }
         if (actionEvent.getSource() == resetButton){
             handleResetButton();
         }
        if (actionEvent.getSource() == mode){
            handleModeButton();
        }
    }

    private void handleResetButton() {
        handleStopButton();
        app.reset();
        draw();
    }

    // toggle between brute force and Warnsdorff solution
    private void handleModeButton() {
        String text = mode.getText();
        if (text.equals("Mode: Warnsdorff")){
            mode.setText("Mode: Brute Force");
            app.setMode(false);
        }else {
            mode.setText("Mode: Warnsdorff");
            app.setMode(true);
        }
    }

    //toggle flash mode
    private void handleStartButton() {
        if (app.tourComplete()){
            handleResetButton();
        }
        String rowText = rowTextField.getText();
        String colText = colTextField.getText();

        if (isValidInput(rowText, colText)) {
            int row = Integer.parseInt(rowText);
            int col = Integer.parseInt(colText);

            if (isValidPosition(row, col)) {
                Location loc = new Location(row, col);
                app.setCurrentLoc(loc);
                startButton.setText("Stop");
                timer.start();  // Start the AnimationTimer
            } else {
                showErrorMessage("Row and column must be between 0 and 7.");
            }
        } else {
            showErrorMessage("Invalid input. Please enter valid integers.");
        }
    }

    // stop shit
    private void handleStopButton() {
        startButton.setText("Start");
        timer.stop();
    }

    // Modularity - maybe more input check in the future
    private boolean isValidInput(String rowText, String colText) {
        return isValidInteger(rowText) && isValidInteger(colText);
    }

    // prevent nasty out of bound shit
    private boolean isValidPosition(int row, int col) {
        return row >= 0 && row < NUM_ROWS && col >= 0 && col < NUM_COLS;
    }

    // master debugger
    private void showErrorMessage(String message) {
        System.out.println("Error: " + message);  // You can later replace this with UI-based error messaging
    }

    // did you put in a number
    private boolean isValidInteger(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}