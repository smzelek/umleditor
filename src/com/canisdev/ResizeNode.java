package com.canisdev;

import javafx.scene.Cursor;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;

/**
 * Created by steve on 2/12/2017.
 */
public class ResizeNode extends Circle {

    public ResizeNode (){
        super(6);

        setId("resize-circle");
        setOnMouseMoved((mouseEvent)-> {
            getScene().setCursor(Cursor.E_RESIZE);
            mouseEvent.consume();
        });
    }
}
