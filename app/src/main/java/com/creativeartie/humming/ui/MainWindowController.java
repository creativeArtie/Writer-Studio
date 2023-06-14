package com.creativeartie.humming.ui;

import java.io.*;

import com.creativeartie.humming.files.*;
import com.creativeartie.humming.main.*;

import javafx.collections.*;
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
    private ListView<ManuscriptFile> textFiles;
    @FXML
    private ListView<File> imageFiles;

    @FXML
    void initialize() throws IOException {
        topSplit.setDividerPosition(0, .3);
        centerSplit.setDividerPosition(0, .3);
        createDefaults();
        textFiles.setCellFactory((file) -> {
            ListCell<ManuscriptFile> result = new ListCell<>() {
                public void updateItem(ManuscriptFile file, boolean empty) {
                    if (empty) {
                        setText(null);
                        setGraphic(null);
                    } else if (file != null) {
                        setText(null);
                        setGraphic(createTextFileLabel(file));
                    } else {
                        setText(new String());
                        setGraphic(null);
                    }
                }
            };
            return result;
        });

        loadFiles();
    }

    private GridPane createTextFileLabel(ManuscriptFile file) {
        GridPane pane = new GridPane();
        ColumnConstraints col1 = new ColumnConstraints(), col2 = new ColumnConstraints(),
                col3 = new ColumnConstraints();
        col1.setPercentWidth(33);
        col2.setPercentWidth(33);
        col3.setPercentWidth(33);
        pane.getColumnConstraints().addAll(col1, col2, col3);

        Label label = new Label(file.getDraftName());
        label.getStyleClass().add(CssStyles.FILE_HEADING.toString());
        pane.add(label, 0, 0, 3, 1);
        GridPane.setFillWidth(label, true);

        String fileDetailClass = CssStyles.FILE_DETIAL.toString();

        label = new Label(UIText.Files.DRAFT_NUMBER.getText() + Integer.toString(file.getDraftNumber()));
        label.getStyleClass().add(fileDetailClass);
        pane.add(label, 0, 1);

        label = new Label(
                UIText.Files.DRAFT_COUNT.getText() + Integer.toString(file.getManuscript().getWritingCount())
        );
        label.getStyleClass().add(fileDetailClass);
        pane.add(label, 1, 1);

        label = new Label(
                UIText.Files.OUTLINE_COUNT.getText() + Integer.toString(file.getManuscript().getOutlineCount())
        );
        label.getStyleClass().add(fileDetailClass);
        pane.add(label, 2, 1);
        return pane;
    }

    private void createDefaults() throws IOException {
        ProjectZip project = ProjectZip.INSTANCE.newProject();
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

    private void loadFiles() {
        ObservableList<ManuscriptFile> textItems = textFiles.getItems();
        textItems.clear();
        for (ManuscriptFile text : ProjectZip.INSTANCE.getManuscriptList()) {
            textItems.add(text);
        }
    }
}
