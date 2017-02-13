package com.canisdev;

import javafx.scene.Cursor;
import javafx.scene.shape.Circle;

/**
 * Created by steve on 2/12/2017.
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
    private Cursor resizeMouseoverCursor;
    private final int resizeType;

    private double parentRightMarginPos;
    private double parentBottomMarginPos;


    public ResizeNode (int resizeType){
        super(RADIUS);

        assert (resizeType >= 0 && resizeType <= 7);
        switch (resizeType){
            case TOP_LEFT:
                resizeMouseoverCursor = Cursor.NW_RESIZE;
                break;
            case TOP_CENTER:
                resizeMouseoverCursor = Cursor.N_RESIZE;
                break;
            case TOP_RIGHT:
                resizeMouseoverCursor = Cursor.NE_RESIZE;
                break;
            case CENTER_RIGHT:
                resizeMouseoverCursor = Cursor.E_RESIZE;
                break;
            case BOTTOM_RIGHT:
                resizeMouseoverCursor = Cursor.SE_RESIZE;
                break;
            case BOTTOM_CENTER:
                resizeMouseoverCursor = Cursor.S_RESIZE;
                break;
            case BOTTOM_LEFT:
                resizeMouseoverCursor = Cursor.SW_RESIZE;
                break;
            case CENTER_LEFT:
                resizeMouseoverCursor = Cursor.W_RESIZE;
                break;
        }

        this.resizeType = resizeType;
        setId("resize-circle");
        setOnMouseMoved((mouseEvent)-> {
            getScene().setCursor(resizeMouseoverCursor);
            mouseEvent.consume();
        });

        setOnMousePressed((mouseEvent) -> {
            lastMousePosX = mouseEvent.getSceneX();
            lastMousePosY = mouseEvent.getSceneY();
            offsetX = 0;
            offsetY = 0;

            UMLClassBox parent = (UMLClassBox) getParent();

            if (parent.getMaxWidth() < parent.getMinWidth()){
                parent.setMaxWidth(parent.getMinWidth());
            }
            if (parent.getMaxHeight() < parent.getMinHeight()){
                parent.setMaxHeight(parent.getMinHeight());
            }

            parentRightMarginPos = parent.getTranslateX() + parent.getMaxWidth();
            parentBottomMarginPos = parent.getTranslateY() + parent.getMaxHeight();

            mouseEvent.consume();
        });

        setOnMouseDragged((mouseEvent) -> {
            offsetX = mouseEvent.getSceneX() - lastMousePosX; //move right = positive offset
            offsetY = mouseEvent.getSceneY() - lastMousePosY; //move down = positive offset

            switch (resizeType){
                case TOP_LEFT:
                    resizeTop();
                    resizeLeft();
                    break;
                case TOP_CENTER:
                    resizeTop();
                    break;
                case TOP_RIGHT:
                    resizeTop();
                    resizeRight();
                    break;
                case CENTER_RIGHT:
                    resizeRight();
                    break;
                case BOTTOM_RIGHT:
                    resizeBottom();
                    resizeRight();
                    break;
                case BOTTOM_CENTER:
                    resizeBottom();
                    break;
                case BOTTOM_LEFT:
                    resizeBottom();
                    resizeLeft();
                    break;
                case CENTER_LEFT:
                    resizeLeft();
                    break;
            }

            lastMousePosX = mouseEvent.getSceneX();
            lastMousePosY = mouseEvent.getSceneY();
            mouseEvent.consume();
        });

    }

    public static void setNodeRadius (double newRadius){
        RADIUS = newRadius;
    }

    private void resizeTop(){
        UMLClassBox parent = (UMLClassBox) getParent();
        parent.setMaxHeight(parent.getMaxHeight() - offsetY);
        if (parent.getMaxHeight() >= parent.getMinHeight()){
            parent.setTranslateY(parentBottomMarginPos - parent.getMaxHeight());
        }
        else{
            parent.setTranslateY(parentBottomMarginPos - parent.getMinHeight());
        }
    }

    private void resizeRight(){
        UMLClassBox parent = (UMLClassBox) getParent();
        parent.setMaxWidth(parent.getMaxWidth() + offsetX);
    }

    private void resizeBottom(){
        UMLClassBox parent = (UMLClassBox) getParent();
        parent.setMaxHeight(parent.getMaxHeight() + offsetY);
    }

    private void resizeLeft(){
        UMLClassBox parent = (UMLClassBox) getParent();
        parent.setMaxWidth(parent.getMaxWidth() - offsetX);
        if (parent.getMaxWidth() >= parent.getMinWidth()){
            parent.setTranslateX(parentRightMarginPos - parent.getMaxWidth());
        }
        else{
            parent.setTranslateX(parentRightMarginPos - parent.getMinWidth());
        }
    }

}
