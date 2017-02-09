package com.canisdev;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.WindowEvent;

/**
 * Created by steve on 2/8/2017.
 */

public class EditorMenu extends MenuBar {

    private String style1url = Main.class.getResource("Style1.css").toExternalForm();
    private String style2url = Main.class.getResource("Style2.css").toExternalForm();

    public EditorMenu (){
        super();
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

        exit.setOnAction((actionEvent) -> {
            getScene().getWindow().fireEvent(new WindowEvent(getScene().getWindow(), WindowEvent.WINDOW_CLOSE_REQUEST));
        });

        /* END OF FILE MENU*/

        /*EDIT MENU*/
        Menu menuEdit = new Menu("Edit");
        /*END OF EDIT MENU*/

        /*VIEW MENU*/

        //todo: bug? changing style affects part of UMLclassbox layout

        Menu menuView = new Menu("View");
        Menu styleOptions = new Menu("Themes");
        MenuItem style1 = new MenuItem("Default");
        style1.setOnAction((e) -> {
            getScene().getStylesheets().removeAll(style1url, style2url);
            getScene().getStylesheets().add(style1url);
        });

        MenuItem style2 = new MenuItem("Test Style");
        style2.setOnAction((e) -> {
            getScene().getStylesheets().removeAll(style1url, style2url);
            getScene().getStylesheets().add(style2url);
        });

        styleOptions.getItems().addAll(style1, style2);
        menuView.getItems().addAll(styleOptions);
        /*END OF VIEW MENU*/

        getMenus().addAll(fileButton, menuEdit, menuView);
        setPrefWidth(Double.MAX_VALUE);
    }

}
