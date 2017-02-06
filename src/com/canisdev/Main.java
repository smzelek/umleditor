package com.canisdev;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

    private String style1url = Main.class.getResource("Style1.css").toExternalForm();
    private String style2url = Main.class.getResource("Style2.css").toExternalForm();


    @Override
    public void start(Stage primaryStage){
        primaryStage.setTitle("CANIS UML Editor 1.0");

        Image newFileIcon = new Image(getClass().getResourceAsStream("newfileicon.png"));
        ImageView newFileView = new ImageView(newFileIcon);

        Image openFileIcon = new Image(getClass().getResourceAsStream("openicon.png"));
        ImageView openFileView = new ImageView(openFileIcon);

        Image saveIcon = new Image(getClass().getResourceAsStream("saveicon.png"));
        ImageView saveView = new ImageView(saveIcon);

        Image settingsIcon = new Image(getClass().getResourceAsStream("settingsicon.png"));
        ImageView settingsView = new ImageView(settingsIcon);

        Image exitIcon = new Image(getClass().getResourceAsStream("exiticon.png"));
        ImageView exitView = new ImageView(exitIcon);

        newFileView.setFitHeight(15);
        newFileView.setFitWidth(15);

        VBox vBox = new VBox();
        Scene scene = new Scene (vBox, 400,450);

        MenuBar menuBar = new MenuBar();

        /* FILE MENU */
        Menu fileButton = new Menu("File");
        //new, open, save, settings, exit
        MenuItem newFile = new MenuItem("New");
        newFile.setGraphic(newFileView);
        fileButton.getItems().addAll(newFile);

        MenuItem openFile = new MenuItem("Open...");
        openFile.setGraphic(openFileView);
        fileButton.getItems().addAll(openFile);

        MenuItem saveFile = new MenuItem("Save");
        saveFile.setGraphic(saveView);
        fileButton.getItems().addAll(saveFile);

        MenuItem settings = new MenuItem("Settings");
        settings.setGraphic(settingsView);
        fileButton.getItems().addAll(settings);

        MenuItem exit = new MenuItem("Exit");
        exit.setGraphic(exitView);
        fileButton.getItems().addAll(exit);
        /* END OF FILE MENU*/

        /*EDIT MENU*/
        Menu menuEdit = new Menu("Edit");
        /*END OF EDIT MENU*/

        /*VIEW MENU*/
        Menu menuView = new Menu("View");
        Menu styleOptions = new Menu("Themes");
        MenuItem style1 = new MenuItem("Default");
        style1.setOnAction((e) -> {
            scene.getStylesheets().removeAll(style1url, style2url);
            scene.getStylesheets().add(style1url);
        });

        MenuItem style2 = new MenuItem("Test Style");
        style2.setOnAction((e) -> {
            scene.getStylesheets().removeAll(style1url, style2url);
            scene.getStylesheets().add(style2url);
        });

        styleOptions.getItems().addAll(style1, style2);
        menuView.getItems().addAll(styleOptions);
        /*END OF VIEW MENU*/


        menuBar.getMenus().addAll(fileButton, menuEdit, menuView);
        vBox.getChildren().addAll(menuBar);

        primaryStage.setScene(scene);
        //primaryStage.setResizable(false);
        scene.getStylesheets().add(style1url);

        primaryStage.show();
    }
    public static void main(String[] args) {
	    // write your code here
        launch(args);
    }
}
