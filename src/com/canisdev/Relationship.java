//todo: "child" tracks mouse until 2nd parent selected
//if child is still null, track mouse
//otherwise update parent/child anchors based on closeness
//this class can't get move events of the mouse in the general uml area...

package com.canisdev;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;

public class Relationship extends Group {

    private UMLClassBox parent;
    private UMLClassBox child;
    private Line path;
    private Polygon shape;
    private Rotate rzArrowHead;
    private double arrowRotationOffset = 90;

    public Relationship(UMLClassBox parent, UMLClassBox child){
        super();

        this.parent = parent;
        this.child = child;

        //Bidirectional data relationship.
        //Endpoints also have a pointer to the relationship.
        //Used for deletion of an endpoint.
        //Used for movement events.
        parent.addDependentRelationship(this);
        child.addDependentRelationship(this);

        path = new Line();
        path.setStrokeWidth(2);

        rzArrowHead = new Rotate();
        rzArrowHead.setAxis(Rotate.Z_AXIS);

        findClosestAnchorPair();

        getChildren().addAll(path);

        //Anchor Events are fired whenever this relationship's
        //parent or child moves. This ensures the relationship properly
        //maintains the link between them.
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

    //Generates all possible pairings of anchor points between
    //the child and the parent UMLClassBox.
    //Saves the pair with the shortest distance between them.
    //Sets this pair as the new endpoints for the line.
    private void findClosestAnchorPair(){
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

    //Ensures that the arrowhead for this relationship
    //is in the correct location and properly rotated.
    public void updateShapeTransform(){
        shape.setTranslateX(path.getEndX());
        shape.setTranslateY(path.getEndY());
        double opposite = path.getStartY() - path.getEndY();
        double adjacent = path.getEndX() - path.getStartX();
        double angle = Math.atan2(opposite, adjacent);
        angle = Math.toDegrees(angle);
        rzArrowHead.setAngle(arrowRotationOffset - angle);
    }

    //Toggles dashed line setting.
    public void setDashedLine(boolean state){
        if (state) {
            path.getStrokeDashArray().setAll(10.0, 10.0);
        }
        else {
            path.getStrokeDashArray().clear();
        }
    }

    //Possible arrowhead options:
    //************************

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
