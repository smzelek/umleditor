package com.canisdev;

import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;


public class UMLArea extends Pane {
    //TODO: when making lines, send its parent/child to front of UMLArea
    //TODO: hitting escape will exit out of new box and new line mode
    //TODO: where should the relationships EXIST? in the UMLArea or only as children of nodes

    private double lastMousePosX;
    private double lastMousePosY;

    private Rectangle selectionArea;
    private double firstCornerX;
    private double firstCornerY;
    private double secondCornerX;
    private double secondCornerY;

    enum Mode {
        None, Box, Line, Select
    }

    private Mode currentMode = Mode.None;

    private int lineType;

    private ArrayList<UMLClassBox> boxes;
    private ArrayList<Relationship> relationships;

    private UMLClassBox lineParent1;
    private UMLClassBox lineParent2;

    /**
     *
     */
    public UMLArea () {
        super();

        boxes = new ArrayList<>();
        relationships = new ArrayList<>();
        setId("umlArea");

        setOnKeyReleased(this::processKeys);
        setOnMouseMoved(this::setMouseoverCursor);
        setOnMousePressed(this::handleMouseDown);
        setOnMouseReleased(this::handleMouseUp);
        setOnMouseDragged(this::handleMouseDrag);
    }

    /**
     *
     * @param mouseEvent
     */
    private void handleMouseDown (MouseEvent mouseEvent)
    {
        switch (currentMode) {
            case None: {
                lastMousePosX = mouseEvent.getSceneX();
                lastMousePosY = mouseEvent.getSceneY();

                getScene().setCursor(Cursor.MOVE);
                break;
            }
            case Box: {
                break;
            }
            case Line: {
                break;
            }
            case Select: {
                lastMousePosX = mouseEvent.getSceneX();
                lastMousePosY = mouseEvent.getSceneY();
                firstCornerX = lastMousePosX;
                firstCornerY = lastMousePosY;

                getScene().setCursor(Cursor.CLOSED_HAND);
                break;
            }
        }

        clearSelections();
        requestFocus();
        mouseEvent.consume();
    }


    /**
     *
     * @param mouseEvent
     */
    private void handleMouseDrag (MouseEvent mouseEvent)
    {
        switch (currentMode)
        {
            case None:
            {
                double offsetX = mouseEvent.getSceneX() - lastMousePosX;
                double offsetY = mouseEvent.getSceneY() - lastMousePosY;

                //translate all boxes, lines will move w/ children
                for (UMLClassBox box : boxes){
                    //todo: let nodes do move in class method, so lines are moved too
                    box.translate(offsetX, offsetY);
                }
                break;
            }
            case Box:
            {
                return;
            }
            case Line:
            {
                return;
            }
            case Select:
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
                break;
            }
        }

        lastMousePosX = mouseEvent.getSceneX();
        lastMousePosY = mouseEvent.getSceneY();

        mouseEvent.consume();
    }

    /**
     *
     * @param mouseEvent
     */
    private void handleMouseUp (MouseEvent mouseEvent)
    {
        switch (currentMode)
        {
            case None:
            {
                break;
            }
            case Box:
            {
                newBox(mouseEvent.getX(), mouseEvent.getY());
                setNoMode();
                break;
            }
            case Line:
            {
                for (UMLClassBox n : boxes){
                    Point2D localMouseXY = n.sceneToLocal(mouseEvent.getSceneX(), mouseEvent.getSceneY());
                    if (n.contains(localMouseXY)){
                        if (lineParent1 == null){
                            lineParent1 = n;
                            break;
                        }else if (!n.equals(lineParent1)){
                            lineParent2 = n;
                            newLine();

                            setNoMode();
                            break;
                        }
                    }
                }
                break;
            }
            case Select:
            {
                getChildren().remove(selectionArea);
                selectionArea = null;
                setNoMode();
                break;
            }
        }

        getScene().setCursor(Cursor.DEFAULT);
        mouseEvent.consume();
    }


    /**
     *
     * @param mouseEvent
     */
    private void setMouseoverCursor (MouseEvent mouseEvent)
    {
        switch (currentMode)
        {
            case None:
            {
                getScene().setCursor(Cursor.DEFAULT);
                break;
            }
            case Box:
            {
                getScene().setCursor(Cursor.CROSSHAIR);
                break;
            }
            case Line:
            {
                getScene().setCursor(Cursor.CROSSHAIR);
                break;
            }
            case Select:
            {
                getScene().setCursor(Cursor.DEFAULT);
                break;
            }
        }
        mouseEvent.consume();
    }

    /**
     *
     * @param keyEvent
     */
    private void processKeys (KeyEvent keyEvent) {
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
    }

    /**
     *
     * @param box
     */
    private void removeLineIfDependent(UMLClassBox box){

        ArrayList<Relationship> relationshipsCopy = new ArrayList<>(relationships);

        for (Relationship r : relationshipsCopy){
            if (r.dependsOn(box)){
                relationships.remove(r);
                getChildren().remove(r);
            }
        }
    }

    /**
     *
     */
    public void setNewBoxMode(){
        currentMode = Mode.Box;

        for (Node n :getChildren()){
            n.setMouseTransparent(true);
        }

        clearSelections();
    }

    /**
     *
     */
    public void setNoMode(){
        currentMode = Mode.None;
        for (Node n :getChildren()){
            n.setMouseTransparent(false);
        }
    }

    /**
     *
     */
    public void setNewLineMode(int lineType){
        currentMode = Mode.Line;
        this.lineType = lineType;

        lineParent1 = null;
        lineParent2 = null;

        for (Node n :getChildren()){
            n.setMouseTransparent(true);
        }

        clearSelections();
    }

    /**
     * Allows graphical button to change state to Selection mode
     */
    public void setSelectionMode() {
        currentMode = Mode.Select;
        clearSelections();

        for (Node n :getChildren()){
            n.setMouseTransparent(true);
        }
    }

    /**
     *
     */
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

    /**
     *
     * @param xpos
     * @param ypos
     */
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

    /**
     *
     */
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
