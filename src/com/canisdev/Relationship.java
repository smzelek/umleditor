package com.canisdev;

import javafx.event.EventHandler;
import javafx.scene.shape.Line;

/**
 * Created by steve on 2/13/2017.
 */

public class Relationship extends Line {

    private UMLClassBox parent;
    private UMLClassBox child;

    public Relationship(UMLClassBox parent, UMLClassBox child){
        super();

        this.parent = parent;
        this.child = child;

        parent.addDependentRelationship(this);
        child.addDependentRelationship(this);

        setStartX(parent.getTranslateX() + parent.getWidth()/2);
        setStartY(parent.getTranslateY() + parent.getHeight()/2);
        setEndX(child.getTranslateX() + child.getWidth()/2);
        setEndY(child.getTranslateY() + child.getHeight()/2);

        addEventHandler(AnchorEvent.MOVED, new EventHandler<AnchorEvent>() {
            @Override
            public void handle(AnchorEvent event) {
                if (event.getMovedBox().equals(parent)){
                    setStartX(event.getX());
                    setStartY(event.getY());

                    event.consume();
                }else if (event.getMovedBox().equals(child)){
                    setEndX(event.getX());
                    setEndY(event.getY());

                    event.consume();
                }
            }
        });


        //todo: find closest anchors, and adjust on translate
        /*
        double shortestDistance = Double.MAX_VALUE;
        ResizeNode parentAnchorNode = parent.top;
        ResizeNode childAnchorNode = child.top;


        for (ResizeNode parNode : parent.getNodes()){
            Point2D parNodePos = parNode.localToScene(parNode.getCenterX(), parNode.getCenterY());

            for (ResizeNode chiNode : child.getNodes()){
                Point2D chiNodePos = chiNode.localToScene(chiNode.getCenterX(), chiNode.getCenterY());

                if (parNodePos.distance(chiNodePos) < shortestDistance){
                    System.out.println("HHHH");
                    shortestDistance = parNodePos.distance(chiNodePos);
                    parentAnchorNode = parNode;
                    childAnchorNode = chiNode;
                }
            }
        }*/

        //Todo: only L-R, U-D pairs allowed... looks better?
        //for parentanchors
        //  for child anchors
        //      compute distance
        //      save shortest pair & their distance
        //      bind to center of shortest pairs
    }

    public boolean dependsOn(UMLClassBox b){
        return (parent.equals(b) || child.equals(b));
    }

}
