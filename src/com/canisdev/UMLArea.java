package com.canisdev;

import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;

import java.util.ArrayList;

/**
 * Created by steve on 2/8/2017.
 */
public class UMLArea extends Pane {

    //TODO: when making lines, send its parent/child to front of UMLArea
    //TODO: hitting escape will exit out of new box and new line mode
    //TODO: where should the relationships EXIST? in the UMLArea or only as children of nodes
    //will both parent and child have a copy? fine, but design issues if delete
    //if not, design issues for when a box moves translating the line

    private double lastMousePosX;
    private double lastMousePosY;
    private boolean newBoxMode = false;
    private boolean newLineMode = false;
    private int lineType;

    private ArrayList<UMLClassBox> boxes;
    private ArrayList<Relationship> relationships;

    private UMLClassBox lineParent1;
    private UMLClassBox lineParent2;

    public UMLArea () {
        super();

        boxes = new ArrayList<>();
        relationships = new ArrayList<>();

        setOnKeyReleased((keyEvent) -> {
            //delete any nodes that are selected
            if (keyEvent.getCode() == KeyCode.DELETE || keyEvent.getCode() == KeyCode.BACK_SPACE){
                ArrayList<UMLClassBox> boxCopy = new ArrayList<>(boxes);

                for (UMLClassBox n : boxCopy){
                    if (n.isSelected) {
                        removeLineIfDependent(n);
                        boxes.remove(n);
                        getChildren().remove(n);

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
                newBox(mouseEvent.getX(), mouseEvent.getY());
                setNewBoxMode(false);
            }
            else if (newLineMode){
                for (UMLClassBox n : boxes){
                    Point2D localMouseXY = n.sceneToLocal(mouseEvent.getSceneX(), mouseEvent.getSceneY());
                    if (n.contains(localMouseXY)){
                        if (lineParent1 == null){
                            lineParent1 = n;
                            break;
                        }else if (!n.equals(lineParent1)){
                            lineParent2 = n;
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
            for (UMLClassBox box : boxes){
                //todo: let nodes do move in class method, so lines are moved too
                box.translate(offsetX, offsetY);
            }

            lastMousePosX = mouseEvent.getSceneX();
            lastMousePosY = mouseEvent.getSceneY();
            mouseEvent.consume();
        });
    }

    private void removeLineIfDependent(UMLClassBox box){

        ArrayList<Relationship> relationshipsCopy = new ArrayList<>(relationships);

        for (Relationship r : relationshipsCopy){
            if (r.dependsOn(box)){
                relationships.remove(r);
                getChildren().remove(r);
            }
        }

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

    public void setLineType(int type){
        lineType = type;
    }

    private void newLine(){
        Relationship rel = new Relationship(lineParent1, lineParent2);
        //base on switch value
        switch (lineType){
            case 0:
                rel.setArrowShape();
                break;
            case 1:
                rel.setEmptyArrowShape();
                break;
            case 2:
                rel.setEmptyArrowShape();
                rel.setDashedLine(true);
                break;
            case 3:
                rel.setArrowShape();
                rel.setDashedLine(true);
                break;
            case 4:
                rel.setEmptyDiamondShape();
                break;
            case 5:
                rel.setFullDiamondShape();
                break;
        }
        //todo: lineParent1/2 add dependent (line)
        //todo: on delete, also delete dependents
        //todo: on move/resize a box, find closest "anchor point" for a line on its parent
        //todo: what is the logical depth ordering?

        getChildren().addAll(rel);
        relationships.add(rel);
    }

    private void newBox(double xpos, double ypos){
        int boxWidth = 120;
        int boxHeight = 160;
        UMLClassBox myBox = new UMLClassBox(boxWidth, boxHeight);
        getChildren().addAll(myBox);
        boxes.add(myBox);
        myBox.setTranslateX(xpos - boxWidth/2);
        myBox.setTranslateY(ypos - boxHeight/2);

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
