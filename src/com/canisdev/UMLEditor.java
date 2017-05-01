package com.canisdev;

import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * A class that assembles and connects the various
 * GUI components into an application.
 */
public class UMLEditor extends BorderPane {
    private EditorMenu editorMenu;
    private UMLArea umlArea;
    private ButtonTray buttonTray;

    /**
     * Creates a UMLEditor.
     */
    public UMLEditor(Stage stage) {
        super();

        umlArea = new UMLArea();
        editorMenu = new EditorMenu(stage, umlArea);
        buttonTray = new ButtonTray(umlArea);

        setCenter(umlArea);
        setTop(editorMenu);
        setBottom(buttonTray);
    }

    public UMLArea getUmlArea() {
        return umlArea;
    }
}