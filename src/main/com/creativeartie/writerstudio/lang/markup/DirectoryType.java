package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;

import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;

/**
 * Styles for the {@link CatalogueMap} created by user provided
 * {@link CatalogueIdentity}.
 */
public enum DirectoryType implements StyleInfo{
    /// Enum value order mandated by LinedParsePointer, DirectoryType and in
    /// interface

    /** {@linkplain StyleInfo} for {@link BasicTextEscape}. */
    COMMENT(TYPE_COMMENT, "", ""),
    NOTE(TYPE_NOTE, CURLY_CITE),
    FOOTNOTE(TYPE_FOOTNOTE, CURLY_FOOTNOTE),
    ENDNOTE(TYPE_ENDNOTE, CURLY_ENDNOTE),
    LINK(TYPE_LINK, LINK_REF, LINK_END);

    /** Create the list of user notes.*/
    public static DirectoryType[] getMenuList(){
        return Arrays.copyOfRange(values(), 1, values().length);
    }

    /** Start reference key span's string. */
    private final String startString;
    /** End reference key span's string */
    private final String endString;
    /** The first and upper most category. */
    private final String baseCategory;

    private DirectoryType(String category, String start){
        this (category, start, CURLY_END);
    }

    private DirectoryType(String category, String start, String end){
        baseCategory = category;
        startString = start;
        endString = end;
    }

    public String getStart(){
        return startString;
    }

    public String getEnd(){
        return endString;
    }

    public String getCategory(){
        return baseCategory;
    }
}
