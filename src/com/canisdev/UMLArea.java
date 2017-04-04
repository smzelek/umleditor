package com.canisdev;

import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

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

    private Rectangle selectionArea;
    private double firstCornerX;
    private double firstCornerY;
    private double secondCornerX;
    private double secondCornerY;

    private boolean newBoxMode = false;
    private boolean newLineMode = false;
    private boolean selectionMode = false;

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


            if (selectionMode) {
                //set first corner of selectionArea
                firstCornerX = lastMousePosX;
                firstCornerY = lastMousePosY;

                getScene().setCursor(Cursor.CLOSED_HAND);
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
            else if (selectionMode) {

                getChildren().remove(selectionArea);
                selectionArea = null;
                setSelectionMode(false);

            }

            getScene().setCursor(Cursor.DEFAULT);
            mouseEvent.consume();
        });

        setOnMouseDragged((mouseEvent) -> {
            if (newBoxMode || newLineMode){
                return;
            }

            if (!selectionMode) {
                double offsetX = mouseEvent.getSceneX() - lastMousePosX;
                double offsetY = mouseEvent.getSceneY() - lastMousePosY;

                //translate all boxes, lines will move w/ children
                for (UMLClassBox box : boxes){
                    //todo: let nodes do move in class method, so lines are moved too
                    box.translate(offsetX, offsetY);
                }
            }
            else
            {
                //get second corner from drag event
                secondCornerX = mouseEvent.getSceneX();
                secondCornerY = mouseEvent.getSceneY();

                //position of rectangle is (x, y) of top left corner
                //will always be min values of corner coordinates
                double xPos = Math.min(secondCornerX, firstCornerX);
                double yPos = Math.min(secondCornerY, firstCornerY);
                double height = Math.abs(secondCornerY - firstCornerY);
                double width = Math.abs(secondCornerX - firstCornerX);

                if (selectionArea == null) {

                    initSelectionArea(xPos, yPos, width, height);
                    getChildren().add(selectionArea);

                } else {

                    setSelectionArea(xPos, yPos, width, height);
                }

                double maxX = xPos + width;
                double maxY = yPos + height;

                selectBoxesInArea(xPos, maxX, yPos, maxY);

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
            selectionMode = false;
        }
        for (Node n :getChildren()){
            n.setMouseTransparent(state);
        }
        newBoxMode = state;
    }

    public void setNewLineMode(boolean state){
        if (state){
            newBoxMode = false;
            selectionMode = false;
            lineParent1 = null;
            lineParent2 = null;
        }
        for (Node n :getChildren()){
            n.setMouseTransparent(state);
        }
        newLineMode = state;
    }

    /**
     * Allows graphical button to change state to Selection mode
     * @param state Boolean value to toggle state
     */
    public void setSelectionMode(boolean state) {
        if (state) {
            newBoxMode = false;
            newLineMode = false;
        }

        for (Node n :getChildren()){
            n.setMouseTransparent(state);
        }
        selectionMode = state;
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

        //todo: what is the logical depth ordering?

        getChildren().addAll(rel);
        relationships.add(rel);
        rel.toBack();
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

    /**
     * Initializes new selectionArea Rectangle with position, width, and height
     * Initialized selectionArea will have a black, dashed border
     * @param xPos X coordinate of top left corner
     * @param yPos Y coordinate of top left corner
     * @param width Rectangle width
     * @param height Rectangle height
     */
    private void initSelectionArea(double xPos, double yPos, double width, double height) {
        selectionArea = new Rectangle(xPos, yPos, width, height);
        selectionArea.setFill(Color.TRANSPARENT);
        selectionArea.setStroke(Color.BLACK);
        selectionArea.getStrokeDashArray().setAll(10.0, 10.0);
    }

    /**
     * Modifies selectionArea's position in parent, width, and height
     * @param xPos New X coordinate of top left corner
     * @param yPos New Y coordinate of top left corner
     * @param width New width
     * @param height New height
     */
    private void setSelectionArea(double xPos, double yPos, double width, double height) {
        selectionArea.setX(xPos);
        selectionArea.setY(yPos);
        selectionArea.setWidth(width);
        selectionArea.setHeight(height);
    }

    /**
     * Selects instances of UMLClassBox class, if any, in bounds given as arguments
     * Preconditions:
     *   * minX <= maxX
     *   * minY <= maxY
     * Postcondition: any UMLClassBox instances occupying space in coordinates
     * ([minX, maxX], [minY, maxY]) within parent are now selected
     * @param minX Minimum parent x coordinate
     * @param maxX Maximum parent x coordinate
     * @param minY Minimum parent y coordinate
     * @param maxY Maximum parent y coordinate
     */
    private void selectBoxesInArea(double minX, double maxX, double minY, double maxY) {

        for (UMLClassBox n : boxes) {

            double leftBound = n.getLeftAnchorPoint().getX();
            double rightBound = n.getRightAnchorPoint().getX();
            double topBound = n.getTopAnchorPoint().getY();
            double bottomBound = n.getBottomAnchorPoint().getY();

            if (leftBound <= maxX && rightBound >= minX && topBound <= maxY && bottomBound >= minY) {
                n.setSelected(true);
            } else {
                n.setSelected(false);
            }

        }
    }

}
