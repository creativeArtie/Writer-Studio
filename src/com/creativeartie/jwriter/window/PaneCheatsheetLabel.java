package com.creativeartie.jwriter.window;

import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.scene.layout.*;
import javafx.beans.property.*;
import javafx.beans.binding.*;
import java.util.function.*;

import java.util.*;

import com.creativeartie.jwriter.lang.*;
import com.creativeartie.jwriter.lang.markup.*;
import com.creativeartie.jwriter.main.*;

class PaneCheatsheetLabel extends Label{
    public enum Name{
        PARAGRAPH,QUOTE,BREAK,NUMBERED,BULLET,HEADING,OUTLINE,HYPERLINK,
        FOOTNOTE,ENDNOTE,AGENDA,SOURCE,NOTE,STUB,DRAFT,FINAL,OTHER,FORMAT_BOLD,
        FORMAT_ITALICS,FORMAT_CODE,FORMAT_UNDERLINE,FORMAT_ESCAPE,
        FORMAT_LINK_DIR,FORMAT_LINK_REF,FORMAT_FOOTNOTE,FORMAT_ENDNOTE,
        FORMAT_NOTE;
    }

    public PaneCheatsheetLabel(String text){
        super(text);
    }
}
