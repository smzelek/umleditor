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

    public static final int TOP_LEFT = 0;
    public static final int TOP_CENTER = 1;
    public static final int TOP_RIGHT = 2;
    public static final int CENTER_RIGHT = 3;
    public static final int BOTTOM_RIGHT = 4;
    public static final int BOTTOM_CENTER = 5;
    public static final int BOTTOM_LEFT = 6;
    public static final int CENTER_LEFT = 7;

    private static double RADIUS = 0;

    private double lastMousePosX;
    private double lastMousePosY;
    private double offsetX;
    private double offsetY;
    private final int resizeType;

    private double parentRightMarginPos;
    private double parentBottomMarginPos;

    /**
     *
     * @param resizeType
     * @param parent
     */
    public ResizeNode (int resizeType, UMLClassBox parent){
        super(RADIUS);

        assert (resizeType >= 0 && resizeType <= 7);
        this.resizeType = resizeType;
        setId("resize-circle");
        setOnMouseMoved(this::setMouseoverCursor);
        setOnMousePressed(this::handleMouseDown);
        setOnMouseDragged(this::handleMouseDrag);
    }

    /**
     * Defines draw size of the circles.
     * @param newRadius
     */
    public static void setNodeRadius (double newRadius) {
        RADIUS = newRadius;
    }

    /**
     * Sets the custom cursor for this resize type.
     * @param mouseEvent
     */
    private void setMouseoverCursor (MouseEvent mouseEvent)
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
     * Resize and translate the parent UMLClassBox based on
     * which node is clicked.
     * @param mouseEvent
     */
    private void handleMouseDrag (MouseEvent mouseEvent)
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
     * Mouse pressed handler performs setup to handle resizing
     * that will occur when dragging begins.
     * @param mouseEvent
     */
    private void handleMouseDown (MouseEvent mouseEvent)
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
     * Increases vertical box height and translates upward at an equal rate.
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
     * Increase horizontal box width; no translate.
     */
    private void resizeRight(){
        UMLClassBox parent = (UMLClassBox) getParent();
        parent.setPrefWidth(parent.getPrefWidth() + offsetX);
    }

    /**
     * Increase vertical box height; no translate;
     */
    private void resizeBottom(){
        UMLClassBox parent = (UMLClassBox) getParent();
        parent.setPrefHeight(parent.getPrefHeight() + offsetY);
    }

    /**
     * Increase horizontal box width and translate left.
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
