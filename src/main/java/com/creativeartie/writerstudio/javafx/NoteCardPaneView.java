package com.creativeartie.writerstudio.javafx;

import java.util.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;

import com.creativeartie.writerstudio.lang.markup.*;
import com.creativeartie.writerstudio.resource.*;

/**
 * A pane with two {@link HeadingTreeControl} objects that
 */
abstract class NoteCardPaneView extends GridPane{

    private class NoteCell extends ListCell<NoteCardSpan>{
        @Override
        public void updateItem(NoteCardSpan item, boolean empty){
            /// Required by JavaFX API:
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                /// Allows TextFlowBuilder to create the Label
                TextFlow graphic = TextFlowBuilder.loadFormatText(item
                    .getTitle());
                setText(null);
                setGraphic(graphic);
            }
        }
    }

    private TreeView<String> idTree;
    private TreeView<String> locationTree;

    private Button insertBeforeButton;
    private Button insertAfterButton;
    private Button deleteButton;
    private ListView<NoteCardSpan> noteCardsList;

    private NoteCardDetailPaneControl noteDetailPane;

    /// %Part 1: Constructor and Class Fields
    public NoteCardPaneView(){
        double width = 100.0 / 3;
        ColumnConstraints columns[] = new ColumnConstraints[3];
        for (int i = 0; i < columns.length; i++){
            columns[i] = new ColumnConstraints();
            columns[i].setPercentWidth(width);
        }
        getColumnConstraints().addAll(columns);
        add(buildLocationPane(), 0, 0);
        add(buildNoteListPane(), 1, 0);
        add(buildDetailPane(), 2, 0);
    }

    /// %Part 2: Layout
    private TabPane buildLocationPane(){
        idTree = new TreeView<>();
        idTree.setShowRoot(false);
        Tab id = new Tab("Id List", idTree);

        locationTree = new TreeView<>();
        locationTree.setShowRoot(false);
        Tab location = new Tab("Location List", locationTree);

        TabPane pane = new TabPane(id, location);
        pane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        return pane;
    }

    private BorderPane buildNoteListPane(){
        BorderPane pane = new BorderPane();

        noteCardsList = new ListView<>();
        noteCardsList.setPlaceholder(new Label("Select a id or location"));
        noteCardsList.setCellFactory(l ->  new NoteCell());
        pane.setCenter(noteCardsList);

        insertAfterButton = new Button("Insert After");
        insertBeforeButton = new Button("Insert Before");
        deleteButton = new Button("Delete");
        pane.setBottom(new FlowPane(insertAfterButton, insertBeforeButton,
            deleteButton));
        return pane;
    }

    private NoteCardDetailPaneControl buildDetailPane(){
        noteDetailPane = new NoteCardDetailPaneControl();
        return noteDetailPane;
    }


    /// %Part 3: Setup Properties

    public void setupProperties(WriterSceneControl control){
        setupChildern(control);
    }

    protected abstract void setupChildern(WriterSceneControl control);

    /// %Part 4: Properties

    /// %Part 5: Get Child Methods

    TreeView<String> getIdTree(){
        return idTree;
    }

    TreeView<String> getLoactionTree(){
        return locationTree;
    }

    ListView<NoteCardSpan> getNoteCardList(){
        return noteCardsList;
    }

    NoteCardDetailPaneControl getNoteDetailPane(){
        return noteDetailPane;
    }
}
