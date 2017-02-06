package com.canisdev;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage){
        primaryStage.setTitle("CANIS UML Editor 1.0");

        Image newFileIcon = new Image(getClass().getResourceAsStream("newfileicon.png"));
        ImageView newFileView = new ImageView(newFileIcon);
        newFileView.setFitHeight(15);
        newFileView.setFitWidth(15);

        VBox vBox = new VBox();

        MenuBar menuBar = new MenuBar();

        Menu fileButton = new Menu("File");
        //new, open, save, settings, exit
        MenuItem newFile = new MenuItem("New");

        newFile.setGraphic(newFileView);
        fileButton.getItems().addAll(newFile);

        Menu menuEdit = new Menu("Edit");
        Menu menuView = new Menu("View");

        menuBar.getMenus().addAll(fileButton, menuEdit, menuView);
        vBox.getChildren().addAll(menuBar);

        Scene scene = new Scene (vBox, 400,450);

        primaryStage.setScene(scene);
        //primaryStage.setResizable(false);
        scene.getStylesheets().add(Main.class.getResource("Style1.css").toExternalForm());
        primaryStage.show();
    }
    public static void main(String[] args) {
	    // write your code here
        launch(args);
    }
}
