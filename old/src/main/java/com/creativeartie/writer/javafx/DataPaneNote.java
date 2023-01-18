package com.creativeartie.writer.javafx;

import java.util.*;
import javafx.scene.control.*;

import com.creativeartie.writer.lang.*;
import com.creativeartie.writer.lang.markup.*;
import com.creativeartie.writer.javafx.utils.*;
import static com.creativeartie.writer.javafx.utils.LayoutConstants.
    NoteConstants.*;
/**
 * Stores a list hyperlinks.
 */
public class DataPaneNote extends DataControl<DataInputNote>{
    private DirectoryType targetType;

    public DataPaneNote(DirectoryType type){
        super(type == DirectoryType.FOOTNOTE? EMPTY_ENDNOTE: EMPTY_ENDNOTE);
        targetType = type;
    }

    @Override
    @SuppressWarnings("unchecked") /// For getColumns().addAdd(TableColumn ...)
    protected void buildColumns(){
        TableColumn<DataInputNote, Number> location = TableCellFactory
            .getNumberColumn(LOCATION_NAME, d ->
                d.noteLocationProperty());
        TableCellFactory.setPrecentWidth(location, this, LOCATION_WIDTH);

        TableColumn<DataInputNote, Optional<CatalogueIdentity>> id =
            TableCellFactory.getIdColumn(ID_NAME, d ->
                d.noteIdProperty());
        TableCellFactory.setPrecentWidth(id, this, ID_WIDTH);

        TableColumn<DataInputNote, String> lookup =
            TableCellFactory.getTextColumn(LOOKUP_NAME, d ->
                d.noteLookupTextProperty(), EMPTY_NA);
        TableCellFactory.setPrecentWidth(lookup, this, LOOKUP_WIDTH);

        TableColumn<DataInputNote, Optional<FormattedSpan>> content =
            TableCellFactory.getFormatColumn(CONTENT_NAME, d ->
                d.noteDataProperty());
        TableCellFactory.setPrecentWidth(content, this, CONTENT_WIDTH);

        getColumns().addAll(location, id, lookup, content);
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
     protected DataInputNote buildSpan(SpanBranch span){
         return new DataInputNote((LinedSpanPointNote)span);
     }
}
