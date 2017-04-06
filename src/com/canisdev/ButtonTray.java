package com.canisdev;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.control.Tooltip;

/**
 * TODO
 */
public class ButtonTray extends HBox {

    private UMLArea umlArea;

    /**
     * Creates a ButtonTray, and maintains a reference to the
     * UMLArea in order to modify it.
     *
     * @param umlArea The UMLArea
     */
    public ButtonTray (UMLArea umlArea) {
        super();
        setId("button-tray");
        setAlignment(Pos.CENTER);
        setPadding(new Insets(10, 5, 10, 5));
        setSpacing(5);

        this.umlArea = umlArea;

        //Icons for all buttons.
        ImageView boxIcon = new ImageView(new Image(getClass().getResourceAsStream("img/box.png")));
        ImageView associationIcon = new ImageView(new Image(getClass().getResourceAsStream("img/association.png")));
        ImageView inheritanceIcon = new ImageView(new Image(getClass().getResourceAsStream("img/inheritance.png")));
        ImageView implementationIcon = new ImageView(new Image(getClass().getResourceAsStream("img/implementation.png")));
        ImageView dependencyIcon = new ImageView(new Image(getClass().getResourceAsStream("img/dependency.png")));
        ImageView aggregationIcon = new ImageView(new Image(getClass().getResourceAsStream("img/aggregation.png")));
        ImageView compositionIcon = new ImageView(new Image(getClass().getResourceAsStream("img/composition.png")));
        ImageView selectionIcon = new ImageView(new Image(getClass().getResourceAsStream("img/selection.png")));


        Button newBoxBtn = new Button();
        newBoxBtn.setGraphic(boxIcon);
        newBoxBtn.setTooltip(new Tooltip("Create a new Class Box"));

        Button associationBtn = new Button();
        associationBtn.setGraphic(associationIcon);
        associationBtn.setTooltip(new Tooltip("Add Association"));

        Button inheritanceBtn = new Button();
        inheritanceBtn.setGraphic(inheritanceIcon);
        inheritanceBtn.setTooltip(new Tooltip("Add Inheritance"));

        Button implementationBtn = new Button();
        implementationBtn.setGraphic(implementationIcon);
        implementationBtn.setTooltip(new Tooltip("Add Implementation"));

        Button dependencyBtn = new Button();
        dependencyBtn.setGraphic(dependencyIcon);
        dependencyBtn.setTooltip(new Tooltip("Add Dependency"));

        Button aggregationBtn = new Button();
        aggregationBtn.setGraphic(aggregationIcon);
        aggregationBtn.setTooltip(new Tooltip("Add Aggregation"));

        Button compositionBtn = new Button();
        compositionBtn.setGraphic(compositionIcon);
        compositionBtn.setTooltip(new Tooltip("Add Composition"));

        Button selectionBtn = new Button();
        selectionBtn.setGraphic(selectionIcon);
        selectionBtn.setTooltip(new Tooltip("Select Region"));

        //Set up event handlers for button clicks.
        newBoxBtn.setOnAction(this::clickNewBoxBtn);
        newBoxBtn.setId("newBoxBtn");
        associationBtn.setOnAction(this::clickAssociationBtn);
        inheritanceBtn.setOnAction(this::clickInheritanceBtn);
        implementationBtn.setOnAction(this::clickImplementationBtn);
        dependencyBtn.setOnAction(this::clickDependencyBtn);
        aggregationBtn.setOnAction(this::clickAggregationBtn);
        aggregationBtn.setId("aggregationBtn");
        compositionBtn.setOnAction(this::clickCompositionBtn);
        selectionBtn.setOnAction(this::clickSelectionBtn);

        getChildren().addAll(newBoxBtn, associationBtn, inheritanceBtn, implementationBtn, dependencyBtn, aggregationBtn, compositionBtn, selectionBtn);

        for (Node n : getChildren()){
            ((Button) n).setPrefWidth(100);
            ((Button) n).setPrefHeight(40);
        }
    }

    /**
     * Allows the user to change the UMLArea mode in order to
     * create a new UMLClassBox on click events.
     *
     * @param actionEvent An interaction event fired by the user.
     */
    public void clickNewBoxBtn (ActionEvent actionEvent) {
        umlArea.setNewBoxMode();
        getScene().setCursor(Cursor.CROSSHAIR);
    }

    /**
     * Allows the user to change the UMLArea mode to allow
     * creating a new Aggregation Relationship by selecting
     * two UMLClassBoxes.
     *
     * @param actionEvent An interaction event fired by the user.
     */
    public void clickAggregationBtn (ActionEvent actionEvent) {
        umlArea.setNewLineMode(Relationship.RelationshipType.Aggregation);
        getScene().setCursor(Cursor.CROSSHAIR);
    }

    /**
     * Allows the user to change the UMLArea mode to allow
     * creating a new Association Relationship by selecting
     * two UMLClassBoxes.
     *
     * @param actionEvent An interaction event fired by the user.
     */
    public void clickAssociationBtn (ActionEvent actionEvent) {
        umlArea.setNewLineMode(Relationship.RelationshipType.Association);
        getScene().setCursor(Cursor.CROSSHAIR);
    }

    /**
     * Allows the user to change the UMLArea mode to allow
     * creating a new Composition Relationship by selecting
     * two UMLClassBoxes.
     *
     * @param actionEvent An interaction event fired by the user.
     */
    public void clickCompositionBtn (ActionEvent actionEvent) {
        umlArea.setNewLineMode(Relationship.RelationshipType.Composition);
        getScene().setCursor(Cursor.CROSSHAIR);
    }

    /**
     * Allows the user to change the UMLArea mode to allow
     * creating a new Dependency Relationship by selecting
     * two UMLClassBoxes.
     *
     * @param actionEvent An interaction event fired by the user.
     */
    public void clickDependencyBtn (ActionEvent actionEvent) {
        umlArea.setNewLineMode(Relationship.RelationshipType.Dependency);
        getScene().setCursor(Cursor.CROSSHAIR);
    }

    /**
     * Allows the user to change the UMLArea mode to allow
     * creating a new Implementation Relationship by selecting
     * two UMLClassBoxes.
     *
     * @param actionEvent An interaction event fired by the user.
     */
    public void clickImplementationBtn (ActionEvent actionEvent) {
        umlArea.setNewLineMode(Relationship.RelationshipType.Implementation);
        getScene().setCursor(Cursor.CROSSHAIR);
    }

    /**
     * Allows the user to change the UMLArea mode to allow
     * creating a new Inheritance Relationship by selecting
     * two UMLClassBoxes.
     *
     * @param actionEvent An interaction event fired by the user.
     */
    public void clickInheritanceBtn (ActionEvent actionEvent) {
        umlArea.setNewLineMode(Relationship.RelationshipType.Inheritance);
        getScene().setCursor(Cursor.CROSSHAIR);
    }

    /**
     * Allows the user to change the UMLArea into selection mode.
     *
     * @param actionEvent An interaction event fired by the user.
     */
    public void clickSelectionBtn (ActionEvent actionEvent) {
        umlArea.setSelectionMode();
    }
}
