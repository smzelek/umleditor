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

    /**
     * Creates an AnchorEvent with a given source UMLClassBox.
     *
     * @param source The UMLClassBox source associated with the AnchorEvent.
     */
    public AnchorEvent (UMLClassBox source) {
        super (source, null, MOVED);
        this.movedBox = source;
    }

    /**
     * Returns a reference to the UMLClassBox that created this AnchorEvent.
     *
     * @return The UMLClassBox that created this AnchorEvent.
     */
    public UMLClassBox getMovedBox(){
        return movedBox;
    }

}
