package com.canisdev;

import javafx.event.Event;
import javafx.event.EventType;

/**
 * Convenience class for a custom event our scene objects
 * can fire and react to.
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
