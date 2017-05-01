package com.canisdev;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A menu that spans the top of the application, allowing
 * manipulation of general settings and file manipulation
 * via drop down menus.
 */
public class EditorMenu extends MenuBar {

    private Stage stage;
    private String style1url = Main.class.getResource("Style1.css").toExternalForm();
    private String style2url = Main.class.getResource("Style2.css").toExternalForm();
    private ArrayList<String> styleOptions;
    private UMLArea umlArea;

    /**
     * Creates an EditorMenu.
     */
    public EditorMenu (Stage stage, UMLArea umlArea){
        super();

        this.umlArea = umlArea;
        this.stage = stage;

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

        Image printerIcon = new Image(getClass().getResourceAsStream("img/printericon.png"));
        ImageView printerView = new ImageView(printerIcon);

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
        newFile.setOnAction(e -> {
            Platform.runLater(() -> new Main().start(new Stage()));
        });

        MenuItem openFile = new MenuItem("Open...");
        openFile.setGraphic(openFileView);
        fileButton.getItems().addAll(openFile);
        openFile.setOnAction(e -> {
            openFileDialog();
        });

        MenuItem saveFile = new MenuItem("Save");
        saveFile.setGraphic(saveView);
        fileButton.getItems().addAll(saveFile);

        saveFile.setOnAction(e -> {
            saveFileDialog();
        });

        MenuItem printFile = new MenuItem("Print");
        printFile.setGraphic(printerView);
        fileButton.getItems().addAll(printFile);

        MenuItem settings = new MenuItem("Settings");
        settings.setGraphic(settingsView);

        MenuItem exit = new MenuItem("Exit");
        exit.setGraphic(exitView);
        fileButton.getItems().addAll(exit);

        printFile.setOnAction((actionEvent) -> {
            Node node = ((BorderPane)(getScene().getRoot())).getCenter();
            Printer printer = Printer.getDefaultPrinter();
            // allow for printing on standard letter paper
            PageLayout pageLayout = printer.createPageLayout(Paper.NA_LETTER, PageOrientation.PORTRAIT, Printer.MarginType.DEFAULT);

            // create scale
            double scaleX = pageLayout.getPrintableWidth() / node.getBoundsInParent().getWidth();
            double scaleY = pageLayout.getPrintableHeight() / node.getBoundsInParent().getHeight();
            Scale s = new Scale(scaleX, scaleY);

            // scale umlArea for printing
            node.getTransforms().add(s);

            // print based on user interaction with printer dialog box
            PrinterJob job = PrinterJob.createPrinterJob();
            boolean successPrintDialog = job.showPrintDialog(getScene().getWindow());
            if (job != null) {
                boolean success = job.printPage(node);
                if (success) {
                    job.endJob();
                }
            }

            // remove printing scale
            node.getTransforms().remove(s);

        });

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


        getMenus().addAll(fileButton, menuView);
    }

    public void openFileDialog () {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("UML Files", "*.uml"));

        try {
            File openedFile = fileChooser.showOpenDialog(stage);

            if (openedFile == null){
                return;
            }
            FileInputStream file = new FileInputStream(openedFile);

            UMLArea umlArea = ((UMLEditor) this.getParent()).getUmlArea();
            ObjectInputStream restore = new ObjectInputStream(file);
            umlArea.clear();

            HashMap<Integer, UMLClassBox> boxLookup = new HashMap<>();

            int numBoxes = restore.readInt();

            for (int i = 0; i < numBoxes; ++i){
                UMLClassBox box = umlArea.newBox(0, 0);
                boxLookup.put(restore.readInt(), box);
                box.setPrefWidth(restore.readDouble());
                box.setPrefHeight(restore.readDouble());
                box.setTranslateX(restore.readDouble());
                box.setTranslateY(restore.readDouble());
                box.setNameText((String) restore.readObject());
                box.setAttributeText((String) restore.readObject());
                box.setMethodText((String) restore.readObject());
            }

            int numRelationships = restore.readInt();

            for (int i = 0; i < numRelationships; ++i){
                Relationship.RelationshipType lineType = (Relationship.RelationshipType) restore.readObject();
                UMLClassBox source = boxLookup.get(restore.readInt());
                UMLClassBox destination = boxLookup.get(restore.readInt());
                Relationship r = umlArea.newLine(source, destination, lineType);
                r.setStart(restore.readDouble(), restore.readDouble());
                r.setEnd(restore.readDouble(), restore.readDouble());
                r.updateShapeTransform();
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveFileDialog() {
        ArrayList<UMLClassBox> allBoxes = ((UMLEditor) getParent()).getUmlArea().getAllBoxes();
        ArrayList<Relationship> allLines = ((UMLEditor) getParent()).getUmlArea().getAllLines();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName("untitled.uml");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("UML Files", "*.uml"));

        try {
            File file = fileChooser.showSaveDialog(stage);
            if (file == null){
                return;
            }
            String file_name = file.toString();
            if (!file_name.endsWith(".uml"))
                file_name += ".uml";
            file.renameTo(new File (file_name));

            FileOutputStream saveFile = new FileOutputStream(file);
            ObjectOutputStream saveStream = new ObjectOutputStream(saveFile);

            saveStream.writeInt(allBoxes.size());
            for (UMLClassBox b : allBoxes){
                saveStream.writeInt(b.getSaveID());
                saveStream.writeDouble(b.getWidth());
                saveStream.writeDouble(b.getHeight());
                saveStream.writeDouble(b.getTranslateX());
                saveStream.writeDouble(b.getTranslateY());
                saveStream.writeObject(b.getNameText());
                saveStream.writeObject(b.getAttributeText());
                saveStream.writeObject(b.getMethodText());
            }

            saveStream.writeInt(allLines.size());
            for (Relationship r : allLines) {
                saveStream.writeObject(r.getRelationshipType());
                saveStream.writeInt(r.getSource().getSaveID());
                saveStream.writeInt(r.getDestination().getSaveID());
                saveStream.writeDouble(r.getStartX());
                saveStream.writeDouble(r.getStartY());
                saveStream.writeDouble(r.getEndX());
                saveStream.writeDouble(r.getEndY());
            }

            saveStream.close();

        }catch (Exception e) {
            e.printStackTrace();
        }
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
