package com.canisdev;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;

/**
 * Created by steve on 2/13/2017.
 */

public class Relationship extends Group {

    private UMLClassBox parent;
    private UMLClassBox child;
    //parent anchor point
    //child anchor point
    private Line path;
    private Polygon shape;
    private Rotate rzArrowHead;
    private double arrowRotationOffset = 90;

    //todo: "child" tracks mouse until 2nd parent selected
    //if child is still null, track mouse
    //otherwise update parent/child anchors based on closeness
    //this class can't get move events of the mouse in the general uml area...

    public Relationship(UMLClassBox parent, UMLClassBox child){
        super();

        this.parent = parent;
        this.child = child;
        parent.addDependentRelationship(this);
        child.addDependentRelationship(this);

        path = new Line();
        path.setStrokeWidth(2);

        rzArrowHead = new Rotate();
        rzArrowHead.setAxis(Rotate.Z_AXIS);
        //setArrowShape();

        findClosestAnchorPair();

        getChildren().addAll(path);

        EventHandler<AnchorEvent> maintainLink = new EventHandler<AnchorEvent>() {
            @Override
            public void handle(AnchorEvent event) {
                findClosestAnchorPair();
                updateShapeTransform();
                event.consume();
            }
        };

        setEventHandler(AnchorEvent.MOVED, maintainLink);
        findClosestAnchorPair();
    }

    private void findClosestAnchorPair(){
        //todo: find closest anchors, and adjust on translate

        double shortestDistance = Double.MAX_VALUE;
        Point2D parentAnchorPoint = null;
        Point2D childAnchorPoint = null;

        for (Point2D parentPoint : parent.getAnchorPoints()){
            for (Point2D childPoint : child.getAnchorPoints()){
                double distance = parentPoint.distance(childPoint);
                if (distance < shortestDistance){
                    shortestDistance = distance;
                    parentAnchorPoint = parentPoint;
                    childAnchorPoint = childPoint;
                }
            }
        }

        path.setStartX(parentAnchorPoint.getX());
        path.setStartY(parentAnchorPoint.getY());

        path.setEndX(childAnchorPoint.getX());
        path.setEndY(childAnchorPoint.getY());
    }

    public void updateShapeTransform(){
        shape.setTranslateX(path.getEndX());
        shape.setTranslateY(path.getEndY());
        double opposite = path.getStartY() - path.getEndY();
        double adjacent = path.getEndX() - path.getStartX();
        double angle = Math.atan2(opposite, adjacent);
        angle = Math.toDegrees(angle);
        rzArrowHead.setAngle(arrowRotationOffset - angle);
    }

    public void setDashedLine(boolean state){
        if (state) {
            path.getStrokeDashArray().setAll(10.0, 10.0);
        }
        else {
            path.getStrokeDashArray().clear();
        }
    }

    public void setEmptyArrowShape(){
        double[] arrowShape = new double[] { 0,0,10,20,-10,20 };
        if (shape != null){
            getChildren().remove(shape);
        }

        shape = new Polygon(arrowShape);
        shape.setFill(Color.WHITE);
        shape.setStroke(Color.BLACK);
        shape.getTransforms().add(rzArrowHead);
        getChildren().addAll(shape);
        updateShapeTransform();
    }

    public void setEmptyDiamondShape(){
        double[] arrowShape = new double[] { 0, 0, -10, 15, 0, 30, 10, 15};
        if (shape != null){
            getChildren().remove(shape);
        }

        shape = new Polygon(arrowShape);
        shape.setFill(Color.WHITE);
        shape.setStroke(Color.BLACK);
        shape.getTransforms().add(rzArrowHead);
        getChildren().addAll(shape);
        updateShapeTransform();
    }

    public void setFullDiamondShape(){
        double[] arrowShape = new double[] { 0, 0, -10, 15, 0, 30, 10, 15};
        if (shape != null){
            getChildren().remove(shape);
        }

        shape = new Polygon(arrowShape);
        shape.setFill(Color.BLACK);
        shape.setStroke(Color.BLACK);
        shape.getTransforms().add(rzArrowHead);
        getChildren().addAll(shape);
        updateShapeTransform();
    }

    public void setArrowShape(){
        double[] arrowShape = new double[] { 0, 0, -10, 20, 0, 1, 10, 20};
        if (shape != null){
            getChildren().remove(shape);
        }

        shape = new Polygon(arrowShape);
        shape.setFill(Color.BLACK);
        shape.setStroke(Color.BLACK);
        shape.getTransforms().add(rzArrowHead);
        getChildren().addAll(shape);
        updateShapeTransform();
    }

    public boolean dependsOn(UMLClassBox b){
        return (parent.equals(b) || child.equals(b));
    }

}
