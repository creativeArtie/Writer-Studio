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

    private NoteCardTreeControl idTree;
    private NoteCardTreeControl locationTree;

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
        idTree = new NoteCardTreeControl();
        Tab id = new Tab("Id List");
        locationTree = new NoteCardTreeControl();
        Tab location = new Tab("Location List");

        TabPane pane = new TabPane(id, location);
        pane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        return pane;
    }

    private BorderPane buildNoteListPane(){
        BorderPane pane = new BorderPane();

        noteCardsList = new ListView<>();
        noteCardsList.setPlaceholder(new Label("Select a id or location"));
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

    NoteCardTreeControl getIdTree(){
        return idTree;
    }

    NoteCardTreeControl getLoactionTree(){
        return locationTree;
    }

    NoteCardDetailPaneControl getNoteDetailPane(){
        return noteDetailPane;
    }
}
