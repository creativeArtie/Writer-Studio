package com.creativeartie.humming.ui;

import java.io.*;

import com.creativeartie.humming.main.*;

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
    public void initialize() throws IOException {
        Tab tab = new Tab("Document", FXMLLoader.load(DataFiles.WRITER.getFile()));
        documentTabs.getTabs().add(tab);
        topSplit.setDividerPosition(0, .3);
        centerSplit.setDividerPosition(0, .3);
    }
}
