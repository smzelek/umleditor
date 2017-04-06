package com.canisdev;

import javafx.application.Platform;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.WindowEvent;

import java.util.ArrayList;

/**
 * A menu that spans the top of the application, allowing
 * manipulation of general settings and file manipulation
 * via drop down menus.
 */
public class EditorMenu extends MenuBar {

    private String style1url = Main.class.getResource("Style1.css").toExternalForm();
    private String style2url = Main.class.getResource("Style2.css").toExternalForm();
    private ArrayList<String> styleOptions;

    /**
     * Creates an EditorMenu.
     */
    public EditorMenu (){
        super();

        styleOptions = new ArrayList<>();
        styleOptions.add(style1url);
        styleOptions.add(style2url);

        Platform.runLater(()-> useStyle(0));


        //Icons to be used for some menu items.
        Image newFileIcon = new Image(getClass().getResourceAsStream("img/newfileicon.png"));
        ImageView newFileView = new ImageView(newFileIcon);

        Image openFileIcon = new Image(getClass().getResourceAsStream("img/openicon.png"));
        ImageView openFileView = new ImageView(openFileIcon);

        Image saveIcon = new Image(getClass().getResourceAsStream("img/saveicon.png"));
        ImageView saveView = new ImageView(saveIcon);

        Image settingsIcon = new Image(getClass().getResourceAsStream("img/settingsicon.png"));
        ImageView settingsView = new ImageView(settingsIcon);

        Image exitIcon = new Image(getClass().getResourceAsStream("img/exiticon.png"));
        ImageView exitView = new ImageView(exitIcon);


        //**********************************************
        // FILE MENU
        Menu fileButton = new Menu("File");
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
        // END OF FILE MENU
        //*************************************************


        // EDIT MENU
        //*************************************************
        Menu menuEdit = new Menu("Edit");
        // END OF EDIT MENU
        //*************************************************


        //*************************************************
        // VIEW MENU
        Menu menuView = new Menu("View");
        Menu styleOptions = new Menu("Themes");
        MenuItem style0 = new MenuItem("Default");
        style0.setOnAction((e) -> {
            useStyle(0);
        });

        MenuItem style1 = new MenuItem("Test Style");
        style1.setOnAction((e) -> {
            useStyle(1);
        });

        styleOptions.getItems().addAll(style0, style1);
        menuView.getItems().addAll(styleOptions);
        // END OF VIEW MENU
        //**********************************************


        getMenus().addAll(fileButton, menuEdit, menuView);
    }

    /**
     * Changes the custom CSS used to style FXML elements of
     * the application by indicating which loaded stylesheet
     * should be used.
     *
     * @param styleNumber Index of the saved stylesheet to use.
     */
    public void useStyle (int styleNumber){
        getScene().getStylesheets().clear();
        getScene().getStylesheets().add(styleOptions.get(styleNumber));
    }
}
