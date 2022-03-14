package com.creativeartie.writer.javafx;

import java.util.*;
import javafx.scene.control.*;

import com.creativeartie.writer.lang.*;
import com.creativeartie.writer.lang.markup.*;

import com.creativeartie.writer.javafx.utils.*;
import static com.creativeartie.writer.javafx.utils.LayoutConstants.
    LinkConstants.*;

/**
 * Stores a list hyperlinks.
 */
public class DataPaneLink extends DataControl<DataInputLink>{

    public DataPaneLink(){
        super(EMPTY_TEXT);
    }

    @Override
    @SuppressWarnings("unchecked") /// For getColumns().addAdd(TableColumn ...)
    protected void buildColumns(){
        TableColumn<DataInputLink, Number> location = TableCellFactory
            .getNumberColumn(LOCATION_NAME, d -> d.linkLocationProperty());
        TableCellFactory.setPrecentWidth(location, this, LOCATION_WIDTH);

        TableColumn<DataInputLink, Optional<CatalogueIdentity>> id =
            TableCellFactory.getIdColumn(ID_NAME, d -> d.linkIdProperty());
        TableCellFactory.setPrecentWidth(id, this, ID_WIDTH);

        TableColumn<DataInputLink, Boolean> bookmark =
            TableCellFactory.getBooleanColumn(BOOKMARK_NAME, d ->
                d.linkBookmarkProperty());
        TableCellFactory.setPrecentWidth(bookmark, this, BOOKMARK_WIDTH);

        TableColumn<DataInputLink, String> lookup =
            TableCellFactory.getTextColumn(LOOKUP_NAME, d ->
                d.linkLookupTextProperty(), EMPTY_NA);
        TableCellFactory.setPrecentWidth(lookup, this, LOOKUP_WIDTH);

        TableColumn<DataInputLink, Object> data =
            TableCellFactory.getLinkColumn(CONTENT_NAME, d ->
                d.linkDataProperty());
        TableCellFactory.setPrecentWidth(data, this, CONTENT_WIDTH);

        getColumns().addAll(location, id, bookmark, lookup, data);
    }

    @Override
    protected List<Class<? extends SpanBranch>> getTargetClass(){
        ArrayList<Class<? extends SpanBranch>> list = new ArrayList<>();
        list.add(LinedSpanLevelSection.class);
        list.add(LinedSpanPointLink.class);
        return list;
    }

    @Override
     protected String getCategory(){
         return AuxiliaryData.TYPE_LINK;
     }

    @Override
     protected DataInputLink buildSpan(SpanBranch span){
         return new DataInputLink(span);
     }
}
