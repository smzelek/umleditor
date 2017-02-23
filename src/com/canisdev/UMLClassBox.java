//TODO: examine states of de/focusing, mouse events, shift click multi-select
//if add max resize limit of box, will need to add condition in resize node to prevent "lag"

package com.canisdev;

import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.Arrays;

public class UMLClassBox extends StackPane {

    private VBox contents;
    private TextField nameArea;
    private TextArea attributeArea;
    private TextArea methodArea;
    public ResizeNode right, left, topRight, bottomRight, topLeft, bottomLeft, top, bottom;

    private double lastMousePosX;
    private double lastMousePosY;
    private final double SIDE_MARGIN = 2;
    private final double RESIZE_MARGIN = 5;
    public boolean isSelected;
    private ArrayList<Relationship> dependents;

    public UMLClassBox(double width, double height){
        super();

        width = Math.floor(width);
        height = Math.floor(height);

        dependents = new ArrayList<>();

        width += (SIDE_MARGIN+RESIZE_MARGIN)*2 + .1;
        height += (SIDE_MARGIN+RESIZE_MARGIN)*2 + .1;

        //Add text areas of a UML Class Box: name, attributes, methods.
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

        //Add 8 resize nodes to decorate frame of UML Class Box.

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
        setPrefSize(width, height);
        setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
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

        attributeArea.setOnMouseClicked((mouseEvent) -> {
            toFront();
            ((UMLArea) getParent()).clearSelections();
            if (attributeArea.getText().length() == 0){
                attributeArea.insertText(0, " ");
                attributeArea.deleteText(0,1);
            }
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
            if (methodArea.getText().length() == 0){
                methodArea.insertText(0, " ");
                methodArea.deleteText(0,1);
            }
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
            translate(offsetX, offsetY);

            lastMousePosX = mouseEvent.getSceneX();
            lastMousePosY = mouseEvent.getSceneY();
            mouseEvent.consume();
        });

        contents.setOnMouseDragged((mouseEvent) -> {
            double offsetX = mouseEvent.getSceneX() - lastMousePosX;
            double offsetY = mouseEvent.getSceneY() - lastMousePosY;

            getScene().setCursor(Cursor.MOVE);
            translate(offsetX, offsetY);

            lastMousePosX = mouseEvent.getSceneX();
            lastMousePosY = mouseEvent.getSceneY();
            mouseEvent.consume();
        });
    }

    //Fetches an array of possible linking points for a relationship.
    public ArrayList<Point2D> getAnchorPoints() {
        return new ArrayList<>(Arrays.asList(getTopAnchorPoint(), getRightAnchorPoint(), getBottomAnchorPoint(), getLeftAnchorPoint()));
    }

    public ArrayList<Point2D> getOppositeAnchorPoints() {
        return new ArrayList<>(Arrays.asList(getBottomAnchorPoint(), getLeftAnchorPoint(), getTopAnchorPoint(), getRightAnchorPoint()));
    }

    //Anchor point getters:
    //******************

    public Point2D getLeftAnchorPoint () {
        double xpos = getTranslateX() + RESIZE_MARGIN;
        double ypos = getTranslateY() + getHeight()/2;
        return new Point2D(xpos, ypos);
    }

    public Point2D getRightAnchorPoint () {
        double xpos = getTranslateX() + getWidth() - RESIZE_MARGIN + SIDE_MARGIN/2;
        double ypos = getTranslateY() + getHeight()/2;
        return new Point2D(xpos, ypos);
    }

    public Point2D getBottomAnchorPoint () {
        double xpos = getTranslateX() + getWidth()/2;
        double ypos = getTranslateY() + getHeight() - RESIZE_MARGIN + SIDE_MARGIN/2;
        return new Point2D(xpos, ypos);
    }

    public Point2D getTopAnchorPoint () {
        double xpos = getTranslateX() + getWidth()/2;
        double ypos = getTranslateY() + RESIZE_MARGIN;
        return new Point2D(xpos, ypos);
    }


    //Convenience method to shift scene object.
    public void translate(double offsetX, double offsetY){
        setTranslateX(getTranslateX() + offsetX);
        setTranslateY(getTranslateY() + offsetY);

        sendMoveEvent();
    }

    //Lets dependent relationships know that they must adjust
    //their endpoints, size, and rotation.
    public void sendMoveEvent(){
        for (Relationship r : dependents){
            r.fireEvent(new AnchorEvent(this));
        }
    }

    //Keep track of attached relationship objects.
    public void addDependentRelationship(Relationship r){
        dependents.add(r);
    }

    //Change appearance to indicate selection, allows resizing and deleting.
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
        } else {
            topLeft.setVisible(false);
            topRight.setVisible(false);
            bottomLeft.setVisible(false);
            bottomRight.setVisible(false);
            left.setVisible(false);
            right.setVisible(false);
            top.setVisible(false);
            bottom.setVisible(false);
        }
    }
}