package com.canisdev;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

/**
 * Created by steve on 2/7/2017.
 */
public class UMLClassBox extends VBox {

    private TextField nameArea;
    private TextArea attributeArea;
    private TextArea methodArea;

    private double lastMousePosX;
    private double lastMousePosY;

    private final double RESIZE_MARGIN = 5;
    private boolean resizing_top;
    private boolean resizing_right;
    private boolean resizing_bottom;
    private boolean resizing_left;
    public boolean isSelected;

    //TODO: tab goes to next text object? (modular)
    //TODO: better focus indication
    //TODO: fix states of de/focusing, mouse events

    public UMLClassBox (double width, double height){
        super();

        resizing_top = false;
        resizing_right = false;
        resizing_bottom = false;
        resizing_left = false;
        setSelected(true);

        setStyle("-fx-background-color: #C0C0C0; -fx-border-style: solid; -fx-border-width: 1px; -fx-border-color: black;");
        setPadding(new Insets(35 + RESIZE_MARGIN, RESIZE_MARGIN, RESIZE_MARGIN, RESIZE_MARGIN));
        setAlignment(Pos.CENTER);

        nameArea = new TextField();
        nameArea.setPromptText("Name");
        nameArea.setCache(false);

        nameArea.setOnMouseClicked((mouseEvent)-> {
            toFront();
            ((UMLArea) getParent()).clearSelections();
        });

        attributeArea = new TextArea();
        attributeArea.setPromptText("Attributes");
        attributeArea.setCache(false);

        attributeArea.setOnMouseClicked((mouseEvent)-> {
            toFront();
            ((UMLArea) getParent()).clearSelections();
        });

        methodArea = new TextArea();
        methodArea.setPromptText("Methods");
        methodArea.setCache(false);

        methodArea.setOnMouseClicked((mouseEvent)-> {
            toFront();
            ((UMLArea) getParent()).clearSelections();
        });

        setMaxSize(width, height);
        setMinSize(width, height);

        setFillWidth(true);
        getChildren().addAll(nameArea, attributeArea, methodArea);

        setOnMouseMoved((mouseEvent) -> {
            setInResizingArea(mouseEvent);
        });

        setOnMousePressed((mouseEvent) -> {
            lastMousePosX = mouseEvent.getSceneX();
            lastMousePosY = mouseEvent.getSceneY();
            setResizing(mouseEvent);
            toFront();

            ((UMLArea) getParent()).clearSelections();
            setSelected(true);

            mouseEvent.consume();
        });

        setOnMouseReleased((mouseEvent) -> {
            resizing_left = false;
            resizing_top = false;
            resizing_right = false;
            resizing_bottom = false;
            mouseEvent.consume();
        });

        setOnMouseDragged((mouseEvent) -> {
            double offsetX = mouseEvent.getSceneX() - lastMousePosX;
            double offsetY = mouseEvent.getSceneY() - lastMousePosY;

            if (resizing_right){
                double rightmargin = getTranslateX() + getWidth() - RESIZE_MARGIN/2;
                double windowOffsetX = mouseEvent.getSceneX() - rightmargin;

                if (windowOffsetX > 0){ //mouse is to right of right margin
                    setMaxWidth(getMaxWidth() + windowOffsetX);
                }
                else if (getWidth() > getMinWidth()) { //on right margin or to left of it
                    setMaxWidth(getMaxWidth() + windowOffsetX);
                }
            }else if (resizing_left){
                //TODO: some bugginess here, window sometimes jumps while resizing
                double leftmargin = getTranslateX() + RESIZE_MARGIN/2;
                double windowOffsetX = mouseEvent.getSceneX() - leftmargin;

                if (windowOffsetX < 0){ //mouse is to left of left margin
                    //grow window
                    setMaxWidth(getMaxWidth() - windowOffsetX);
                    //appear to remain fixed
                    setTranslateX(getTranslateX() + windowOffsetX);

                }
                else if (getWidth() > getMinWidth()) { //on left margin or to right of it
                    if (getWidth() - windowOffsetX >= getMinWidth()){
                        setMaxWidth(getMaxWidth() - windowOffsetX);
                        //appear to remain fixed
                        setTranslateX(getTranslateX() + windowOffsetX);
                    }
                }
            }
            if (resizing_bottom){
                double bottommargin = getTranslateY() + getHeight() - RESIZE_MARGIN/2;
                double windowOffsetY = mouseEvent.getSceneY() - bottommargin;
                if (windowOffsetY > 0){ //mouse is below bottom margin
                    setMaxHeight(getMaxHeight() + windowOffsetY);
                }
                else if (getHeight() > getMinHeight()) { //above bottom margin
                    setMaxHeight(getMaxHeight() + windowOffsetY);
                }
            } else if (resizing_top) {
                //TODO: some bugginess here, window sometimes jumps while resizing
                double topmargin = getTranslateY() + RESIZE_MARGIN/2;
                double windowOffsetY = mouseEvent.getSceneY() - topmargin;

                if (windowOffsetY < 0){ //mouse is above topmargin
                    //grow window
                    setMaxHeight(getMaxHeight() - windowOffsetY);
                    //appear to remain fixed
                    setTranslateY(getTranslateY() + windowOffsetY);

                }
                else if (getHeight() > getMinHeight()) { //mouse below top margin
                    if (getHeight() - windowOffsetY >= getMinHeight()){
                        setMaxHeight(getMaxHeight() - windowOffsetY);
                        //appear to remain fixed
                        setTranslateY(getTranslateY() + windowOffsetY);
                    }
                }
            }

            //if not doing a resize, do a translate
            if (!(resizing_bottom || resizing_left || resizing_right || resizing_top))
            {
                setCursor(Cursor.MOVE);
                setTranslateX(getTranslateX() + offsetX);
                setTranslateY(getTranslateY() + offsetY);
            }

            lastMousePosX = mouseEvent.getSceneX();
            lastMousePosY = mouseEvent.getSceneY();
            mouseEvent.consume();
        });

        setOnMouseReleased((mouseEvent) -> {
            setCursor(Cursor.DEFAULT);
            mouseEvent.consume();
        });
    }

    public void setSelected (boolean state){
        isSelected = state;
        if (state){
            setStyle("-fx-background-color: #C0C0C0; -fx-border-style: solid; -fx-border-width: 1px; -fx-border-color: red;");
        }else{
            setStyle("-fx-background-color: #C0C0C0; -fx-border-style: solid; -fx-border-width: 1px; -fx-border-color: black;");
        }
    }

    boolean inBottomMargin(MouseEvent mouseEvent){
        return mouseEvent.getY() + RESIZE_MARGIN >= getHeight();
    }

    boolean inTopMargin(MouseEvent mouseEvent){
        return mouseEvent.getY() <= RESIZE_MARGIN;
    }

    boolean inRightMargin(MouseEvent mouseEvent){
        return mouseEvent.getX() + RESIZE_MARGIN >= getWidth();
    }

    boolean inLeftMargin(MouseEvent mouseEvent){
        return mouseEvent.getX() <= RESIZE_MARGIN;
    }

    private void setInResizingArea(MouseEvent mouseEvent){
        if (inTopMargin(mouseEvent)){
            if (inLeftMargin(mouseEvent)){
                setCursor(Cursor.NW_RESIZE);
            }else if (inRightMargin(mouseEvent)){
                setCursor(Cursor.NE_RESIZE);
            }
            else{
                setCursor(Cursor.N_RESIZE);
            }
        }else if (inBottomMargin(mouseEvent)) {
            if (inLeftMargin(mouseEvent)){
                setCursor(Cursor.SW_RESIZE);
            }else if (inRightMargin(mouseEvent)){
                setCursor(Cursor.SE_RESIZE);
            }else{
                setCursor(Cursor.S_RESIZE);
            }
        }else if (inLeftMargin(mouseEvent)){
            setCursor(Cursor.W_RESIZE);
        }else if (inRightMargin(mouseEvent)){
            setCursor(Cursor.E_RESIZE);
        }else {
            setCursor(Cursor.DEFAULT);
        }
    }

    private void setResizing(MouseEvent mouseEvent){
        resizing_top = inTopMargin(mouseEvent);
        resizing_right = inRightMargin(mouseEvent);
        resizing_bottom = inBottomMargin(mouseEvent);
        resizing_left = inLeftMargin(mouseEvent);
    }

}