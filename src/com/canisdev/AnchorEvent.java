package com.canisdev;

import javafx.event.Event;
import javafx.event.EventType;
import javafx.geometry.Point2D;

/**
 * Created by steve on 2/15/2017.
 */
public class AnchorEvent extends Event {
    public static final EventType<AnchorEvent> MOVED = new EventType<>(Event.ANY, "MOVED");
    private UMLClassBox movedBox;

    public UMLClassBox getMovedBox(){
        return movedBox;
    }

    public AnchorEvent (UMLClassBox source) {
        super (source, null, MOVED);
        this.movedBox = source;
    }
}
