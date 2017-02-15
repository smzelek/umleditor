package com.canisdev;

import javafx.event.Event;
import javafx.event.EventType;

/**
 * Created by steve on 2/15/2017.
 */
public class AnchorEvent extends Event {
    public static final EventType<AnchorEvent> MOVED = new EventType<>(Event.ANY, "MOVED");
    private int x, y;
    private UMLClassBox movedBox;

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public UMLClassBox getMovedBox(){
        return movedBox;
    }

    public AnchorEvent (UMLClassBox source) {
        super (source, null, MOVED);
        this.x = (int) (source.getTranslateX() + source.getWidth()/2);
        this.y = (int) (source.getTranslateY() + source.getHeight()/2);
        this.movedBox = source;
    }
}
