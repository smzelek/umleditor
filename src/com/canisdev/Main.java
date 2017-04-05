package com.canisdev;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;

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

    public static class UMLEditor extends BorderPane {
        public EditorMenu editorMenu;
        public UMLArea umlArea;
        public ButtonTray buttonTray;

        public UMLEditor() {
            super();
            editorMenu = new EditorMenu();
            //editorMenu.useStyle(0);
            umlArea = new UMLArea();
            buttonTray = new ButtonTray(umlArea);

            setCenter(umlArea);
            setTop(editorMenu);
            setBottom(buttonTray);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
