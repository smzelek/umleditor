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

/**
 * Class that creates the field within which UMLClassBoxes, and their relationships, can be added and manipulated.
 * Holds onto created boxes and relationships in ArrayLists, to keep track of all the current nodes.
 * When a Class Box is deleted, its relationships are deleted with it.
 *
 * UMLAreas have several different modes depending on what the user wants to do.
 * Box Mode: Allows the user to create new Class Boxes.
 * None    : Allows the user to translate the viewport for the UMLArea.
 * Line    : Allows the user to create new relationships
 * Select  : Allows the user to select any Class Box within its area.
 *
 * Interactions are handled through various mouse and keyboard events.
 */
public class UMLArea extends Pane {
    //TODO: when making lines, send its parent/child to front of UMLArea
    //TODO: hitting escape will exit out of new box and new line mode
    //TODO: where should the relationships EXIST? in the UMLArea or only as children of nodes
    enum Mode { None, Box, Line, Select }
    private Mode currentMode = Mode.None;

    private double lastMousePosX;
    private double lastMousePosY;

    private Rectangle selectionArea;
    private double firstCornerX;
    private double firstCornerY;
    private double secondCornerX;
    private double secondCornerY;

    private Relationship.RelationshipType lineType;

    private ArrayList<UMLClassBox> boxes;
    private ArrayList<Relationship> relationships;

    private UMLClassBox lineParent1;
    private UMLClassBox lineParent2;

    /**
     * Creates a UMLArea.
     */
    public UMLArea () {
        super();

        boxes = new ArrayList<>();
        relationships = new ArrayList<>();
        setId("umlArea");

        setOnKeyReleased(this::handleKeyReleased);
        setOnMouseMoved(this::handleMouseMoved);
        setOnMousePressed(this::handleMousePressed);
        setOnMouseReleased(this::handleMouseReleased);
        setOnMouseDragged(this::handleMouseDragged);
    }

    /**
     * Handles mouse press events based on the current mode of
     * the UMLArea.
     * <p>
     *    <strong>None:</strong> Performs the setup to allow translating
     *    the viewport during mouse drag events.
     * </p>
     * <p>
     *    <strong>Select:</strong> Performs the setup to allow calculation
     *    of a selection area.
     * </p>
     *
     * @param mouseEvent A mouse press event fired by the user.
     */
    private void handleMousePressed (MouseEvent mouseEvent)
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
     * Handles mouse drag events based on the current mode of
     * the UMLArea.
     * <p>
     *    <strong>None:</strong> Translates the viewport of the UMLArea.
     * </p>
     * <p>
     *    <strong>Select:</strong> Updates the selection area.
     * </p>
     *
     * @param mouseEvent A mouse drag event fired by the user.
     */
    private void handleMouseDragged (MouseEvent mouseEvent)
    {
        switch (currentMode)
        {
            case None:
            {
                double offsetX = mouseEvent.getSceneX() - lastMousePosX;
                double offsetY = mouseEvent.getSceneY() - lastMousePosY;

                for (UMLClassBox box : boxes){
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

                selectBoxesInArea();
                break;
            }
        }

        lastMousePosX = mouseEvent.getSceneX();
        lastMousePosY = mouseEvent.getSceneY();

        mouseEvent.consume();
    }

    /**
     * Handles mouse release events based on the current mode of
     * the UMLArea.
     * <p>
     *    <strong>Box:</strong> Adds a new UMLClassBox at the mouse
     *    location and resets the UMLArea's mode.
     * </p>
     * <p>
     *    <strong>Line:</strong> Sets the source for a Relationship on
     *    first click. Sets the destination for a Relationship on second
     *    click, and constructs the Relationship.
     * </p>
     * <p>
     *    <strong>Select:</strong> Removes the selection area, but leaves
     *    the UMLClassBoxes selected.
     * </p>
     *
     * @param mouseEvent A mouse release event fired by the user.
     */
    private void handleMouseReleased (MouseEvent mouseEvent)
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
     * Handles mouse move events based on the current mode of
     * the UMLArea. Properly sets the mouse Cursor for each mode.
     *
     * @param mouseEvent A mouse move event fired by the user.
     */
    private void handleMouseMoved (MouseEvent mouseEvent)
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
     * Handles key press events while the UMLArea is focused
     * for DELETE and BACK_SPACE by deleting any UMLClassBoxes
     * that are currently selected. Also deletes Relationships
     * that depend on these UMLClassBoxes.
     *
     * @param keyEvent A key press event fired by the user.
     */
    private void handleKeyReleased (KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.DELETE || keyEvent.getCode() == KeyCode.BACK_SPACE){
            ArrayList<UMLClassBox> boxCopy = new ArrayList<>(boxes);

            for (UMLClassBox n : boxCopy){
                if (n.isSelected) {
                    removeDependentRelationships(n);
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
     * Deletes any Relationships that depend on a given
     * UMLClassBox.
     *
     * @param box The UMLClassBox whose dependent Relationships
     *            should be removed.
     */
    private void removeDependentRelationships (UMLClassBox box){

        ArrayList<Relationship> relationshipsCopy = new ArrayList<>(relationships);

        for (Relationship r : relationshipsCopy){
            if (r.dependsOn(box)){
                relationships.remove(r);
                getChildren().remove(r);
            }
        }
    }

    /**
     * Changes the UMLArea's mode to Box, allowing the creation
     * of new UMLClassBoxes.
     */
    public void setNewBoxMode(){
        currentMode = Mode.Box;

        for (Node n :getChildren()){
            n.setMouseTransparent(true);
        }

        clearSelections();
    }

    /**
     * Changes the UMLArea's mode to None, allowing the translation
     * of the viewport for the UMLArea.
     */
    public void setNoMode(){
        currentMode = Mode.None;
        for (Node n :getChildren()){
            n.setMouseTransparent(false);
        }
    }

    /**
     * Changes the UMLArea's mode to Line, allowing the creation of
     * new Relationships.
     */
    public void setNewLineMode(Relationship.RelationshipType lineType){
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
     * Changes the UMLArea's mode to Select, allowing the user to
     * select multiple UMLClassBoxes in a selection area.
     */
    public void setSelectionMode() {
        currentMode = Mode.Select;
        clearSelections();

        for (Node n :getChildren()){
            n.setMouseTransparent(true);
        }
    }

    /**
     * Creates a new UMLClassBox at a given set of local coordinates
     * and selects it.
     *
     * @param xPos The local x-position to place the UMLClassBox at.
     * @param yPos The local y-position to place the UMLClassBox at.
     */
    public UMLClassBox newBox (double xPos, double yPos){
        int boxWidth = 120;
        int boxHeight = 160;
        UMLClassBox myBox = new UMLClassBox(boxWidth, boxHeight);
        getChildren().addAll(myBox);
        boxes.add(myBox);
        myBox.setTranslateX(xPos - boxWidth/2);
        myBox.setTranslateY(yPos - boxHeight/2);

        clearSelections();
        myBox.setSelected(true);
        requestFocus();

        return myBox;
    }

    /**
     * Creates a new Relationship based on the two UMLClassBoxes
     * selected as source and destination, and the RelationshipType
     * specified.
     */
    private void newLine (){
        Relationship rel = new Relationship (lineParent1, lineParent2, lineType);
        getChildren().addAll(rel);
        relationships.add(rel);
        rel.toBack();
    }

    public Relationship newLine(UMLClassBox source, UMLClassBox destination, Relationship.RelationshipType lineType){
        Relationship rel = new Relationship(source, destination, lineType);
        getChildren().addAll(rel);
        relationships.add(rel);
        rel.toBack();
        return rel;
    }

    /**
     * Deselects every UMLClassBox currently selected within the
     * UMLArea.
     */
    public void clearSelections(){
        for (UMLClassBox n : boxes){
            n.setSelected(false);
        }
    }

    /**
     * Creates a Rectangle representing the selection area.
     *
     * @param xPos Local x-position of top left corner.
     * @param yPos Local y-position of top left corner.
     * @param width Width of the selection area.
     * @param height Height of the selection area.
     */
    private void initSelectionArea(double xPos, double yPos, double width, double height) {
        selectionArea = new Rectangle(xPos, yPos, width, height);
        selectionArea.setFill(Color.TRANSPARENT);
        selectionArea.setStroke(Color.BLACK);
        selectionArea.getStrokeDashArray().setAll(10.0, 10.0);
    }

    /**
     * Modifies the selection area's local position, width,
     * and height.
     *
     * @param xPos New local x-position of top left corner.
     * @param yPos New local y-position of top left corner.
     * @param width New width of the selection area.
     * @param height New height of the selection area.
     */
    private void setSelectionArea(double xPos, double yPos, double width, double height) {
        selectionArea.setX(xPos);
        selectionArea.setY(yPos);
        selectionArea.setWidth(width);
        selectionArea.setHeight(height);
    }

    /**
     * Selects instances of UMLClassBox class, if any, in the bounds
     * of the selection area.
     */
    private void selectBoxesInArea() {
        //TODO: the selection area doesn't appear precisely at the mouseclick.

        double selectionLeft = selectionArea.getX();
        double selectionRight = selectionLeft + selectionArea.getWidth();
        double selectionTop = selectionArea.getY();
        double selectionBottom = selectionTop + selectionArea.getHeight();

        for (UMLClassBox n : boxes) {

            double leftBound = n.getLeftAnchorPoint().getX();
            double rightBound = n.getRightAnchorPoint().getX();
            double topBound = n.getTopAnchorPoint().getY();
            double bottomBound = n.getBottomAnchorPoint().getY();

            if (leftBound <= selectionRight && rightBound >= selectionLeft && topBound <= selectionBottom && bottomBound >= selectionTop) {
                n.setSelected(true);
            } else {
                n.setSelected(false);
            }

        }
    }

    public void clear(){
        ArrayList<UMLClassBox> boxCopy = new ArrayList<>(boxes);
        for (UMLClassBox n : boxCopy){
            removeDependentRelationships(n);
            boxes.remove(n);
            getChildren().remove(n);

            requestFocus();
            getScene().setCursor(Cursor.DEFAULT);
        }
    }

    public ArrayList<UMLClassBox> getAllBoxes() {
        return boxes;
    }

    public ArrayList<Relationship> getAllLines() {
        return relationships;
    }
}
