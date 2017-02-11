package com.canisdev;

import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import java.util.ArrayList;

/**
 * Created by steve on 2/8/2017.
 */
public class UMLArea extends Pane {

    //TODO: create new boxes with an offset

    private double lastMousePosX;
    private double lastMousePosY;

    public UMLArea () {
        super();

        setOnKeyReleased((keyEvent) -> {
            //delete any nodes that are selected
            if (keyEvent.getCode() == KeyCode.DELETE || keyEvent.getCode() == KeyCode.BACK_SPACE){

                ArrayList<Node> children = new ArrayList<>(getChildren());

                for (Node n : children){
                    if (((UMLClassBox) n).isSelected) {
                        getChildren().remove(n);
                        requestFocus();
                    }
                }

                //TODO: bug? can delete while resizing
            }
        });

        setOnMouseClicked((mouseEvent) -> {
            requestFocus();
        });

        setOnMousePressed((mouseEvent) -> {
            lastMousePosX = mouseEvent.getSceneX();
            lastMousePosY = mouseEvent.getSceneY();

            clearSelections();
            setCursor(Cursor.MOVE);

            requestFocus();
        });

        setOnMouseReleased((mouseEvent) -> {
            setCursor(Cursor.DEFAULT);
        });

        setOnMouseDragged((mouseEvent) -> {
            double offsetX = mouseEvent.getSceneX() - lastMousePosX;
            double offsetY = mouseEvent.getSceneY() - lastMousePosY;

            //translate all children
            for (Node n : getChildren()){
                n.setTranslateX(n.getTranslateX() + offsetX);
                n.setTranslateY(n.getTranslateY() + offsetY);
            }

            lastMousePosX = mouseEvent.getSceneX();
            lastMousePosY = mouseEvent.getSceneY();
        });
    }

    public void newBox(){
        UMLClassBox myBox = new UMLClassBox(150, 200);
        getChildren().addAll(myBox);

        clearSelections();
        myBox.setSelected(true);

        requestFocus();
    }

    public void clearSelections(){
        for (Node n : getChildren()){
            ((UMLClassBox) n).setSelected(false);
        }
    }

}
