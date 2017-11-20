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
        PARAGRAPH, QUOTE, BREAK, NUMBERED, BULLET, FOOTNOTE, ENDNOTE, NOTE,
        HEADING, OUTLINE, HYPERLINK, AGENDA, SOURCE,
        FORMAT_BOLD, FORMAT_ITALICS,FORMAT_CODED,FORMAT_UNDERLINE,
        EDITION, ID, FORMAT_ESCAPE,
        FORMAT_LINK_DIR, FORMAT_LINK_REF, FORMAT_AGENDA,
        FORMAT_NOTE, FORMAT_FOOTNOTE, FORMAT_ENDNOTE;
    }

    private Name nameType;

    public PaneCheatsheetLabel(Name type){
        super(Utilities.getString("CheatSheet." + Utilities.enumToKey(type
            .name())));
        nameType = type;
    }

    private boolean compare(DetailStyle style){
        return nameType == Name.valueOf(style.name());
    }

    private String toType(){
        return nameType.name().substring("FORMAT_".length());
    }

    public boolean isTurnOn(ManuscriptDocument doc, int point){
        if (Name.SOURCE.ordinal() >= nameType.ordinal()){
            return doc.spansAt(point, LinedSpan.class).map(
                    span -> compare(span.getLinedType())
                ).orElse(false);
        } else if (Name.FORMAT_UNDERLINE.ordinal() >= nameType.ordinal()){
            ///For format
            return doc.spansAt(point, FormatSpan.class).map(
                    span -> span.isFormat(FormatType.valueOf(toType()))
                ).orElse(false);
        } else if (Name.EDITION == nameType){
            return doc.spansAt(point, EditionSpan.class).isPresent();
        } else if (Name.ID == nameType){
            return doc.spansAt(point, DirectorySpan.class).isPresent();
        } else if (Name.FORMAT_ESCAPE == nameType){
            return doc.spansAt(point, BasicTextEscape.class).isPresent();
        } else if (Name.FORMAT_LINK_DIR == nameType){
            return doc.spansAt(point, FormatSpanLinkDirect.class).isPresent();
        } else if (Name.FORMAT_LINK_REF == nameType){
            return doc.spansAt(point, FormatSpanLinkRef.class).isPresent();
        } else if (Name.FORMAT_AGENDA == nameType){
            return doc.spansAt(point, FormatSpanAgenda.class).isPresent();
        }
        return doc.spansAt(point, FormatSpanDirectory.class).map(
                span -> span.getIdType() == DirectoryType.valueOf(toType())
            ).orElse(false);
    }
}
