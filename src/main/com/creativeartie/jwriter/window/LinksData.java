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
 * Stores a list of user notes, hyperlinks.
 */
public class LinksData{
    private ReadOnlyObjectWrapper<Optional<CatalogueIdentity>> linkId;
    private ReadOnlyStringWrapper linkLookupText;
    private ReadOnlyIntegerWrapper linkLocation;
    private ReadOnlyBooleanWrapper linkBookmark;
    private ReadOnlyObjectWrapper<Object> linkData;
    private SpanBranch targetLink;

    public static ObservableList<LinksData> extractList(WritingText text){
        ArrayList<LinksData> list = new ArrayList<>();
        for (SpanBranch span: text.getCatalogue().getIds(AuxiliaryData.TYPE_LINK)){
            if (span instanceof LinedSpanLevelSection ||
                    span instanceof LinedSpanPointLink){
                list.add(new LinksData(span));
            }
        }
        return FXCollections.observableList(list);
    }


    private LinksData(SpanBranch span){
        Optional<CatalogueIdentity> id;
        String lookup;
        boolean bookmark;
        Object target;

        if (span instanceof LinedSpanLevelSection){
            LinedSpanLevelSection section = (LinedSpanLevelSection) span;
            id = section.getSpanIdentity();
            lookup = section.getLookupText();
            bookmark = true;
            target = section;
        } else {
            assert span instanceof LinedSpanPointLink: "Wrong class.";
            LinedSpanPointLink link = (LinedSpanPointLink) span;
            id = link.getSpanIdentity();
            lookup = link.getLookupText();
            bookmark = false;
            target = link.getPath();
        }
        linkId = new ReadOnlyObjectWrapper<>(id);
        linkLookupText = new ReadOnlyStringWrapper(lookup);
        linkLocation = new ReadOnlyIntegerWrapper(span.getStartLine());
        linkBookmark = new ReadOnlyBooleanWrapper(bookmark);
        linkData = new ReadOnlyObjectWrapper<>(target);
        targetLink = span;
    }

    public ReadOnlyObjectProperty<Optional<CatalogueIdentity>> linkIdProperty(){
        return linkId.getReadOnlyProperty();
    }

    public Optional<CatalogueIdentity> getLinkId(){
        return linkId.getValue();
    }

    public ReadOnlyStringProperty linkLookupTextProperty(){
        return linkLookupText.getReadOnlyProperty();
    }

    public String getLookupText(){
        return linkLookupText.getValue();
    }

    public ReadOnlyIntegerProperty linkLocationProperty(){
        return linkLocation.getReadOnlyProperty();
    }

    public int getLinkLocation(){
        return linkLocation.getValue();
    }

    public ReadOnlyBooleanProperty linkBookmarkProperty(){
        return linkBookmark.getReadOnlyProperty();
    }

    public boolean isBookmark(){
        return linkBookmark.getValue();
    }

    public ReadOnlyObjectProperty<Object> linkDataProperty(){
        return linkData.getReadOnlyProperty();
    }

    public Object getLinkData(){
        return linkData.getValue();
    }

    public SpanBranch getTargetSpan(){
        return targetLink;
    }
}