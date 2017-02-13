package com.canisdev;

import javafx.beans.property.DoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;

/**
 * Created by steve on 2/7/2017.
 */
public class UMLClassBox extends StackPane {

    private VBox contents;
    private TextField nameArea;
    private TextArea attributeArea;
    private TextArea methodArea;
    private ResizeNode right, left, topRight, bottomRight, topLeft, bottomLeft, top, bottom;

    private double lastMousePosX;
    private double lastMousePosY;
    private final double SIDE_MARGIN = 2;
    private final double RESIZE_MARGIN = 5;
    public boolean isSelected;

    //TODO: fix states of de/focusing, mouse events
    //fix resizing

    public UMLClassBox(double width, double height){
        super();

        width += (SIDE_MARGIN+RESIZE_MARGIN)*2;
        height += (SIDE_MARGIN+RESIZE_MARGIN)*2;

        nameArea = new TextField();
        nameArea.setPromptText("Name");
        nameArea.setAlignment(Pos.TOP_CENTER);
        nameArea.setPrefHeight(25);
        nameArea.setPrefWidth(Double.MAX_VALUE);
        nameArea.setMaxWidth(Double.MAX_VALUE);

        attributeArea = new TextArea();
        attributeArea.setPromptText("Attributes");
        attributeArea.setPrefHeight(80);
        attributeArea.setPrefWidth(Double.MAX_VALUE);
        attributeArea.setMaxWidth(Double.MAX_VALUE);

        methodArea = new TextArea();
        methodArea.setPromptText("Methods");
        methodArea.setPrefHeight(80);
        methodArea.setPrefWidth(Double.MAX_VALUE);
        methodArea.setMaxWidth(Double.MAX_VALUE);

        contents = new VBox();
        contents.setId("uml-class-box-contents");
        contents.setPadding(new Insets(SIDE_MARGIN));
        contents.setVgrow(nameArea, Priority.NEVER);
        contents.setVgrow(methodArea, Priority.ALWAYS);
        contents.setVgrow(attributeArea, Priority.ALWAYS);
        contents.getChildren().addAll(nameArea,attributeArea,methodArea);
        contents.setPrefWidth(Double.MAX_VALUE);
        contents.setMaxWidth(Double.MAX_VALUE);
        contents.setPrefHeight(Double.MAX_VALUE);
        contents.setMaxHeight(Double.MAX_VALUE);

        ResizeNode.setNodeRadius(RESIZE_MARGIN);

        topLeft = new ResizeNode(ResizeNode.TOP_LEFT);
        setAlignment(topLeft, Pos.TOP_LEFT);
        top = new ResizeNode(ResizeNode.TOP_CENTER);
        setAlignment(top, Pos.TOP_CENTER);
        topRight = new ResizeNode(ResizeNode.TOP_RIGHT);
        setAlignment(topRight, Pos.TOP_RIGHT);
        right = new ResizeNode(ResizeNode.CENTER_RIGHT);
        setAlignment(right, Pos.CENTER_RIGHT);
        bottomRight = new ResizeNode(ResizeNode.BOTTOM_RIGHT);
        setAlignment(bottomRight, Pos.BOTTOM_RIGHT);
        bottom = new ResizeNode(ResizeNode.BOTTOM_CENTER);
        setAlignment(bottom, Pos.BOTTOM_CENTER);
        bottomLeft = new ResizeNode(ResizeNode.BOTTOM_LEFT);
        setAlignment(bottomLeft, Pos.BOTTOM_LEFT);
        left = new ResizeNode(ResizeNode.CENTER_LEFT);
        setAlignment(left, Pos.CENTER_LEFT);

        getChildren().addAll(contents);
        getChildren().addAll(topLeft, top, topRight, right, bottomRight, bottom, bottomLeft, left);
        setAlignment(contents, Pos.CENTER);
        setMargin(contents, new Insets(RESIZE_MARGIN));
        setId("uml-class-box-frame");
        setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
        setMaxSize(width, height);
        setMinSize(width, height);

        setSelected(true);

        //*********************************************************************
        //Mouse handlers for contents box

        nameArea.setOnKeyPressed((keyEvent) -> {
            if (keyEvent.getCode() == KeyCode.TAB){
                if (keyEvent.isShiftDown()){
                    methodArea.requestFocus();
                }else{
                    attributeArea.requestFocus();
                }
                keyEvent.consume();
            }
        });
        nameArea.setOnMouseClicked((mouseEvent)-> {
            toFront();
            ((UMLArea) getParent()).clearSelections();
            mouseEvent.consume();
        });

        attributeArea.setOnKeyPressed((keyEvent) -> {
            if (keyEvent.getCode() == KeyCode.TAB){
                if (keyEvent.isShiftDown()){
                    nameArea.requestFocus();
                }else{
                    methodArea.requestFocus();
                }
                keyEvent.consume();
            }
        });
        attributeArea.setOnMouseClicked((mouseEvent)-> {
            toFront();
            ((UMLArea) getParent()).clearSelections();
            attributeArea.home();
            mouseEvent.consume();
        });


        methodArea.setOnKeyPressed((keyEvent) -> {
            if (keyEvent.getCode() == KeyCode.TAB){
                if (keyEvent.isShiftDown()){
                    attributeArea.requestFocus();
                }else{
                    nameArea.requestFocus();
                }
                keyEvent.consume();
            }
        });

        methodArea.setOnMouseClicked((mouseEvent)-> {
            toFront();
            ((UMLArea) getParent()).clearSelections();
            methodArea.home();
            mouseEvent.consume();
        });

        contents.setOnMouseExited((mouseEvent) -> {
            getScene().setCursor(Cursor.DEFAULT);
            mouseEvent.consume();
        });

        setOnMouseExited((mouseEvent) -> {
            getScene().setCursor(Cursor.DEFAULT);
            mouseEvent.consume();
        });

        contents.setOnMouseMoved((mouseEvent) -> {
            getScene().setCursor(Cursor.MOVE);
            mouseEvent.consume();
        });

        setOnMouseMoved((mouseEvent) -> {
            getScene().setCursor(Cursor.MOVE);
            mouseEvent.consume();
        });

        contents.setOnMousePressed((mouseEvent) -> {
            lastMousePosX = mouseEvent.getSceneX();
            lastMousePosY = mouseEvent.getSceneY();
            ((UMLArea) getParent()).clearSelections();
            setSelected(true);
            toFront();

            mouseEvent.consume();

        });

        setOnMousePressed((mouseEvent) -> {
            lastMousePosX = mouseEvent.getSceneX();
            lastMousePosY = mouseEvent.getSceneY();
            ((UMLArea) getParent()).clearSelections();
            setSelected(true);
            toFront();

            mouseEvent.consume();
        });

        contents.setOnMouseReleased((mouseEvent) -> {
            getScene().setCursor(Cursor.DEFAULT);
            mouseEvent.consume();
        });

        setOnMouseReleased((mouseEvent) -> {
            getScene().setCursor(Cursor.DEFAULT);
            mouseEvent.consume();
        });

        setOnMouseDragged((mouseEvent) -> {
            double offsetX = mouseEvent.getSceneX() - lastMousePosX;
            double offsetY = mouseEvent.getSceneY() - lastMousePosY;

            getScene().setCursor(Cursor.MOVE);
            setTranslateX(getTranslateX() + offsetX);
            setTranslateY(getTranslateY() + offsetY);

            lastMousePosX = mouseEvent.getSceneX();
            lastMousePosY = mouseEvent.getSceneY();
            mouseEvent.consume();
        });

        contents.setOnMouseDragged((mouseEvent) -> {
            double offsetX = mouseEvent.getSceneX() - lastMousePosX;
            double offsetY = mouseEvent.getSceneY() - lastMousePosY;

            getScene().setCursor(Cursor.MOVE);
            setTranslateX(getTranslateX() + offsetX);
            setTranslateY(getTranslateY() + offsetY);

            lastMousePosX = mouseEvent.getSceneX();
            lastMousePosY = mouseEvent.getSceneY();
            mouseEvent.consume();
        });
    }

    public void setDependentLine(Line line, DoubleProperty lineX, DoubleProperty lineY){
        lineX.bind(translateXProperty());
        lineY.bind(translateYProperty());
    }

    public void setSelected (boolean state) {
        isSelected = state;
        if (state) {
            requestFocus();
            topLeft.setVisible(true);
            topRight.setVisible(true);
            bottomLeft.setVisible(true);
            bottomRight.setVisible(true);
            left.setVisible(true);
            right.setVisible(true);
            top.setVisible(true);
            bottom.setVisible(true);
            //set circle resize nodes visible
        } else {
            topLeft.setVisible(false);
            topRight.setVisible(false);
            bottomLeft.setVisible(false);
            bottomRight.setVisible(false);
            left.setVisible(false);
            right.setVisible(false);
            top.setVisible(false);
            bottom.setVisible(false);
            //set circle resize nodes invisible
        }
    }
}