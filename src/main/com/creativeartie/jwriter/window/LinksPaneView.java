package com.creativeartie.jwriter.window;

import java.util.*;
import java.util.Optional;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.scene.layout.*;
import javafx.beans.property.*;
import javafx.collections.*;
import javafx.scene.control.cell.*;

import com.creativeartie.jwriter.main.*;
import com.creativeartie.jwriter.lang.*;
import com.creativeartie.jwriter.lang.markup.*;
import com.creativeartie.jwriter.resource.*;

import com.google.common.base.*;
import com.google.common.collect.*;

/**
 * Stores a list hyperlinks.
 */
abstract class LinksPaneView extends TableView<LinksData>{
    public LinksPaneView(){
        TableViewHelper.styleTableView(this, WindowText.LINKS_EMPTY);
        initLinksColumns();
    }


    @SuppressWarnings("unchecked") /// For getColumns().addAdd(TableColumn ...)
    private void initLinksColumns(){
        TableColumn<LinksData, Number> location = TableViewHelper
            .getNumberColumn(WindowText.LINKS_LOCATION, d ->
                d.linkLocationProperty());
        TableViewHelper.setPrecentWidth(location, this, 10.0);

        TableColumn<LinksData, Optional<CatalogueIdentity>> id =
            TableViewHelper.getIdColumn(WindowText.LINKS_ID, d ->
                d.linkIdProperty());
        TableViewHelper.setPrecentWidth(id, this, 20.0);

        TableColumn<LinksData, Boolean> bookmark =
            TableViewHelper.getBooleanColumn(WindowText.LINKS_BOOKMARK, d ->
                d.linkBookmarkProperty());
        TableViewHelper.setPrecentWidth(bookmark, this, 10.0);

        TableColumn<LinksData, String> lookup =
            TableViewHelper.getTextColumn(WindowText.LINKS_LOOKUP, d ->
                d.linkLookupTextProperty(), WindowText.EMPTY_NA);
        TableViewHelper.setPrecentWidth(lookup, this, 20.0);

        TableColumn<LinksData, Object> data =
            TableViewHelper.getLinkColumn(WindowText.LINKS_DATA, d ->
                d.linkDataProperty());
        TableViewHelper.setPrecentWidth(data, this, 40.0);

        getColumns().addAll(location, id, bookmark, lookup, data);
    }

}