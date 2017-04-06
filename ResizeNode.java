package com.canisdev;

import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;

/**
 * A UMLClassBox has 8 ResizeNodes on its edges.
 * This scene object appears as a circle and
 * uses custom mouse event handlers to implement resizing
 * its parent UMLClassBox.
 */
public class ResizeNode extends Circle {

    enum ResizeType
    {
        TOP_LEFT, TOP_CENTER, TOP_RIGHT, CENTER_RIGHT,
        BOTTOM_RIGHT, BOTTOM_CENTER, BOTTOM_LEFT, CENTER_LEFT
    }

    private static double RADIUS = 0;

    private double lastMousePosX;
    private double lastMousePosY;
    private double offsetX;
    private double offsetY;
    private final ResizeType resizeType;

    private double parentRightMarginPos;
    private double parentBottomMarginPos;

    /**
     * Creates a ResizeNode object with a given ResizeType, which is
     * analogous to the location it occupies.
     *
     * @param resizeType Which position the ResizeNode occupies.
     */
    public ResizeNode (ResizeType resizeType){
        super(RADIUS);

        this.resizeType = resizeType;

        setId("resize-circle");
        setOnMouseMoved(this::handleMouseMoved);
        setOnMousePressed(this::handleMousePressed);
        setOnMouseDragged(this::handleMouseDragged);
    }

    /**
     * Sets the static draw size for all ResizeNodes. This method
     * should be called before any ResizeNodes are constructed.
     *
     * @param newRadius A radius to use for all ResizeNodes.
     */
    public static void setNodeRadius (double newRadius) {
        RADIUS = newRadius;
    }

    /**
     * Handles mouse over events for a ResizeNode by setting the
     * cursor based on its resizeType.
     *
     * @param mouseEvent A mouse moved event fired by the user.
     */
    private void handleMouseMoved (MouseEvent mouseEvent)
    {
        mouseEvent.consume();
        switch (resizeType){
            case TOP_LEFT:
            {
                getScene().setCursor(Cursor.NW_RESIZE);
                break;
            }
            case TOP_CENTER:
            {
                getScene().setCursor(Cursor.N_RESIZE);
                break;
            }
            case TOP_RIGHT:
            {
                getScene().setCursor(Cursor.NE_RESIZE);
                break;
            }
            case CENTER_RIGHT:
            {
                getScene().setCursor(Cursor.E_RESIZE);
                break;
            }
            case BOTTOM_RIGHT:
            {
                getScene().setCursor(Cursor.SE_RESIZE);
                break;
            }
            case BOTTOM_CENTER:
            {
                getScene().setCursor(Cursor.S_RESIZE);
                break;
            }
            case BOTTOM_LEFT:
            {
                getScene().setCursor(Cursor.SW_RESIZE);
                break;
            }
            case CENTER_LEFT:
            {
                getScene().setCursor(Cursor.W_RESIZE);
                break;
            }
        }
    }

    /**
     * Handles mouse drag events for a ResizeNode by resizing
     * and translating the UMLClassBox based on ResizeType.
     *
     * @param mouseEvent A mouse drag event fired by the user.
     */
    private void handleMouseDragged (MouseEvent mouseEvent)
    {
        UMLClassBox parent = (UMLClassBox) getParent();
        mouseEvent.consume();
        offsetX = mouseEvent.getSceneX() - lastMousePosX; //move right = positive offset
        offsetY = mouseEvent.getSceneY() - lastMousePosY; //move down = positive offset
        lastMousePosX = mouseEvent.getSceneX();
        lastMousePosY = mouseEvent.getSceneY();
        parent.sendMoveEvent();

        switch (resizeType){
            case TOP_LEFT:
            {
                resizeTop();
                resizeLeft();
                break;
            }
            case TOP_CENTER:
            {
                resizeTop();
                break;
            }
            case TOP_RIGHT:
            {
                resizeTop();
                resizeRight();
                break;
            }
            case CENTER_RIGHT:
            {
                resizeRight();
                break;
            }
            case BOTTOM_RIGHT:
            {
                resizeBottom();
                resizeRight();
                break;
            }
            case BOTTOM_CENTER:
            {
                resizeBottom();
                break;
            }
            case BOTTOM_LEFT:
            {
                resizeBottom();
                resizeLeft();
                break;
            }
            case CENTER_LEFT:
            {
                resizeLeft();
                break;
            }
        }
    }

    /**
     * Handles mouse press events for a ResizeNode by performing
     * the setup to allow resizing during mouse drag events.
     *
     * @param mouseEvent A mouse press event fired by the user.
     */
    private void handleMousePressed (MouseEvent mouseEvent)
    {
        mouseEvent.consume();

        UMLClassBox parent = (UMLClassBox) getParent();
        lastMousePosX = mouseEvent.getSceneX();
        lastMousePosY = mouseEvent.getSceneY();
        offsetX = 0;
        offsetY = 0;

        if (parent.getPrefWidth() < parent.getMinWidth()){
            parent.setPrefWidth(parent.getMinWidth());
        }
        if (parent.getPrefHeight() < parent.getMinHeight()){
            parent.setPrefHeight(parent.getMinHeight());
        }

        parentRightMarginPos = parent.getTranslateX() + parent.getPrefWidth();
        parentBottomMarginPos = parent.getTranslateY() + parent.getPrefHeight();
    }

    /**
     * Modifies the parent UMLClassBox of this ResizeNode by
     * increasing the parent's height. Also translates the
     * parent UMLClassBox upward at an equal rate to height
     * increases, so that only the top margin of the UMLClassBox
     * appears to move.
     */
    private void resizeTop(){
        UMLClassBox parent = (UMLClassBox) getParent();
        parent.setPrefHeight(parent.getPrefHeight() - offsetY);
        if (parent.getPrefHeight() > parent.getMinHeight()){
            parent.setTranslateY(parentBottomMarginPos - parent.getPrefHeight());
        }
        else{
            parent.setTranslateY(parentBottomMarginPos - parent.getMinHeight());
        }
    }

    /**
     * Modifies the parent UMLClassBox of this ResizeNode by
     * increasing the parent's width.
     */
    private void resizeRight(){
        UMLClassBox parent = (UMLClassBox) getParent();
        parent.setPrefWidth(parent.getPrefWidth() + offsetX);
    }

    /**
     * Modifies the parent UMLClassBox of this ResizeNode by
     * increasing the parent's height.
     */
    private void resizeBottom(){
        UMLClassBox parent = (UMLClassBox) getParent();
        parent.setPrefHeight(parent.getPrefHeight() + offsetY);
    }

    /**
     * Modifies the parent UMLClassBox of this ResizeNode by
     * increasing the parent's width. Also translates the
     * parent UMLClassBox left at an equal rate to width
     * increases, so that only the left margin of the UMLClassBox
     * appears to move.
     */
    private void resizeLeft(){
        UMLClassBox parent = (UMLClassBox) getParent();
        parent.setPrefWidth(parent.getPrefWidth() - offsetX);

        if (parent.getPrefWidth() > parent.getMinWidth()){
            parent.setTranslateX(parentRightMarginPos - parent.getPrefWidth());
        }
        else{
            parent.setTranslateX(parentRightMarginPos - parent.getMinWidth());
        }
    }
}
