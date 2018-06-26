package com.creativeartie.writerstudio.fxgui;

import java.util.*;
import javafx.scene.control.*;

import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.resource.*;
import com.creativeartie.writerstudio.lang.markup.*;

/**
 * Stores a list hyperlinks.
 */
public class TableLinkPane extends TableDataControl<TableLinkData>{

    public TableLinkPane(){
        super(WindowText.LINKS_EMPTY);
    }

    @Override
    @SuppressWarnings("unchecked") /// For getColumns().addAdd(TableColumn ...)
    protected void buildColumns(){
        TableColumn<TableLinkData, Number> location = TableDataHelper
            .getNumberColumn(WindowText.LINKS_LOCATION, d ->
                d.linkLocationProperty());
        TableDataHelper.setPrecentWidth(location, this, 10.0);

        TableColumn<TableLinkData, Optional<CatalogueIdentity>> id =
            TableDataHelper.getIdColumn(WindowText.LINKS_ID, d ->
                d.linkIdProperty());
        TableDataHelper.setPrecentWidth(id, this, 20.0);

        TableColumn<TableLinkData, Boolean> bookmark =
            TableDataHelper.getBooleanColumn(WindowText.LINKS_BOOKMARK, d ->
                d.linkBookmarkProperty());
        TableDataHelper.setPrecentWidth(bookmark, this, 10.0);

        TableColumn<TableLinkData, String> lookup =
            TableDataHelper.getTextColumn(WindowText.LINKS_LOOKUP, d ->
                d.linkLookupTextProperty(), WindowText.EMPTY_NA);
        TableDataHelper.setPrecentWidth(lookup, this, 20.0);

        TableColumn<TableLinkData, Object> data =
            TableDataHelper.getLinkColumn(WindowText.LINKS_DATA, d ->
                d.linkDataProperty());
        TableDataHelper.setPrecentWidth(data, this, 40.0);

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
     protected TableLinkData buildSpan(SpanBranch span){
         return new TableLinkData(span);
     }
}
