package com.creativeartie.jwriter.window;

import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.scene.layout.*;
import javafx.beans.property.*;
import javafx.beans.binding.*;

import java.util.*;

import com.creativeartie.jwriter.lang.*;
import com.creativeartie.jwriter.lang.markup.*;
import com.creativeartie.jwriter.main.*;
import static com.creativeartie.jwriter.window.PaneCheatsheetLabel.Name;

abstract class PaneCheatsheetView extends GridPane{
    PaneCheatsheetLabel[] labels;

    PaneCheatsheetView(){
        labels = new PaneCheatsheetLabel[Name.values().length];
        int i = 0;
        for (Name type: Name.values()){
            labels[i++] = new PaneCheatsheetLabel(Utilities.getString(
                "CheatSheet." + Utilities.enumToKey(type.name())
            ));
        }
        layoutLabels();
    }

    private void layoutLabels(){
        add(labels[Name.NUMBERED.ordinal()], 0, 0);
        add(labels[Name.HEADING.ordinal()], 0, 1);
        add(labels[Name.BULLET.ordinal()], 1, 0);
        add(labels[Name.OUTLINE.ordinal()], 1, 1);
        add(labels[Name.PARAGRAPH.ordinal()], 2, 0);
        add(labels[Name.QUOTE.ordinal()], 2, 1);
        add(labels[Name.BREAK.ordinal()], 2, 2);
        add(labels[Name.FORMAT_BOLD.ordinal()], 3, 0);
        add(labels[Name.FORMAT_CODE.ordinal()], 3, 1);
        add(labels[Name.FORMAT_ESCAPE.ordinal()], 3, 2);
        add(labels[Name.FORMAT_ITALICS.ordinal()], 4, 0);
        add(labels[Name.FORMAT_UNDERLINE.ordinal()], 4, 1);
        add(labels[Name.FORMAT_LINK_DIR.ordinal()], 4, 2);
        add(labels[Name.FORMAT_FOOTNOTE.ordinal()], 5, 0);
        add(labels[Name.FORMAT_ENDNOTE.ordinal()], 5, 1);
        add(labels[Name.FORMAT_LINK_REF.ordinal()], 5, 2);
        add(labels[Name.FOOTNOTE.ordinal()], 6, 0);
        add(labels[Name.ENDNOTE.ordinal()], 6, 1);
        add(labels[Name.HYPERLINK.ordinal()], 6, 2);
        add(labels[Name.NOTE.ordinal()], 7, 0);
        add(labels[Name.SOURCE.ordinal()], 7, 1);
        add(labels[Name.AGENDA.ordinal()], 7, 2);
    }
}