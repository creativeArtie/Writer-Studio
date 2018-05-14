package com.creativeartie.writerstudio.window;

import java.util.*;
import java.util.Optional;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.scene.layout.*;
import javafx.beans.property.*;
import javafx.collections.*;
import javafx.scene.control.cell.*;

import com.creativeartie.writerstudio.main.*;
import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.*;
import com.creativeartie.writerstudio.resource.*;

import com.google.common.base.*;
import com.google.common.collect.*;

/**
 * Stores a list of user notes, hyperlinks.
 */
public abstract class NoteCardView extends GridPane{
    private TableView<NoteCardData> noteTable;
    private NoteCardDetail noteDetail;
    private ReadOnlyIntegerWrapper locationChoosen;

    public NoteCardView(){
        noteTable = initNoteTable();
        noteDetail = initNoteDetail();

        noteTable.getSelectionModel().selectedItemProperty().addListener(
            (data, oldValue, newValue) -> noteSelected(Optional
                .ofNullable(newValue)
                .map(span -> span.getTargetSpan())
                .orElse(null)
            )
        );

        locationChoosen = new ReadOnlyIntegerWrapper(this, "locationChoosen");
        locationChoosen.bind(noteDetail.locationChoosenProperty());
    }

    public ReadOnlyIntegerProperty locationChoosenProperty(){
        return locationChoosen.getReadOnlyProperty();
    }

    public int getLocationChoose(){
        return locationChoosen.getValue();
    }

    @SuppressWarnings("unchecked") /// For ans.getColumns().addAdd(TableColumn ...)
    private TableView<NoteCardData> initNoteTable(){
        TableView<NoteCardData> ans = new TableView<>();

        TableColumn<NoteCardData, Optional<CatalogueIdentity>> id =
            TableViewHelper.getIdColumn(WindowText.NOTE_CARDS_ID, d ->
                d.noteIdProperty());
        TableViewHelper.setPrecentWidth(id, ans, 20.0);

        TableColumn<NoteCardData, SectionSpan> section =
            TableViewHelper.getSectionColumn(WindowText.NOTE_CARDS_SECTION, d ->
                d.noteSectionProperty());
        TableViewHelper.setPrecentWidth(section, ans, 40.0);

        TableColumn<NoteCardData, Optional<FormattedSpan>> format =
            TableViewHelper.getFormatColumn(WindowText.NOTE_CARDS_TITLE, d ->
                d.noteTitleProperty());
        TableViewHelper.setPrecentWidth(format, ans, 40.0);

        ans.getColumns().addAll(id, section, format);
        ans.setFixedCellSize(30);
        ans.setPlaceholder(new Label(WindowText.NOTE_CARDS_EMPTY.getText()));
        ans.prefWidthProperty().bind(widthProperty().multiply(.7));
        add(ans, 0, 0);
        return ans;
    }

    private NoteCardDetail initNoteDetail(){
        NoteCardDetail ans = new NoteCardDetail();
        add(ans, 1, 0);
        ans.prefWidthProperty().bind(widthProperty().multiply(.3));
        ans.prefHeightProperty().bind(heightProperty());
        return ans;
    }

    TableView<NoteCardData> getNoteTable(){
        return noteTable;
    }

    NoteCardDetail getNoteCardDetail(){
        return noteDetail;
    }

    protected abstract void noteSelected(NoteCardSpan span);
}