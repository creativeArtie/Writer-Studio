package com.creativeartie.writerstudio.lang.markup;

import com.creativeartie.writerstudio.lang.*;

public class FormatCurlyDebug {

    public static IDBuilder buildNoteId(String name){
        return new IDBuilder().addCategory(AuxiliaryData.TYPE_RESEARCH).setId(name);
    }

    public static IDBuilder buildFootnoteId(String name){
        return new IDBuilder().addCategory(AuxiliaryData.TYPE_FOOTNOTE).setId(name);
    }

    public static IDBuilder buildEndnoteId(String name){
        return new IDBuilder().addCategory(AuxiliaryData.TYPE_ENDNOTE).setId(name);
    }
}
