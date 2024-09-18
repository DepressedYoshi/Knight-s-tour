package com.yueshuya.knighttour;

/*
we are using this file as botht eh contoller and the view
 */

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.Scanner;

public class KnightTourController {
    public static final int NUM_ROWS = 8;
    public static final int NUM_COLS = 8;
    public static final int SIZE = 60;


    private KnightTourApplication app;
    private AnchorPane anchorPane;
    //GUI compoenents
    private Button startButton;
    private Button stepButton;
    private Label rowLabel;
    private Label colLabel;
    private TextField rowTextField;
    private TextField colTextField;
    private Canvas canvas;
    private GraphicsContext gc;

    public KnightTourController(KnightTourApplication app)
    {
        this.app = app;
        anchorPane = new AnchorPane();

        createGUI();
        attchListeners();
    }

    public AnchorPane getAnchorPane() {
        return anchorPane;
    }

    private void createGUI() {
        //board
        canvas = new Canvas(600,500); //a 600x500 canvas area
        gc = canvas.getGraphicsContext2D(); //a GraphicsContext to draw on the canvas
        gc.setFill(Color.TRANSPARENT); //choose a red color
        gc.fillRect(0,0,600,500); //fill the entire area, 600x500, with a
        AnchorPane.setLeftAnchor(canvas, 50.0); //anchor the canvas 50 pixels to the
        AnchorPane.setTopAnchor(canvas, 100.0); //anchor the canvas 100 pixels down from
        anchorPane.getChildren().add(canvas); //add the canvas to the AnchorPane
        //rowLabel
        rowLabel = new Label("row");
        AnchorPane.setTopAnchor(rowLabel, 100.0);
        AnchorPane.setRightAnchor(rowLabel, 200.0);
        anchorPane.getChildren().add(rowLabel);
        //colLabel
        colLabel = new Label("column");
        AnchorPane.setTopAnchor(colLabel, 130.0);
        AnchorPane.setRightAnchor(colLabel, 200.0);
        anchorPane.getChildren().add(colLabel);
        //rowText
        rowTextField = new TextField();
        rowTextField.setPrefWidth(50);
        AnchorPane.setTopAnchor(rowTextField, 97.0);
        AnchorPane.setRightAnchor(rowTextField, 135.0);
        anchorPane.getChildren().add(rowTextField);
        //col text
        colTextField = new TextField();
        colTextField.setPrefWidth(50);
        AnchorPane.setTopAnchor(colTextField, 127.0);
        AnchorPane.setRightAnchor(colTextField, 135.0);
        anchorPane.getChildren().add(colTextField);

        //start button
        startButton = new Button("Start");
        startButton.setPrefWidth(100);
        AnchorPane.setTopAnchor(startButton, 170.0);
        AnchorPane.setRightAnchor(startButton, 140.0);
        anchorPane.getChildren().add(startButton);

        //Step button
        stepButton = new Button("Step");
        stepButton.setPrefWidth(100);
        AnchorPane.setTopAnchor(stepButton, 210.0);
        AnchorPane.setRightAnchor(stepButton, 140.0);
        anchorPane.getChildren().add(stepButton);
    }

    private void drawSingleSquare(int x, int y, int size, int stroke, Paint color) {
        gc.setFill(Color.GOLDENROD);
        gc.fillRect(x, y, size, size);
        gc.setFill(color);
        gc.fillRect(x+stroke, y+stroke, size-(stroke*2), size-(stroke*2));
    }

    public void draw() {
        Color color;
        for (int i = 0; i < NUM_ROWS; i++) {
            for (int j = 0; j < NUM_COLS; j++) {
                Location me = new Location(i,j);
                Location target = app.getCurrentLoc();
                if (target != null && target.equals(me))
                    color = Color.CYAN;
                else
                    color = Color.BURLYWOOD;

                drawSingleSquare(50+j*SIZE , 10+i*SIZE, SIZE, 2, color);
            }
        }
    }

    private void attchListeners() {
        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                handleButtonClicks(actionEvent);
            }
        });
        stepButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                handleButtonClicks(actionEvent);
            }
        });
    }
    private void handleButtonClicks(ActionEvent actionEvent) {
        if(actionEvent.getSource() == startButton) {
            String buttonText = startButton.getText();
            if(buttonText.equals("Start")) {
                if(app.getCurrentLoc() == null) {
                    int row = Integer.parseInt(rowTextField.getText());
                    int col = Integer.parseInt(colTextField.getText());
                    Location loc = new Location(row, col);
                    app.setCurrentLoc(loc);
                }
                startButton.setText("Stop");
            }
            else {
                startButton.setText("Start");
            }
        }
    }
}