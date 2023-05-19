package com.creativeartie.humming.ui;

import java.io.*;

import com.creativeartie.humming.files.*;
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
        topSplit.setDividerPosition(0, .3);
        centerSplit.setDividerPosition(0, .3);
        createDefaults();
    }

    private void createDefaults() throws IOException {
        ProjectZip project = ProjectZip.newProject();
        ManuscriptFile active = project.createManuscript(UIText.DefaultNames.DRAFT.getText());
        project.createManuscript(UIText.DefaultNames.NOTE.getText());

        FXMLLoader fxmlLoader = new FXMLLoader(DataFiles.WRITER.getFile());
        Pane p = fxmlLoader.load();
        WritingController controller = fxmlLoader.<WritingController>getController();
        controller.setManuscript(active);

        Tab tab = new Tab(UIText.General.WRITER_TAB.getText(), p);
        ActiveFile.setActiveFile(controller);
        documentTabs.getTabs().add(tab);
    }
}
