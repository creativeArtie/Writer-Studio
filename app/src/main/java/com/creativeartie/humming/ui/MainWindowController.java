package com.creativeartie.humming.ui;

import java.io.*;

import com.creativeartie.humming.main.*;

import javafx.fxml.*;
import javafx.scene.control.*;

/**
 * Controller for <a href="../../../../../resources/data/main.fxml"> main.fxml
 * </a>
 */
public class MainWindowController {
    @FXML
    private TabPane documentTabs;
    @FXML
    private SplitPane topSplit, centerSplit;
    @FXML
    private TreeView<String> tableOfContents;

    @FXML
    void initialize() throws IOException {
        Tab tab = new Tab(UIText.General.WRITER_TAB.getText(), FXMLLoader.load(DataFiles.WRITER.getFile()));
        documentTabs.getTabs().add(tab);
        topSplit.setDividerPosition(0, .3);
        centerSplit.setDividerPosition(0, .3);
    }
}
