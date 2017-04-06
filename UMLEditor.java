package com.canisdev;

import javafx.scene.layout.BorderPane;

/**
 * A class that assembles and connects the various
 * GUI components into an application.
 */
public class UMLEditor extends BorderPane {
    public EditorMenu editorMenu;
    public UMLArea umlArea;
    public ButtonTray buttonTray;

    /**
     * Creates a UMLEditor.
     */
    public UMLEditor() {
        super();
        editorMenu = new EditorMenu();
        umlArea = new UMLArea();
        buttonTray = new ButtonTray(umlArea);

        setCenter(umlArea);
        setTop(editorMenu);
        setBottom(buttonTray);
    }
}