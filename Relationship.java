//todo: "child" tracks mouse until 2nd parent selected
//if destination is still null, track mouse
//otherwise update source/destination anchors based on closeness
//this class can't get move events of the mouse in the general uml area...

package com.canisdev;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;

/**
 * Bidirectional data relationship with a GUI component.
 * The endpoints of a relationship hold a pointer
 * to the relationship, which is used to delete the relationship
 * when that endpoint is deleted.
 *
 * The relationship also handles movement events of its endpoints
 * with the pointers.
 */
public class Relationship extends Group {

    public enum RelationshipType {
        Aggregation, Association, Composition, Dependency, Implementation, Inheritance
    }
    private UMLClassBox source;
    private UMLClassBox destination;
    private Line path;
    private Polygon shape;
    private Rotate rzArrowHead;
    private double arrowRotationOffset = 90;

    /**
     * Creates a Relationship with references to both UMLClassBoxes
     * it is attached to.
     *
     * @param source The UMLClassBox source of a Relationship.
     * @param destination The UMLClassBox destination of a Relationship.
     */
    public Relationship(UMLClassBox source, UMLClassBox destination, RelationshipType relationshipType){
        super();

        this.source = source;
        this.destination = destination;

        source.addDependentRelationship(this);
        destination.addDependentRelationship(this);

        path = new Line();
        path.setStrokeWidth(2);
        getChildren().addAll(path);

        rzArrowHead = new Rotate();
        rzArrowHead.setAxis(Rotate.Z_AXIS);

        switch (relationshipType){
            case Aggregation:
                setEmptyDiamondShape();
                break;
            case Association:
                setArrowShape();
                break;
            case Composition:
                setFullDiamondShape();
                break;
            case Dependency:
                setArrowShape();
                setDashedLine(true);
                break;
            case Implementation:
                setEmptyArrowShape();
                setDashedLine(true);
                break;
            case Inheritance:
                setEmptyArrowShape();
                break;
        }

        setEventHandler(AnchorEvent.MOVED, this::handleMove);
        findClosestAnchorPair();
        updateShapeTransform();
    }

    /**
     * Determines whether or not this Relationship has a given
     * UMLClassBox as either its source or its destination.
     *
     * @param b The UMLClassBox to check if this Relationship has as
     *          an endpoint.
     * @return  Whether or not this Relationship has the UMLClassBox as
     *          an endpoint.
     */
    public boolean dependsOn (UMLClassBox b){
        return (source.equals(b) || destination.equals(b));
    }

    /**
     * Handles anchor moved events sent by this Relationship's source
     * or destination by appropriately updating the attachment points
     * for this Relationship on the source and destination UMLClassBoxes.
     *
     * @param anchorEvent An anchor moved event fired by a UMLClassBox
     *                    translating or resizing.
     */
    private void handleMove(AnchorEvent anchorEvent)
    {
        findClosestAnchorPair();
        updateShapeTransform();
        anchorEvent.consume();
    }

    /**
     * Generates all possible pairings of anchor points between
     * the source and destination UMLClassBoxes, and uses the
     * shortest pair as the new attachment points for this
     * Relationship.
     */
    private void findClosestAnchorPair(){
        double shortestDistance = Double.MAX_VALUE;
        Point2D sourceAnchorPoint = null;
        Point2D destinationAnchorPoint = null;

        for (Point2D sourcePoint : source.getAnchorPoints()){
            for (Point2D destinationPoint : destination.getAnchorPoints()){
                double distance = sourcePoint.distance(destinationPoint);
                if (distance < shortestDistance){
                    shortestDistance = distance;
                    sourceAnchorPoint = sourcePoint;
                    destinationAnchorPoint = destinationPoint;
                }
            }
        }

        path.setStartX(sourceAnchorPoint.getX());
        path.setStartY(sourceAnchorPoint.getY());

        path.setEndX(destinationAnchorPoint.getX());
        path.setEndY(destinationAnchorPoint.getY());
    }

    /**
     * Updates the arrowhead for this Relationship to ensure
     * that it is at the destination endpoint and that it is
     * properly rotated to align with the line for this
     * Relationship.
     */
    private void updateShapeTransform(){
        shape.setTranslateX(path.getEndX());
        shape.setTranslateY(path.getEndY());
        double opposite = path.getStartY() - path.getEndY();
        double adjacent = path.getEndX() - path.getStartX();
        double angle = Math.atan2(opposite, adjacent);
        angle = Math.toDegrees(angle);
        rzArrowHead.setAngle(arrowRotationOffset - angle);
    }

    /**
     * Toggles whether or not the line for this Relationship
     * is dashed or solid.
     *
     * @param state Whether the line should be dashed.
     */
    private void setDashedLine(boolean state){
        if (state) {
            path.getStrokeDashArray().setAll(10.0, 10.0);
        }
        else {
            path.getStrokeDashArray().clear();
        }
    }

    /**
     * Customizes this Relationship by changing the arrowhead to
     * use an empty arrow shape.
     */
    private void setEmptyArrowShape(){
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

    /**
     * Customizes this Relationship by changing the arrowhead to
     * use an empty diamond shape.
     */
    private void setEmptyDiamondShape(){
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

    /**
     * Customizes this Relationship by changing the arrowhead to
     * use a full diamond shape.
     */
    private void setFullDiamondShape(){
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

    /**
     * Customizes this Relationship by changing the arrowhead to use
     * an unsealed arrow shape.
     */
    private void setArrowShape(){
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
}
