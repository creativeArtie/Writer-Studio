package com.creativeartie.humming.ui;

import java.io.*;

import com.creativeartie.humming.main.*;

import javafx.fxml.*;
import javafx.scene.control.*;

public class MainWindowController {
    @FXML
    private TabPane documentTabs;
    @FXML
    private SplitPane topSplit, centerSplit;
    @FXML
    private TreeView<String> tableOfContents;

    @FXML
    void initialize() throws IOException {
        Tab tab = new Tab("Document", FXMLLoader.load(DataFiles.WRITER.getFile()));
        documentTabs.getTabs().add(tab);
        topSplit.setDividerPosition(0, .3);
        centerSplit.setDividerPosition(0, .3);
    }
}
