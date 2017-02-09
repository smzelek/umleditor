package com.canisdev;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {

    private Scene scene;

    //TODO: when I add the arrows
    //there will be a canvas with the arrow images
    //and nodes on an overlay that correspond to the arrows, etc
    //this will all be in the UMLArea

    @Override
    public void start(Stage primaryStage){
        primaryStage.setTitle("CANIS UML Editor 1.0");

        EditorMenu topMenu = new EditorMenu();
        UMLArea umlArea = new UMLArea();

        GridPane palette = new GridPane();
        palette.setPrefWidth(Double.MAX_VALUE);
        palette.setGridLinesVisible(true);
        ColumnConstraints cc = new ColumnConstraints();
        cc.setPercentWidth(100);
        palette.getColumnConstraints().add(cc);

        Button btn = new Button("Add Class Box");
        btn.setPrefWidth(Double.MAX_VALUE);
        btn.setMaxWidth(100);

        btn.setOnAction((actionEvent) -> {
            umlArea.newBox();
        });

        palette.add(btn, 0, 0);

        BorderPane root = new BorderPane();
        root.setCenter(umlArea);
        root.setTop(topMenu);
        root.setBottom(palette);
        scene = new Scene(root, 400,450);

        primaryStage.setResizable(true);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Canis UML Editor 0.1b");
        primaryStage.show();
    }

    public static void main(String[] args) {
	    launch(args);
    }
}
