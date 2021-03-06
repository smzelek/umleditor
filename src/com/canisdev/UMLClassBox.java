package com.canisdev;

import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Instances of UMLClassBox represent a class within a virtual
 * UML diagram. These classes may have a name, attributes, and
 * methods.
 *
 * A UMLClassBox may also have relationships to other classes.
 * The UMLArea holds and manipulates a collection of UMLClassBoxes.
 *
 * @see Relationship
 * @see UMLArea
 */
public class UMLClassBox extends StackPane implements Serializable {

    private VBox contents;
    private TextField nameArea;
    private TextArea attributeArea;
    private TextArea methodArea;
    private static int nextID = 0;
    private int saveID;

    public ResizeNode right, left, topRight, bottomRight, topLeft, bottomLeft, top, bottom;

    private double lastMousePosX;
    private double lastMousePosY;
    private final double SIDE_MARGIN = 2;
    private final double RESIZE_MARGIN = 5;
    public boolean isSelected;
    private ArrayList<Relationship> dependents;

    public int getSaveID() {
        return saveID;
    }

    public String getNameText() {
        return nameArea.getText();
    }

    public String getAttributeText() {
        return attributeArea.getText();
    }

    public String getMethodText() {
        return methodArea.getText();
    }

    /**
     * Creates a UMLClassBox based on size parameters. Margins are
     * <em>added</em> to these dimensions, not subtracted from them.
     * UMLClassBoxes start out selected.
     *
     * @param width Width of the UMLClassBox.
     * @param height Height of the UMLClassBox.
     */
    public UMLClassBox(double width, double height) {
        super();

        width = Math.floor(width);
        height = Math.floor(height);
        saveID = nextID;
        nextID++;

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
        nameArea.setOnKeyPressed(this::handleTextFieldsKeyPressed);
        nameArea.setOnMouseClicked(this::handleTextFieldsMouseClicked);

        //Layout for attributeArea element
        attributeArea = new TextArea();
        attributeArea.setPromptText("Attributes");
        attributeArea.setPrefHeight(80);
        attributeArea.setPrefWidth(Double.MAX_VALUE);
        attributeArea.setMaxWidth(Double.MAX_VALUE);

        //Handlers for attributeArea element
        attributeArea.setOnKeyPressed(this::handleTextFieldsKeyPressed);
        attributeArea.setOnMouseClicked(this::handleTextFieldsMouseClicked);

        //Layout for methodArea element
        methodArea = new TextArea();
        methodArea.setPromptText("Methods");
        methodArea.setPrefHeight(80);
        methodArea.setPrefWidth(Double.MAX_VALUE);
        methodArea.setMaxWidth(Double.MAX_VALUE);

        //Handlers for methodArea element
        methodArea.setOnKeyPressed(this::handleTextFieldsKeyPressed);
        methodArea.setOnMouseClicked(this::handleTextFieldsMouseClicked);

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
        contents.setOnMouseMoved(this::handleMouseMoved);
        contents.setOnMousePressed(this::handleMousePressed);
        contents.setOnMouseDragged(this::handleMouseDragged);
        contents.setOnMouseReleased(this::handleMouseExited);
        contents.setOnMouseExited(this::handleMouseExited);

        //Set radius for all following Resize Nodes.
        ResizeNode.setNodeRadius(RESIZE_MARGIN);

        //Add 8 resize nodes to decorate frame of UML Class Box.
        topLeft = new ResizeNode(ResizeNode.ResizeType.TOP_LEFT);
        setAlignment(topLeft, Pos.TOP_LEFT);
        top = new ResizeNode(ResizeNode.ResizeType.TOP_CENTER);
        setAlignment(top, Pos.TOP_CENTER);
        topRight = new ResizeNode(ResizeNode.ResizeType.TOP_RIGHT);
        setAlignment(topRight, Pos.TOP_RIGHT);
        right = new ResizeNode(ResizeNode.ResizeType.CENTER_RIGHT);
        setAlignment(right, Pos.CENTER_RIGHT);
        bottomRight = new ResizeNode(ResizeNode.ResizeType.BOTTOM_RIGHT);
        setAlignment(bottomRight, Pos.BOTTOM_RIGHT);
        bottom = new ResizeNode(ResizeNode.ResizeType.BOTTOM_CENTER);
        setAlignment(bottom, Pos.BOTTOM_CENTER);
        bottomLeft = new ResizeNode(ResizeNode.ResizeType.BOTTOM_LEFT);
        setAlignment(bottomLeft, Pos.BOTTOM_LEFT);
        left = new ResizeNode(ResizeNode.ResizeType.CENTER_LEFT);
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
        setOnMouseMoved(this::handleMouseMoved);
        setOnMousePressed(this::handleMousePressed);
        setOnMouseDragged(this::handleMouseDragged);
        setOnMouseReleased(this::handleMouseExited);
        setOnMouseExited(this::handleMouseExited);

        //Boxes start out selected.
        setSelected(true);
    }

    /**
     * Handles mouse drag events on a UMLClassBox's frame by
     * translating the UMLClassBox within the UMLArea.
     *
     * @param mouseEvent A mouse drag event fired by the user.
     */
    private void handleMouseDragged (MouseEvent mouseEvent)
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
     * Handles mouse exit events for a UMLClassBox by resetting
     * the mouse cursor to Cursor.DEFAULT.
     *
     * @param mouseEvent A mouse exit event fired by the user.
     */
    private void handleMouseExited (MouseEvent mouseEvent)
    {
        getScene().setCursor(Cursor.DEFAULT);
        mouseEvent.consume();
    }

    /**
     * Handles mouse move events by changing the mouse cursor
     * to Cursor.MOVE, indicating that clicking and dragging will
     * allow the user to translate the UMLClassBox.
     *
     * @param mouseEvent A mouse move event fired by the user.
     */
    private void handleMouseMoved (MouseEvent mouseEvent)
    {
        getScene().setCursor(Cursor.MOVE);
        mouseEvent.consume();
    }

    /**
     * Handles mouse press events by selecting the UMLClassBox.
     * The UMLClassBox will be decorated to indicate selection.
     * Also moves the UMLClassBox to the front of the draw order.
     *
     * @param mouseEvent A mouse press event fired by the user.
     */
    private void handleMousePressed (MouseEvent mouseEvent)
    {
        lastMousePosX = mouseEvent.getSceneX();
        lastMousePosY = mouseEvent.getSceneY();
        ((UMLArea) getParent()).clearSelections();
        setSelected(true);
        toFront();

        mouseEvent.consume();
    }

    /**
     * Handles key press events on the three text fields of a
     * UMLClassBox. Specifically, currently responds to TAB
     * and SHIFT-TAB by focusing the preceding or succeeding
     * text field.
     *
     * @param keyEvent A key press event fired by the user.
     */
    private void handleTextFieldsKeyPressed (KeyEvent keyEvent)
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
     * Handles mouse click events for the three text fields of
     * a UMLClassBox. Clears the selection in the UMLArea.
     * Currently, performs a hack to ensure that the cursor
     * correctly appears when a text field is selected.
     * Note: This appears to possibly be an internal JavaFX issue.
     *
     * @param mouseEvent A mouse click event fired by the user.
     */
    private void handleTextFieldsMouseClicked (MouseEvent mouseEvent){
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
     * Creates an array from the anchor points on a given
     * UMLClassBox instance. These consist of the top middle,
     * left middle, right middle, and bottom middle points
     * on the UMLClassBox. These are used by relationships
     * to determine where their endpoints may be located.
     *
     * @see Relationship
     * @return The points that relationships may link
     * to on this box, in scene coordinates.
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
     * Convenience method to access the left center point on
     * the edge of a UMLClassBox.
     *
     * @return The left center point in local coordinates.
     */
    public Point2D getLeftAnchorPoint () {
        double xpos = getTranslateX() + RESIZE_MARGIN;
        double ypos = getTranslateY() + getHeight()/2;
        return new Point2D(xpos, ypos);
    }

    /**
     * Convenience method to access the right center point on
     * the edge of a UMLClassBox.
     *
     * @return The right center point in local coordinates.
     */
    public Point2D getRightAnchorPoint () {
        double xpos = getTranslateX() + getWidth() - RESIZE_MARGIN + SIDE_MARGIN/2;
        double ypos = getTranslateY() + getHeight()/2;
        return new Point2D(xpos, ypos);
    }

    /**
     * Convenience method to access the bottom center point on
     * the edge of a UMLClassBox.
     *
     * @return The bottom center point in local coordinates.
     */
    public Point2D getBottomAnchorPoint () {
        double xpos = getTranslateX() + getWidth()/2;
        double ypos = getTranslateY() + getHeight() - RESIZE_MARGIN + SIDE_MARGIN/2;
        return new Point2D(xpos, ypos);
    }

    /**
     * Convenience method to access the top center point on
     * the edge of a UMLClassBox.
     *
     * @return The top center point in local coordinates.
     */
    public Point2D getTopAnchorPoint () {
        double xpos = getTranslateX() + getWidth()/2;
        double ypos = getTranslateY() + RESIZE_MARGIN;
        return new Point2D(xpos, ypos);
    }

    /**
     * Convenience method to move scene object by a given 2D
     * offset vector.
     *
     * @param offsetX Translation delta in X
     * @param offsetY Translation delta in Y
     */
    public void translate(double offsetX, double offsetY){
        setTranslateX(getTranslateX() + offsetX);
        setTranslateY(getTranslateY() + offsetY);

        sendMoveEvent();
    }

    /**
     * Fires an event to all dependent relationships that
     * this UMLClassBox has moved, so they must adjust
     * their endpoints, size, and rotation.
     */
    public void sendMoveEvent(){
        for (Relationship r : dependents){
            r.fireEvent(new AnchorEvent(this));
        }
    }

    /**
     * Adds the supplied Relationship onto the list of
     * Relationships that are attached to this UMLClassBox.
     * Keeping track of them allows the UMLClassBox to
     * send events to the Relationship.
     *
     * @param r A Relationship that depends on this UMLClassBox.
     */
    public void addDependentRelationship(Relationship r){
        dependents.add(r);
    }

    /**
     * Decorates a UMLClassBox to indicate selection,
     * which allows for deletion. Selection also reveals
     * ResizeNodes along the border which allow the
     * UMLClassBox to be dynamically resized.
     *
     * @param state Whether or not to select the box.
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

    public void setNameText (String s){
        nameArea.setText(s);
    }

    public void setAttributeText (String s) {
        attributeArea.setText(s);
    }

    public void setMethodText (String s) {
        methodArea.setText(s);
    }

}