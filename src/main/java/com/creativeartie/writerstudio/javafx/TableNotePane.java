package com.creativeartie.writerstudio.javafx;

import java.util.*;
import javafx.scene.control.*;

import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.*;
import com.creativeartie.writerstudio.resource.*;

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
    protected void buildColumns(){
        TableColumn<TableNoteData, Number> location = TableDataHelper
            .getNumberColumn(WindowText.NOTES_LOCATION, d ->
                d.noteLocationProperty());
        TableDataHelper.setPrecentWidth(location, this, 10.0);

        TableColumn<TableNoteData, Optional<CatalogueIdentity>> id =
            TableDataHelper.getIdColumn(WindowText.NOTES_ID, d ->
                d.noteIdProperty());
        TableDataHelper.setPrecentWidth(id, this, 20.0);

        TableColumn<TableNoteData, String> lookup =
            TableDataHelper.getTextColumn(WindowText.NOTES_LOOKUP, d ->
                d.noteLookupTextProperty(), WindowText.EMPTY_NA);
        TableDataHelper.setPrecentWidth(lookup, this, 20.0);

        TableColumn<TableNoteData, Optional<FormattedSpan>> data =
            TableDataHelper.getFormatColumn(WindowText.NOTES_DATA, d ->
                d.noteDataProperty());
        TableDataHelper.setPrecentWidth(data, this, 50.0);

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
