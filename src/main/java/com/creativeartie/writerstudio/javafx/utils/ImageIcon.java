package com.creativeartie.writerstudio.javafx.utils;

import javafx.scene.image.*;
import javafx.geometry.*;

import com.creativeartie.writerstudio.main.*;
import com.google.common.base.*;

public enum ImageIcon{
    /// Keeping the following code for the WYSIWYG mode
    /*PARAGRAPH_LINE(0, 0), QUOTE_LINE(1, 0),BREAK_LINE(2, 0),NUMBERED_LINE(3, 0),
    BULLET_LINE(4, 0), HEADING_LINE(5, 0), OUTLINE_LINE(6, 0), HYPERLINK_LINE(7, 0),
    DATA_TEXT(8, 0),
    FOOTNOTE_LINE(9, 0), ENDNOTE_LINE(10, 0),AGENDA_LINE(11, 0), SOURCE_LINE(12, 0),
    NOTE_LINE(13, 0),

    BOLD_FORMAT(0, 1), ITALIC_FORMAT(1, 1), UNDERLINE_FORMAT(2, 1),
    CODE_FORMAT(3, 1), STATUS(4, 1), ID(5, 1), LINK_DIRECT(6, 1), LINK_REF(7, 1),
    LINK_TEXT(8, 1), FOOTNOTE_SPAN(9, 1), ENDNOTE_SPAN(10, 1),AGENDA_SPAN(11, 1),
    CITE_SPAN(12, 1), ESCAPE_CHAR(13, 1),*/

    START_MONTH(0, 0), PAST_MONTH(1, 0), NEXT_MONTH(2, 0), END_MONTH(3, 0),
    GOAL_FAIL(7, 0), GOAL_WORD(4, 0), GOAL_TIME(5, 0), GOAL_ALL(6, 0);

    private static final int RAW_SIZE = 128;
    private static final int OUT_SIZE = 24;

    private static Image map;

    private static Image getImage(){
        if (map == null){
            map = new Image(FileResource.ICON_PATH.getResourceStream());
        }
        return map;
    }

    private int row;
    private int col;

    public ImageView getIcon(){
        ImageView icon = new ImageView(getImage());
        int minX = RAW_SIZE * row;
        int minY = RAW_SIZE * col;
        icon.setViewport(new Rectangle2D(minX, minY, RAW_SIZE, RAW_SIZE));
        icon.setFitWidth(OUT_SIZE);
        icon.setPreserveRatio(true);
        return icon;
    }

    private ImageIcon(int r, int c){
        row = r;
        col = c;
    }
}
