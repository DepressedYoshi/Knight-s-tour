package com.yueshuya.knighttour;

/*
we are using this file as botht eh contoller and the view
 */

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class KnightTourController {
    private KnightTourApplication app;
    private AnchorPane anchorPane;

    //GUI compoenents
    private Button startButton;
    private Button stepButton;
    private Label rowLabel;
    private Label colLabel;
    private TextField rowTextField;
    private TextField colTextField;
    private GraphicsContext gc;

    public KnightTourController(KnightTourApplication app)
    {
        this.app = app;
        anchorPane = new AnchorPane();

        createGUI();
        attchListeners();
    }

    private void createGUI() {
        startButton = new Button("Start");
        AnchorPane.setTopAnchor(startButton, 50.0);
        AnchorPane.setRightAnchor(startButton, 100.0);
        anchorPane.getChildren().add(startButton);
    }

    private void attchListeners() {

    }

    public AnchorPane getAnchorPane() {
        return anchorPane;
    }
}