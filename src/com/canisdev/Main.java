package com.canisdev;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Main extends Application {

    public EditorMenu editorMenu;
    public UMLArea umlArea;
    public ButtonTray buttonTray;

    @Override
    public void start(Stage primaryStage){
        primaryStage.setTitle("CANIS UML Editor 1.0");

        editorMenu = new EditorMenu();
        umlArea = new UMLArea();
        buttonTray = new ButtonTray(umlArea);

        BorderPane root = new BorderPane();
        root.setCenter(umlArea);
        root.setTop(editorMenu);
        root.setBottom(buttonTray);

        Scene scene = new Scene(root, 400,450);
        primaryStage.setMinWidth(400);
        primaryStage.setMinHeight(350);

        primaryStage.setResizable(true);
        primaryStage.setScene(scene);
        primaryStage.show();

        editorMenu.useStyle(0);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
