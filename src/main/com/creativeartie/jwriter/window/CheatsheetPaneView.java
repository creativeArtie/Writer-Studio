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

        addLabel(ans, CheatsheetLabel.getLabel(LinedType.HEADING), 0, 0, 2, 1);
        addLabel(ans, CheatsheetLabel.getLabel(LinedType.OUTLINE), 0, 1, 2, 1);
        addLabel(ans, CheatsheetLabel.getLabel(EditionType.STUB),  0, 2);
        addLabel(ans, CheatsheetLabel.getLabel(EditionType.DRAFT), 0, 3);
        setPrecentWidth(10);
        addLabel(ans, CheatsheetLabel.getLabel(EditionType.OTHER), 1, 2);
        addLabel(ans, CheatsheetLabel.getLabel(EditionType.FINAL), 1, 3);
        setPrecentWidth(10); /// fills to: 20

        addLabel(ans, CheatsheetLabel.getLabel(LinedType.PARAGRAPH), 2, 0);
        addLabel(ans, CheatsheetLabel.getLabel(LinedType.NUMBERED),  2, 1);
        addLabel(ans, CheatsheetLabel.getLabel(LinedType.BULLET),    2, 2);
        addLabel(ans, CheatsheetLabel.getLabel(LinedType.BREAK),     2, 3);
        setPrecentWidth(11); /// fills to: 31

        /// Future:
        ///     column 3     = quote, agenda, graphs (pie, line, etc.), picture
        ///     column 4 - 5 = table, source, InfoFieldTypes
        addLabel(ans, CheatsheetLabel.getLabel(LinedType.QUOTE),      3, 0);
        addLabel(ans, CheatsheetLabel.getLabel(LinedType.SOURCE),     3, 1, 2, 1);
        addLabel(ans, CheatsheetLabel.getLabel(InfoFieldType.SOURCE), 3, 2);
        setPrecentWidth(6); /// fills to: 37
        addLabel(ans, CheatsheetLabel.getLabel(LinedType.AGENDA),       4, 0);
        addLabel(ans, CheatsheetLabel.getLabel(InfoFieldType.IN_TEXT),  4, 2);
        addLabel(ans, CheatsheetLabel.getLabel(InfoFieldType.FOOTNOTE), 4, 3);
        setPrecentWidth(6); /// fills to: 43

        addLabel(ans, CheatsheetLabel.getLabel(LinedType.NOTE),      5, 0);
        addLabel(ans, CheatsheetLabel.getLabel(LinedType.FOOTNOTE),  5, 1);
        addLabel(ans, CheatsheetLabel.getLabel(LinedType.ENDNOTE),   5, 2);
        addLabel(ans, CheatsheetLabel.getLabel(LinedType.HYPERLINK), 5, 3);
        setPrecentWidth(12); /// fills to: 55

        addLabel(ans, CheatsheetLabel.getLabel(DirectoryType.NOTE),     6, 0);
        addLabel(ans, CheatsheetLabel.getLabel(DirectoryType.FOOTNOTE), 6, 1);
        addLabel(ans, CheatsheetLabel.getLabel(DirectoryType.ENDNOTE),  6, 2);
        addLabel(ans, CheatsheetLabel.getLabel(AuxiliaryType.REF_LINK), 6, 3);
        setPrecentWidth(17); /// fills to: 72

        addLabel(ans, CheatsheetLabel.getLabel(AuxiliaryType.AGENDA),      7, 0);
        addLabel(ans, CheatsheetLabel.getLabel(AuxiliaryType.ESCAPE),      7, 1);
        addLabel(ans, CheatsheetLabel.getLabel(AuxiliaryType.DIRECT_LINK), 7, 2);
        addLabel(ans, CheatsheetLabel.getIdentityLabel(),                  7, 3);
        setPrecentWidth(17); /// fills to: 89

        addLabel(ans, CheatsheetLabel.getLabel(FormatType.BOLD),      8, 0);
        addLabel(ans, CheatsheetLabel.getLabel(FormatType.CODED),     8, 1);
        addLabel(ans, CheatsheetLabel.getLabel(FormatType.ITALICS),   8, 2);
        addLabel(ans, CheatsheetLabel.getLabel(FormatType.UNDERLINE), 8, 3);
        setPrecentWidth(11); /// fills to: 100
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
