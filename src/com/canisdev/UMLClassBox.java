//TODO: examine states of de/focusing, mouse events, shift click multi-select

package com.canisdev;

import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
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

    /**
     * Constructor for the UMLClassBox object. Creates a new UMLClassBox
     * based on size parameters. Margins are <em>added</em> to these dimensions,
     * not subtracted from them.
     * <p>
     * Sets up GUI sub-elements, and binds mouse and key handlers for each.
     * UMLClassBoxes start out selected.
     *
     * @param width - width of the UMLClassBox
     * @param height - height of the UMLClassBox
     */
    public UMLClassBox(double width, double height){
        super();

        width = Math.floor(width);
        height = Math.floor(height);

        dependents = new ArrayList<>();

        width += (SIDE_MARGIN+RESIZE_MARGIN)*2 + .1;
        height += (SIDE_MARGIN+RESIZE_MARGIN)*2 + .1;

        //Layout for nameArea element
        nameArea = new TextField();
        nameArea.setPromptText("Name");
        nameArea.setAlignment(Pos.TOP_CENTER);
        nameArea.setPrefHeight(25);
        nameArea.setPrefWidth(Double.MAX_VALUE);
        nameArea.setMaxWidth(Double.MAX_VALUE);

        //Handlers for nameArea element
        nameArea.setOnKeyPressed(this::cycleTextFields);
        nameArea.setOnMouseClicked(this::clickTextField);

        //Layout for attributeArea element
        attributeArea = new TextArea();
        attributeArea.setPromptText("Attributes");
        attributeArea.setPrefHeight(80);
        attributeArea.setPrefWidth(Double.MAX_VALUE);
        attributeArea.setMaxWidth(Double.MAX_VALUE);

        //Handlers for attributeArea element
        attributeArea.setOnKeyPressed(this::cycleTextFields);
        attributeArea.setOnMouseClicked(this::clickTextField);

        //Layout for methodArea element
        methodArea = new TextArea();
        methodArea.setPromptText("Methods");
        methodArea.setPrefHeight(80);
        methodArea.setPrefWidth(Double.MAX_VALUE);
        methodArea.setMaxWidth(Double.MAX_VALUE);

        //Handlers for methodArea element
        methodArea.setOnKeyPressed(this::cycleTextFields);
        methodArea.setOnMouseClicked(this::clickTextField);

        //Layout for contents parent
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

        //Handlers for contents parent
        contents.setOnMouseMoved(this::setMouseoverCursor);
        contents.setOnMousePressed(this::selectBox);
        contents.setOnMouseDragged(this::dragBox);
        contents.setOnMouseReleased(this::resetMouseCursor);
        contents.setOnMouseExited(this::resetMouseCursor);

        //Set radius for all following Resize Nodes.
        ResizeNode.setNodeRadius(RESIZE_MARGIN);

        //Add 8 resize nodes to decorate frame of UML Class Box.
        topLeft = new ResizeNode(ResizeNode.TOP_LEFT, this);
        setAlignment(topLeft, Pos.TOP_LEFT);
        top = new ResizeNode(ResizeNode.TOP_CENTER, this);
        setAlignment(top, Pos.TOP_CENTER);
        topRight = new ResizeNode(ResizeNode.TOP_RIGHT, this);
        setAlignment(topRight, Pos.TOP_RIGHT);
        right = new ResizeNode(ResizeNode.CENTER_RIGHT, this);
        setAlignment(right, Pos.CENTER_RIGHT);
        bottomRight = new ResizeNode(ResizeNode.BOTTOM_RIGHT, this);
        setAlignment(bottomRight, Pos.BOTTOM_RIGHT);
        bottom = new ResizeNode(ResizeNode.BOTTOM_CENTER, this);
        setAlignment(bottom, Pos.BOTTOM_CENTER);
        bottomLeft = new ResizeNode(ResizeNode.BOTTOM_LEFT, this);
        setAlignment(bottomLeft, Pos.BOTTOM_LEFT);
        left = new ResizeNode(ResizeNode.CENTER_LEFT, this);
        setAlignment(left, Pos.CENTER_LEFT);

        //Layout for UMLClassBox object.
        getChildren().addAll(contents, topLeft, top, topRight, right, bottomRight, bottom, bottomLeft, left);
        setAlignment(contents, Pos.CENTER);
        setMargin(contents, new Insets(RESIZE_MARGIN));
        setId("uml-class-box-frame");
        setPrefSize(width, height);
        setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        setMinSize(width, height);

        //Handlers for UMLClassBox object.
        setOnMouseMoved(this::setMouseoverCursor);
        setOnMousePressed(this::selectBox);
        setOnMouseDragged(this::dragBox);
        setOnMouseReleased(this::resetMouseCursor);
        setOnMouseExited(this::resetMouseCursor);

        //Boxes start out selected.
        setSelected(true);
    }

    /**
     *
     * @param mouseEvent
     */
    private void dragBox (MouseEvent mouseEvent)
    {
        double offsetX = mouseEvent.getSceneX() - lastMousePosX;
        double offsetY = mouseEvent.getSceneY() - lastMousePosY;

        getScene().setCursor(Cursor.MOVE);
        translate(offsetX, offsetY);

        lastMousePosX = mouseEvent.getSceneX();
        lastMousePosY = mouseEvent.getSceneY();
        mouseEvent.consume();
    }

    /**
     *
     * @param mouseEvent
     */
    private void resetMouseCursor (MouseEvent mouseEvent)
    {
        getScene().setCursor(Cursor.DEFAULT);
        mouseEvent.consume();
    }

    /**
     *
     * @param mouseEvent
     */
    private void setMouseoverCursor (MouseEvent mouseEvent)
    {
        getScene().setCursor(Cursor.MOVE);
        mouseEvent.consume();
    }

    /**
     *
     * @param mouseEvent
     */
    private void selectBox (MouseEvent mouseEvent)
    {
        lastMousePosX = mouseEvent.getSceneX();
        lastMousePosY = mouseEvent.getSceneY();
        ((UMLArea) getParent()).clearSelections();
        setSelected(true);
        toFront();

        mouseEvent.consume();
    }

    /**
     *
     * @param keyEvent
     */
    private void cycleTextFields(KeyEvent keyEvent)
    {
        if (keyEvent.getCode() == KeyCode.TAB) {
            if (keyEvent.isShiftDown()) {
                if (keyEvent.getSource().equals(nameArea))
                {
                    methodArea.requestFocus();
                }
                else if (keyEvent.getSource().equals(attributeArea))
                {
                    nameArea.requestFocus();
                }
                else {
                    attributeArea.requestFocus();
                }
            }
            else {
                if (keyEvent.getSource().equals(nameArea))
                {
                    attributeArea.requestFocus();
                }
                else if (keyEvent.getSource().equals(attributeArea))
                {
                    methodArea.requestFocus();
                }
                else {
                    nameArea.requestFocus();
                }
            }
            keyEvent.consume();
        }
    }

    /**
     *
     * @param mouseEvent
     */
    private void clickTextField (MouseEvent mouseEvent){
        toFront();
        ((UMLArea) getParent()).clearSelections();

        Object source = mouseEvent.getSource();
        if (source instanceof TextArea){
            TextArea textArea = (TextArea) source;
            if (textArea.getText().length() == 0)
            {
                textArea.insertText(0, " ");
                textArea.deleteText(0,1);
            }
        }
        mouseEvent.consume();
    }
    /**
     * Fetches an array of possible linking points for a relationship.
     * @return
     */
    public ArrayList<Point2D> getAnchorPoints() {
        return new ArrayList<>(Arrays.asList(getTopAnchorPoint(),
                                             getRightAnchorPoint(),
                                             getBottomAnchorPoint(),
                                             getLeftAnchorPoint()));
    }

    //Anchor point getters:
    //*******************************************************************

    /**
     *
     * @return
     */
    public Point2D getLeftAnchorPoint () {
        double xpos = getTranslateX() + RESIZE_MARGIN;
        double ypos = getTranslateY() + getHeight()/2;
        return new Point2D(xpos, ypos);
    }

    /**
     *
     * @return
     */
    public Point2D getRightAnchorPoint () {
        double xpos = getTranslateX() + getWidth() - RESIZE_MARGIN + SIDE_MARGIN/2;
        double ypos = getTranslateY() + getHeight()/2;
        return new Point2D(xpos, ypos);
    }

    /**
     *
     * @return
     */
    public Point2D getBottomAnchorPoint () {
        double xpos = getTranslateX() + getWidth()/2;
        double ypos = getTranslateY() + getHeight() - RESIZE_MARGIN + SIDE_MARGIN/2;
        return new Point2D(xpos, ypos);
    }

    /**
     *
     * @return
     */
    public Point2D getTopAnchorPoint () {
        double xpos = getTranslateX() + getWidth()/2;
        double ypos = getTranslateY() + RESIZE_MARGIN;
        return new Point2D(xpos, ypos);
    }

    /**
     * Convenience method to move scene object by a given offset vector.
     * @param offsetX
     * @param offsetY
     */
    public void translate(double offsetX, double offsetY){
        setTranslateX(getTranslateX() + offsetX);
        setTranslateY(getTranslateY() + offsetY);

        sendMoveEvent();
    }

    /**
     * Lets dependent relationships know that they must adjust
     * their endpoints, size, and rotation.
     */
    public void sendMoveEvent(){
        for (Relationship r : dependents){
            r.fireEvent(new AnchorEvent(this));
        }
    }

    /**
     * Keep track of attached relationship objects by adding
     * them to the list of ones attached to this list.
     *
     * @param r
     */
    public void addDependentRelationship(Relationship r){
        dependents.add(r);
    }

    /**
     * Change appearance to indicate selection, which
     * allows resizing and deleting. Resize nodes appear
     * upon selection.
     *
     * @param state - set selected to true or false
     */
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