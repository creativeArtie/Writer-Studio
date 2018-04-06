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
 * Stores a list hyperlinks.
 */
public class TableNotePane extends TableDataControl<TableNoteData>{
    private DirectoryType targetType;

    public TableNotePane(DirectoryType type){
        super(type == DirectoryType.FOOTNOTE? WindowText.FOOTNOTE_EMPTY:
            WindowText.ENDNOTE_EMPTY);
        targetType = type;
    }

    @Override
    @SuppressWarnings("unchecked") /// For getColumns().addAdd(TableColumn ...)
    protected void initColumns(){
        TableColumn<TableNoteData, Number> location = TableViewHelper
            .getNumberColumn(WindowText.NOTES_LOCATION, d ->
                d.noteLocationProperty());
        TableViewHelper.setPrecentWidth(location, this, 10.0);

        TableColumn<TableNoteData, Optional<CatalogueIdentity>> id =
            TableViewHelper.getIdColumn(WindowText.NOTES_ID, d ->
                d.noteIdProperty());
        TableViewHelper.setPrecentWidth(id, this, 20.0);

        TableColumn<TableNoteData, String> lookup =
            TableViewHelper.getTextColumn(WindowText.NOTES_LOOKUP, d ->
                d.noteLookupTextProperty(), WindowText.EMPTY_NA);
        TableViewHelper.setPrecentWidth(lookup, this, 20.0);

        TableColumn<TableNoteData, Optional<FormattedSpan>> data =
            TableViewHelper.getFormatColumn(WindowText.NOTES_DATA, d ->
                d.noteDataProperty());
        TableViewHelper.setPrecentWidth(data, this, 40.0);

        getColumns().addAll(location, id, lookup, data);
    }

    @Override
    protected List<Class<? extends SpanBranch>> getTargetClass(){
        ArrayList<Class<? extends SpanBranch>> list = new ArrayList<>();
        list.add(LinedSpanPointNote.class);
        return list;
    }

    @Override
     protected String getCategory(){
         return targetType == DirectoryType.FOOTNOTE?
            AuxiliaryData.TYPE_FOOTNOTE:
            AuxiliaryData.TYPE_ENDNOTE;
     }

    @Override
     protected TableNoteData buildSpan(SpanBranch span){
         return new TableNoteData((LinedSpanPointNote)span);
     }
}