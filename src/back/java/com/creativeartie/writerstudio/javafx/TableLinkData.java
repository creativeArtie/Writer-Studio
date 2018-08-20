package com.creativeartie.writerstudio.javafx;

import java.util.*;
import javafx.beans.property.*;

import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.*;

/**
 * Stores a list of user notes, hyperlinks.
 */
public class TableLinkData implements TableData{
    private ReadOnlyObjectWrapper<Optional<CatalogueIdentity>> linkId;
    private ReadOnlyStringWrapper linkLookupText;
    private ReadOnlyIntegerWrapper linkLocation;
    private ReadOnlyBooleanWrapper linkBookmark;
    private ReadOnlyObjectWrapper<Object> linkData;
    private SpanBranch targetLink;

    TableLinkData(SpanBranch span){
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

    @Override
    public SpanBranch getTargetSpan(){
        return targetLink;
    }
}
