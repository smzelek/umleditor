package com.canisdev;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class ButtonTray extends HBox {

    public ButtonTray (UMLArea umlArea) {
        super();
        setPrefWidth(Double.MAX_VALUE);
        setStyle("-fx-background-color:#A0A0A0;-fx-border-color:black; -fx-border-width:1 0 0 0; -fx-border-style:solid;");
        setAlignment(Pos.CENTER);
        setPadding(new Insets(10, 5, 10, 5));
        setSpacing(5);

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

        Button associationBtn = new Button();
        associationBtn.setGraphic(associationIcon);

        Button inheritanceBtn = new Button();
        inheritanceBtn.setGraphic(inheritanceIcon);

        Button implementationBtn = new Button();
        implementationBtn.setGraphic(implementationIcon);

        Button dependencyBtn = new Button();
        dependencyBtn.setGraphic(dependencyIcon);

        Button aggregationBtn = new Button();
        aggregationBtn.setGraphic(aggregationIcon);

        Button compositionBtn = new Button();
        compositionBtn.setGraphic(compositionIcon);

        Button selectionBtn = new Button();
        selectionBtn.setGraphic(selectionIcon);

        getChildren().addAll(btn, associationBtn, inheritanceBtn, implementationBtn, dependencyBtn, aggregationBtn, compositionBtn, selectionBtn);

        for (Node n : getChildren()){
            ((Button) n).setPrefWidth(100);
            ((Button) n).setPrefHeight(40);
        }

        //Set up event handlers for button clicks.
        selectionBtn.setOnAction((actionEvent) -> {
            umlArea.setSelectionMode(true);
            umlArea.clearSelections();
        });

        btn.setOnAction((actionEvent) -> {
            umlArea.setNewBoxMode(true);
            umlArea.clearSelections();
            getScene().setCursor(Cursor.CROSSHAIR);
        });

        associationBtn.setOnAction((actionEvent) -> {
            umlArea.setNewLineMode(true);
            umlArea.setLineType(0);
            umlArea.clearSelections();
            getScene().setCursor(Cursor.CROSSHAIR);
        });

        inheritanceBtn.setOnAction((actionEvent) -> {
            umlArea.setNewLineMode(true);
            umlArea.setLineType(1);
            umlArea.clearSelections();
            getScene().setCursor(Cursor.CROSSHAIR);
        });

        implementationBtn.setOnAction((actionEvent) -> {
            umlArea.setNewLineMode(true);
            umlArea.setLineType(2);
            umlArea.clearSelections();
            getScene().setCursor(Cursor.CROSSHAIR);
        });

        dependencyBtn.setOnAction((actionEvent) -> {
            umlArea.setNewLineMode(true);
            umlArea.setLineType(3);
            umlArea.clearSelections();
            getScene().setCursor(Cursor.CROSSHAIR);
        });

        aggregationBtn.setOnAction((actionEvent) -> {
            umlArea.setNewLineMode(true);
            umlArea.setLineType(4);
            umlArea.clearSelections();
            getScene().setCursor(Cursor.CROSSHAIR);
        });

        compositionBtn.setOnAction((actionEvent) -> {
            umlArea.setNewLineMode(true);
            umlArea.setLineType(5);
            umlArea.clearSelections();
            getScene().setCursor(Cursor.CROSSHAIR);
        });
    }
}
