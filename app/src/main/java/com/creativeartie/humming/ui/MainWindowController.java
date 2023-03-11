package com.creativeartie.humming.ui;

import javafx.fxml.*;
import javafx.scene.control.*;

public class MainWindowController {
    @FXML
    public TabPane documentTabs;
    @FXML
    public SplitPane topSplit, centerSplit;
    @FXML
    private TreeView<String> tableOfContents;

    public MainWindowController() {}

    @FXML
    public void initialize() {
        Tab tab = new Tab("Document");
        tab.setContent(new WritingCoderPane());
        documentTabs.getTabs().add(tab);
        topSplit.setDividerPosition(0, .3);
        centerSplit.setDividerPosition(0, .3);
    }
}
