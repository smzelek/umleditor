package com.canisdev;

import javafx.scene.layout.Pane;

/**
 * Created by steve on 2/8/2017.
 */
public class UMLArea extends Pane {

    //TODO: focus UMLArea after a delete
    //TODO: create new boxes with an offset
    //TODO: allow focus UMLArea itself if clicked
    //TODO: translate all children if click and drag on UMLArea

    public UMLArea () {
        super();
    }

    public void newBox(){
        UMLClassBox myBox = new UMLClassBox(150, 200);
        getChildren().addAll(myBox);
        myBox.requestFocus();
    }
}
