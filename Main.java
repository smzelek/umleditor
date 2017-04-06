package com.canisdev;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * Runnable class that launches the Application
 * and constructs a UMLEditor for the Application
 * to consist of.
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage){
        primaryStage.setTitle("CANIS UML Editor 1.0");
        Parent root = new UMLEditor();
        Scene scene = new Scene(root, 400,450);
        primaryStage.setResizable(true);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
