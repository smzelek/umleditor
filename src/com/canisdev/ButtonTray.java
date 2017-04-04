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

public class ButtonTray extends HBox {

    private UMLArea umlArea;

    public ButtonTray (UMLArea umlArea) {
        super();
        //setPrefWidth(Double.MAX_VALUE);
        setStyle("-fx-background-color:#A0A0A0;-fx-border-color:black; -fx-border-width:1 0 0 0; -fx-border-style:solid;");
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


        Button btn = new Button();
        btn.setGraphic(boxIcon);
        btn.setTooltip(new Tooltip("Create a new Class Box"));

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

        getChildren().addAll(btn, associationBtn, inheritanceBtn, implementationBtn, dependencyBtn, aggregationBtn, compositionBtn, selectionBtn);

        for (Node n : getChildren()){
            ((Button) n).setPrefWidth(100);
            ((Button) n).setPrefHeight(40);
        }

        //Set up event handlers for button clicks.
        selectionBtn.setOnAction(this::clickSelectionBtn);
        btn.setOnAction(this::clickNewBoxBtn);
        associationBtn.setOnAction(this::clickAssociationBtn);
        inheritanceBtn.setOnAction(this::clickInheritanceBtn);
        implementationBtn.setOnAction(this::clickImplementationBtn);
        dependencyBtn.setOnAction(this::clickDependencyBtn);
        aggregationBtn.setOnAction(this::clickAggregationBtn);
        compositionBtn.setOnAction(this::clickCompositionBtn);
    }

    public void clickSelectionBtn (ActionEvent actionEvent) {
        umlArea.setSelectionMode(true);
    }

    public void clickNewBoxBtn (ActionEvent actionEvent) {
        umlArea.setNewBoxMode(true);
        getScene().setCursor(Cursor.CROSSHAIR);
    }

    public void clickAssociationBtn (ActionEvent actionEvent) {
        umlArea.setNewLineMode(true);
        umlArea.setLineType(0);
        getScene().setCursor(Cursor.CROSSHAIR);
    }

    public void clickInheritanceBtn (ActionEvent actionEvent) {
        umlArea.setNewLineMode(true);
        umlArea.setLineType(1);
        getScene().setCursor(Cursor.CROSSHAIR);
    }

    public void clickCompositionBtn (ActionEvent actionEvent) {
        umlArea.setNewLineMode(true);
        umlArea.setLineType(5);
        getScene().setCursor(Cursor.CROSSHAIR);
    }

    public void clickAggregationBtn (ActionEvent actionEvent) {
        umlArea.setNewLineMode(true);
        umlArea.setLineType(4);
        getScene().setCursor(Cursor.CROSSHAIR);
    }

    public void clickDependencyBtn (ActionEvent actionEvent) {
        umlArea.setNewLineMode(true);
        umlArea.setLineType(3);
        getScene().setCursor(Cursor.CROSSHAIR);
    }

    public void clickImplementationBtn (ActionEvent actionEvent) {
        umlArea.setNewLineMode(true);
        umlArea.setLineType(2);
        getScene().setCursor(Cursor.CROSSHAIR);
    }
}
