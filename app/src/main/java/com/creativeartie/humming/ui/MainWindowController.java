package com.creativeartie.humming.ui;

import java.io.*;

import com.creativeartie.humming.main.*;

import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

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
        FXMLLoader fxmlLoader = new FXMLLoader();
        Pane p = fxmlLoader.load(DataFiles.WRITER.getFile().openStream());
        ActiveFile controller = (ActiveFile) fxmlLoader.getController();

        Tab tab = new Tab(UIText.General.WRITER_TAB.getText(), p);
        ActiveFile.setActiveFile(controller);
        documentTabs.getTabs().add(tab);
        topSplit.setDividerPosition(0, .3);
        centerSplit.setDividerPosition(0, .3);
    }
}
