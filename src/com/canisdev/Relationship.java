package com.canisdev;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;

/**
 * Created by steve on 2/13/2017.
 */

public class Relationship extends Group {

    private UMLClassBox parent;
    private UMLClassBox child;
    private Line path;
    private Polygon arrowHead;
    private Rotate rzArrowHead;
    private double arrowRotationOffset = 90;

    public Relationship(UMLClassBox parent, UMLClassBox child){
        super();

        this.parent = parent;
        this.child = child;

        path = new Line();
        path.setStartX(parent.getRightAnchorPoint().getX());
        path.setStartY(parent.getRightAnchorPoint().getY());
        path.setEndX(child.getLeftAnchorPoint().getX());
        path.setEndY(child.getLeftAnchorPoint().getY());
        path.setStrokeWidth(2);
        path.setStrokeLineCap(StrokeLineCap.SQUARE);

        double[] arrowShape = new double[] { 0,0,10,20,-10,20 };
        Polygon p = new Polygon(arrowShape);

        p.setFill(Color.WHITE);
        p.setStroke(Color.BLACK);
        p.setTranslateX(path.getEndX());
        p.setTranslateY(path.getEndY());

        rzArrowHead = new Rotate();
        rzArrowHead.setAxis(Rotate.Z_AXIS);
        p.getTransforms().add(rzArrowHead);


        double opposite = path.getStartY() - path.getEndY();
        double adjacent = path.getEndX() - path.getStartX();
        double angle = Math.atan(opposite/adjacent);
        angle = Math.toDegrees(angle);
        rzArrowHead.setAngle(arrowRotationOffset - angle);

        getChildren().addAll(path, p);

        parent.addDependentRelationship(this);
        child.addDependentRelationship(this);

        addEventHandler(AnchorEvent.MOVED, new EventHandler<AnchorEvent>() {
            @Override
            public void handle(AnchorEvent event) {
                if (event.getMovedBox().equals(parent)){
                    path.setStartX(event.getMovedBox().getRightAnchorPoint().getX());
                    path.setStartY(event.getMovedBox().getRightAnchorPoint().getY());

                    event.consume();
                }else if (event.getMovedBox().equals(child)){
                    path.setEndX(event.getMovedBox().getLeftAnchorPoint().getX());
                    path.setEndY(event.getMovedBox().getLeftAnchorPoint().getY());

                    p.setTranslateX(path.getEndX());
                    p.setTranslateY(path.getEndY());
                    double opposite = path.getStartY() - path.getEndY();
                    double adjacent = path.getEndX() - path.getStartX();
                    double angle = Math.atan(opposite/adjacent);
                    angle = Math.toDegrees(angle);
                    rzArrowHead.setAngle(arrowRotationOffset - angle);
                    //p.getTransforms().add(new Rotate(arrowRotationOffset - angle));

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
