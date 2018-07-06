package com.creativeartie.writerstudio.javafx;

import java.util.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.*;
import com.creativeartie.writerstudio.resource.*;

/**
 * Stores a list of user notes, hyperlinks.
 */
public abstract class NoteCardView extends GridPane{
    /// %Part 1: Constructor and Class Fields

    private TableView<NoteCardData> noteTable;
    private NoteCardDetailControl noteDetail;

    public NoteCardView(){

        add(buildNoteTable(), 0, 0);
        add(buildNoteDetail(), 1, 0);
    }

    /// %Part 2: Layout

    @SuppressWarnings("unchecked") /// For ans.getColumns().addAdd(TableColumn ...)
    private TableView<NoteCardData> buildNoteTable(){
        noteTable = new TableView<>();

        TableColumn<NoteCardData, Optional<CatalogueIdentity>> id =
            TableDataHelper.getIdColumn(WindowText.NOTE_CARDS_ID, d ->
                d.noteIdProperty());
        TableDataHelper.setPrecentWidth(id, noteTable, 20.0);

        TableColumn<NoteCardData, SectionSpan> section =
            TableDataHelper.getSectionColumn(WindowText.NOTE_CARDS_SECTION, d ->
                d.noteSectionProperty());
        TableDataHelper.setPrecentWidth(section, noteTable, 40.0);

        TableColumn<NoteCardData, Optional<FormattedSpan>> format =
            TableDataHelper.getFormatColumn(WindowText.NOTE_CARDS_TITLE, d ->
                d.noteTitleProperty());
        TableDataHelper.setPrecentWidth(format, noteTable, 40.0);

        noteTable.getColumns().addAll(id, section, format);
        noteTable.setFixedCellSize(30);
        noteTable.setPlaceholder(new Label(WindowText.NOTE_CARDS_EMPTY.getText()));
        noteTable.prefWidthProperty().bind(widthProperty().multiply(.7));
        return noteTable;
    }

    private NoteCardDetailControl buildNoteDetail(){
        noteDetail = new NoteCardDetailControl();
        noteDetail.prefWidthProperty().bind(widthProperty().multiply(.3));
        noteDetail.prefHeightProperty().bind(heightProperty());
        return noteDetail;
    }

    /// %Part 3: Listener Methods

    public void setupProperties(WriterSceneControl control){
        setupChildern(control);
    }

    protected abstract void setupChildern(WriterSceneControl control);

    /// %Part 4: Properties

    /// %Part 5: Get Child Methods

    TableView<NoteCardData> getNoteTable(){
        return noteTable;
    }

    NoteCardDetailControl getNoteCardDetail(){
        return noteDetail;
    }
}
