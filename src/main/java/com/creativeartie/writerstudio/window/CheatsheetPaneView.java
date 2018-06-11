package com.creativeartie.writerstudio.window;

import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.scene.layout.*;
import javafx.beans.property.*;
import javafx.beans.binding.*;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.*;
import com.creativeartie.writerstudio.main.*;
import com.creativeartie.writerstudio.resource.*;

import static com.creativeartie.writerstudio.window.CheatsheetLabel.getLabel;

/**
 * A cheatsheet showing various of hints. The {@link CheatsheetLabel labels}
 * can also change style class base on position of the cursor.
 */
abstract class CheatsheetPaneView extends GridPane{

    private final ArrayList<CheatsheetLabel> labelList;

    CheatsheetPaneView(){
        labelList = initHintsLabels();
    }

    /// Layout Node
    private ArrayList<CheatsheetLabel> initHintsLabels(){
        /// Magic numbers just change the positions with column sizes
        ArrayList<CheatsheetLabel> ans = new ArrayList<>();

        addLabel(ans, getLabel(CheatsheetText.LINED_HEADING), 0, 0, 2, 1);
        addLabel(ans, getLabel(CheatsheetText.LINED_OUTLINE), 0, 1, 2, 1);
        addLabel(ans, getLabel(CheatsheetText.EDITION_STUB),  0, 2);
        addLabel(ans, getLabel(CheatsheetText.EDITION_DRAFT), 0, 3);
        setPrecentWidth(7);
        addLabel(ans, getLabel(CheatsheetText.EDITION_OTHER), 1, 2);
        addLabel(ans, getLabel(CheatsheetText.EDITION_FINAL), 1, 3);
        setPrecentWidth(8); /// fills to: 15

        addLabel(ans, getLabel(CheatsheetText.LINED_PARAGRAPH), 2, 0);
        addLabel(ans, getLabel(CheatsheetText.LINED_NUMBERED),  2, 1);
        addLabel(ans, getLabel(CheatsheetText.LINED_BULLET),    2, 2);
        addLabel(ans, getLabel(CheatsheetText.LINED_BREAK),     2, 3);
        setPrecentWidth(11); /// fills to: 26

        /// Future:
        ///     column 3     = quote, agenda, graphs (pie, line, etc.), picture
        ///     column 4 - 5 = table, source, InfoFieldTypes
        addLabel(ans, getLabel(CheatsheetText.LINED_QUOTE),  3, 0);
        addLabel(ans, getLabel(CheatsheetText.LINED_CITE),   3, 1, 2, 1);
        addLabel(ans, getLabel(CheatsheetText.FIELD_SOURCE), 3, 2);
        addLabel(ans, getLabel(CheatsheetText.FIELD_REF),    3, 3);
        setPrecentWidth(6); /// fills to: 32
        addLabel(ans, getLabel(CheatsheetText.LINED_AGENDA),   4, 0);
        addLabel(ans, getLabel(CheatsheetText.FIELD_IN_TEXT),  4, 2);
        addLabel(ans, getLabel(CheatsheetText.FIELD_FOOTNOTE), 4, 3);
        setPrecentWidth(6); /// fills to: 38

        addLabel(ans, getLabel(CheatsheetText.LINED_NOTE),     5, 0);
        addLabel(ans, getLabel(CheatsheetText.LINED_FOOTNOTE), 5, 1);
        addLabel(ans, getLabel(CheatsheetText.LINED_ENDNOTE),  5, 2);
        addLabel(ans, getLabel(CheatsheetText.LINED_LINK),     5, 3);
        setPrecentWidth(12); /// fills to: 50

        addLabel(ans, getLabel(CheatsheetText.FORMAT_CITE),     6, 0);
        addLabel(ans, getLabel(CheatsheetText.FORMAT_FOOTNOTE), 6, 1);
        addLabel(ans, getLabel(CheatsheetText.FORMAT_ENDNOTE),  6, 2);
        addLabel(ans, getLabel(CheatsheetText.FORMAT_REF_LINK), 6, 3);
        setPrecentWidth(12); /// fills to: 62

        addLabel(ans, getLabel(CheatsheetText.FORMAT_AGENDA),      7, 0);
        addLabel(ans, getLabel(CheatsheetText.OTHER_ESCAPE),       7, 1);
        addLabel(ans, getLabel(CheatsheetText.FORMAT_DIRECT_LINK), 7, 2);
        addLabel(ans, getLabel(CheatsheetText.OTHER_ID),           7, 3);
        setPrecentWidth(12); /// fills to: 74

        addLabel(ans, getLabel(CheatsheetText.FORMAT_REF_KEY), 8, 0);
        setPrecentWidth(12); /// fills to: 86

        addLabel(ans, getLabel(CheatsheetText.FORMAT_BOLD),      9, 0);
        addLabel(ans, getLabel(CheatsheetText.FORMAT_CODED),     9, 1);
        addLabel(ans, getLabel(CheatsheetText.FORMAT_ITALICS),   9, 2);
        addLabel(ans, getLabel(CheatsheetText.FORMAT_UNDERLINE), 9, 3);
        setPrecentWidth(14); /// fills to: 100
        return ans;
    }

    /**
     * Adds a {@link CheatsheetLabe} to the grid and to a
     * {@linkplain ArrayList}. Helper method of {@link #initHintsLabels()}.
     */
    private void addLabel(ArrayList<CheatsheetLabel> labels,
            CheatsheetLabel label, int column, int row){
        labels.add(label);
        add(label, column, row);
    }

    /**
     * Adds a {@link CheatsheetLabe} to the grid and to a
     * {@linkplain ArrayList}. Helper method of {@link #initHintsLabels()}.
     */
    private void addLabel(ArrayList<CheatsheetLabel> labels,
            CheatsheetLabel label, int column, int row, int columnSpan,
            int rowSpan){
        labels.add(label);
        add(label, column, row, columnSpan, rowSpan);
    }

    /**
     * Set the next column by percent width. Helper method of
     * {@link #initHintsLabels()}.
     */
    private void setPrecentWidth(double value){
        ColumnConstraints column = new ColumnConstraints();
        column.setPercentWidth(value);
        getColumnConstraints().add(column);
    }

    /// Getters
    protected List<CheatsheetLabel> getLabels(){
        return labelList;
    }

    /// Node Properties

    /// Control Methods
}
