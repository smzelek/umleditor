package com.canisdev;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

import java.util.ArrayList;

/**
 * Created by steve on 2/8/2017.
 */
public class UMLArea extends Pane {

    //TODO: hitting escape will exit out of new box and new line mode

    private double lastMousePosX;
    private double lastMousePosY;
    private boolean newBoxMode = false;
    private boolean newLineMode = false;

    private ArrayList<UMLClassBox> boxes;
    private ArrayList<Line> lines;

    private UMLClassBox lineParent1;
    private UMLClassBox lineParent2;

    public UMLArea () {
        super();

        boxes = new ArrayList<>();
        lines = new ArrayList<>();

        setOnKeyReleased((keyEvent) -> {
            //delete any nodes that are selected
            if (keyEvent.getCode() == KeyCode.DELETE || keyEvent.getCode() == KeyCode.BACK_SPACE){

                ArrayList<UMLClassBox> box_children = new ArrayList<>(boxes);

                for (UMLClassBox n : box_children){
                    if (n.isSelected) {
                        getChildren().remove(n);
                        boxes.remove(n);
                        requestFocus();
                        getScene().setCursor(Cursor.DEFAULT);
                    }
                    keyEvent.consume();
                }
            }
        });

        setOnMouseMoved((mouseEvent) -> {
            if (newBoxMode || newLineMode){
                getScene().setCursor(Cursor.CROSSHAIR);
            }else{
                getScene().setCursor(Cursor.DEFAULT);
            }
            mouseEvent.consume();
        });

        setOnMousePressed((mouseEvent) -> {
            if (!newBoxMode && !newLineMode){
                lastMousePosX = mouseEvent.getSceneX();
                lastMousePosY = mouseEvent.getSceneY();

                clearSelections();
                getScene().setCursor(Cursor.MOVE);
            }

            requestFocus();
            mouseEvent.consume();
        });

        setOnMouseReleased((mouseEvent) -> {
            if (newBoxMode){
                System.out.println("dsdsds");
                newBox(mouseEvent.getX(), mouseEvent.getY());
                setNewBoxMode(false);
            }
            //TODO: future bugs possible if lines are in get children!
            else if (newLineMode){
                for (UMLClassBox n : boxes){
                    Point2D localMouseXY = n.sceneToLocal(mouseEvent.getSceneX(), mouseEvent.getSceneY());
                    if (n.contains(localMouseXY)){
                        if (lineParent1 == null){
                            lineParent1 = n;
                            System.out.println(n);
                            break;
                        }else if (!n.equals(lineParent1)){
                            lineParent2 = n;
                            System.out.println(n);
                            newLine();

                            setNewLineMode(false);
                            break;
                        }
                    }
                }
            }
            getScene().setCursor(Cursor.DEFAULT);
            mouseEvent.consume();
        });

        setOnMouseDragged((mouseEvent) -> {
            if (newBoxMode || newLineMode){
                return;
            }
            double offsetX = mouseEvent.getSceneX() - lastMousePosX;
            double offsetY = mouseEvent.getSceneY() - lastMousePosY;

            //translate all boxes, lines will move w/ children
            for (Node n : boxes){
                n.setTranslateX(n.getTranslateX() + offsetX);
                n.setTranslateY(n.getTranslateY() + offsetY);
            }

            lastMousePosX = mouseEvent.getSceneX();
            lastMousePosY = mouseEvent.getSceneY();
            mouseEvent.consume();
        });
    }

    public void setNewBoxMode(boolean state){
        if (state){
            newLineMode = false;
        }
        for (Node n :getChildren()){
            n.setMouseTransparent(state);
        }
        newBoxMode = state;
    }

    public void setNewLineMode(boolean state){
        if (state){
            newBoxMode = false;
            lineParent1 = null;
            lineParent2 = null;
        }
        for (Node n :getChildren()){
            n.setMouseTransparent(state);
        }
        newLineMode = state;
    }

    private void newLine(){
        Line line = new Line();
        DoubleProperty startX = new SimpleDoubleProperty();
        DoubleProperty startY = new SimpleDoubleProperty();
        line.startXProperty().bind(startX);
        line.startYProperty().bind(startY);
        lineParent1.setDependentLine(line, startX, startY);

        DoubleProperty endX = new SimpleDoubleProperty();
        DoubleProperty endY = new SimpleDoubleProperty();
        line.endXProperty().bind(endX);
        line.endYProperty().bind(endY);
        lineParent2.setDependentLine(line, endX, endY);

        //todo: lineParent1/2 add dependent (line)
        //todo: on delete, also delete dependents
        //todo: on move/resize a box, find closest "anchor point" for a line on its parent
        //todo: lines are not always on top

        getChildren().addAll(line);
        lines.add(line);
    }

    private void newBox(double xpos, double ypos){
        UMLClassBox myBox = new UMLClassBox(150, 200);
        getChildren().addAll(myBox);
        boxes.add(myBox);
        myBox.setTranslateX(xpos-75);
        myBox.setTranslateY(ypos-100);

        clearSelections();
        myBox.setSelected(true);
        requestFocus();
    }

    public void clearSelections(){
        for (UMLClassBox n : boxes){
            n.setSelected(false);
        }
    }

}
