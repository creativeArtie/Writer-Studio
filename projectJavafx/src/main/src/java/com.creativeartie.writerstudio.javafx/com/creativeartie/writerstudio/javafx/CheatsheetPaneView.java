package com.creativeartie.writerstudio.javafx;

import java.util.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;


import com.creativeartie.writerstudio.javafx.utils.*;
import static com.creativeartie.writerstudio.javafx.utils.HintLabel.getLabel;
import static com.creativeartie.writerstudio.javafx.utils.LayoutConstants.
    HintConstants.*;

/**
 * A cheatsheet showing various of hints. The {@link HintLabel labels}
 * can also change style class base on position of the cursor.
 */
abstract class CheatsheetPaneView extends GridPane{
    /// %Part 1: Constructor and Class Fields

    private final ArrayList<HintLabel> hintLabels;

    CheatsheetPaneView(){
        hintLabels = new ArrayList<>();

        addLabel(hintLabels, getLabel(HintText.LINED_HEADING), 0, 0, 2, 1);
        addLabel(hintLabels, getLabel(HintText.LINED_OUTLINE), 0, 1, 2, 1);
        addLabel(hintLabels, getLabel(HintText.EDITION_STUB),  0, 2);
        addLabel(hintLabels, getLabel(HintText.EDITION_DRAFT), 0, 3);
        CommonLayoutUtility.setWidthPrecent(this, COLUMN1);
        addLabel(hintLabels, getLabel(HintText.EDITION_OTHER), 1, 2);
        addLabel(hintLabels, getLabel(HintText.EDITION_FINAL), 1, 3);
        CommonLayoutUtility.setWidthPrecent(this, COLUMN2);

        addLabel(hintLabels, getLabel(HintText.LINED_PARAGRAPH), 2, 0);
        addLabel(hintLabels, getLabel(HintText.LINED_NUMBERED),  2, 1);
        addLabel(hintLabels, getLabel(HintText.LINED_BULLET),    2, 2);
        addLabel(hintLabels, getLabel(HintText.LINED_BREAK),     2, 3);
        CommonLayoutUtility.setWidthPrecent(this, COLUMN3);

        /// Future:
        ///     column 3     = quote, agenda, graphs (pie, line, etc.), picture
        ///     column 4 - 5 = table, source, InfoFieldTypes
        addLabel(hintLabels, getLabel(HintText.LINED_QUOTE),  3, 0);
        addLabel(hintLabels, getLabel(HintText.LINED_CITE),   3, 1, 2, 1);
        addLabel(hintLabels, getLabel(HintText.FIELD_SOURCE), 3, 2);
        addLabel(hintLabels, getLabel(HintText.FIELD_REF),    3, 3);
        CommonLayoutUtility.setWidthPrecent(this, COLUMN4);
        addLabel(hintLabels, getLabel(HintText.LINED_AGENDA),   4, 0);
        addLabel(hintLabels, getLabel(HintText.FIELD_IN_TEXT),  4, 2);
        addLabel(hintLabels, getLabel(HintText.FIELD_FOOTNOTE), 4, 3);
        CommonLayoutUtility.setWidthPrecent(this, COLUMN5);

        addLabel(hintLabels, getLabel(HintText.LINED_NOTE),     5, 0);
        addLabel(hintLabels, getLabel(HintText.LINED_FOOTNOTE), 5, 1);
        addLabel(hintLabels, getLabel(HintText.LINED_ENDNOTE),  5, 2);
        addLabel(hintLabels, getLabel(HintText.LINED_LINK),     5, 3);
        CommonLayoutUtility.setWidthPrecent(this, COLUMN6);

        addLabel(hintLabels, getLabel(HintText.FORMAT_CITE),     6, 0);
        addLabel(hintLabels, getLabel(HintText.FORMAT_FOOTNOTE), 6, 1);
        addLabel(hintLabels, getLabel(HintText.FORMAT_ENDNOTE),  6, 2);
        addLabel(hintLabels, getLabel(HintText.FORMAT_REF_LINK), 6, 3);
        CommonLayoutUtility.setWidthPrecent(this, COLUMN7);

        addLabel(hintLabels, getLabel(HintText.FORMAT_AGENDA),      7, 0);
        addLabel(hintLabels, getLabel(HintText.OTHER_ESCAPE),       7, 1);
        addLabel(hintLabels, getLabel(HintText.FORMAT_DIRECT_LINK), 7, 2);
        addLabel(hintLabels, getLabel(HintText.OTHER_ID),           7, 3);
        CommonLayoutUtility.setWidthPrecent(this, COLUMN8);

        addLabel(hintLabels, getLabel(HintText.FORMAT_REF_KEY), 8, 0);
        CommonLayoutUtility.setWidthPrecent(this, COLUMN9);

        addLabel(hintLabels, getLabel(HintText.FORMAT_BOLD),      9, 0);
        addLabel(hintLabels, getLabel(HintText.FORMAT_CODED),     9, 1);
        addLabel(hintLabels, getLabel(HintText.FORMAT_ITALICS),   9, 2);
        addLabel(hintLabels, getLabel(HintText.FORMAT_UNDERLINE), 9, 3);
        CommonLayoutUtility.setWidthPrecent(this, COLUMN10);
    }

    /// %Part 2: Layout - Utilities

    /**
     * Adds a {@link HintLabel} to the grid and to a
     * {@linkplain ArrayList}. Helper method of {@link #initHintsLabels()}.
     */
    private void addLabel(ArrayList<HintLabel> labels,
            HintLabel label, int column, int row){
        labels.add(label);
        add(label, column, row);
    }

    /**
     * Adds a {@link CheatsheetLabe} to the grid and to a
     * {@linkplain ArrayList}. Helper method of {@link #initHintsLabels()}.
     */
    private void addLabel(ArrayList<HintLabel> labels,
            HintLabel label, int column, int row, int columnSpan,
            int rowSpan){
        labels.add(label);
        add(label, column, row, columnSpan, rowSpan);
    }

    /// %Part 3: Setup Properties

    public void postLoad(WriterSceneControl control){
        bindChildren(control);
    }

    protected abstract void bindChildren(WriterSceneControl control);

    /// %Part 4: Properties

    /// %Part 5: Get Child Methods

    protected List<HintLabel> getHintLabels(){
        return hintLabels;
    }
}
