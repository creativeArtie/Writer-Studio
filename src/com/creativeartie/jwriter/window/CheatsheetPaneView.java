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

abstract class CheatsheetPaneView extends GridPane{

    private TreeMap<LinedType, CheatsheetLabel> lineLabels;
    private TreeMap<FormatType, CheatsheetLabel> formatLabels;
    private TreeMap<EditionType, CheatsheetLabel> editionLabels;
    private TreeMap<InfoFieldType, CheatsheetLabel> infoLabels;
    private TreeMap<AuxiliaryType, CheatsheetLabel> otherLabels;
    private TreeMap<DirectoryType, CheatsheetLabel> refLabels;
    private CheatsheetLabel idLabels;
    private ArrayList<CheatsheetLabel> labelList;

    CheatsheetPaneView(){
        labelList = new ArrayList<>();
        lineLabels = new TreeMap<>();
        CheatsheetLabel holder;
        for(LinedType type: LinedType.values()){
            holder = CheatsheetLabel.getLabel(type);
            lineLabels.put(type, holder);
            labelList.add(holder);
        }

        formatLabels = new TreeMap<>();
        for(FormatType type: FormatType.values()){
            holder = CheatsheetLabel.getLabel(type);
            formatLabels.put(type, holder);
            labelList.add(holder);
        }

        editionLabels = new TreeMap<>();
        for (EditionType type: EditionType.values()){
            if (type != EditionType.NONE){
                holder = CheatsheetLabel.getLabel(type);
                editionLabels.put(type, holder);
                labelList.add(holder);
            }
        }

        infoLabels = new TreeMap<>();
        for (InfoFieldType type: InfoFieldType.values()){
            if (type != InfoFieldType.ERROR){
                holder = CheatsheetLabel.getLabel(type);
                infoLabels.put(type, holder);
                labelList.add(holder);
            }
        }

        refLabels = new TreeMap<>();
        for (DirectoryType type: DirectoryType.values()){
            if (type != DirectoryType.COMMENT && type != DirectoryType.LINK){
                holder = CheatsheetLabel.getLabel(type);
                refLabels.put(type, holder);
                labelList.add(holder);
            }
        }

        idLabels = CheatsheetLabel.getIdentityLabel();
        labelList.add(idLabels);

        otherLabels = new TreeMap<>();
        for (AuxiliaryType type: AuxiliaryType.getFormatTypes()){
            holder = CheatsheetLabel.getLabel(type);
            otherLabels.put(type, holder);
            labelList.add(holder);
        }
        layoutLabels();
    }

    private void layoutLabels(){
        add(lineLabels.get(LinedType.PARAGRAPH), 0, 0);
        add(lineLabels.get(LinedType.QUOTE),     0, 1);
        add(lineLabels.get(LinedType.AGENDA),    0, 2);

        add(lineLabels.get(LinedType.NUMBERED), 1, 0);
        add(lineLabels.get(LinedType.BULLET),   1, 1);
        add(lineLabels.get(LinedType.BREAK),    1, 2);

        add(lineLabels.get(LinedType.HEADING),    2, 0, 3, 1);
        add(lineLabels.get(LinedType.OUTLINE),    2, 1, 3, 1);
        add(editionLabels.get(EditionType.STUB),  2, 2);
        add(editionLabels.get(EditionType.DRAFT), 3, 2);
        add(editionLabels.get(EditionType.FINAL), 4, 2);

        add(lineLabels.get(LinedType.NOTE),    5, 0);
        add(lineLabels.get(LinedType.SOURCE),  5, 1);
        add(editionLabels.get(EditionType.OTHER), 5, 2);

        add(infoLabels.get(InfoFieldType.SOURCE),   6, 0);
        add(infoLabels.get(InfoFieldType.IN_TEXT),  6, 1);
        add(infoLabels.get(InfoFieldType.FOOTNOTE), 6, 2);


        add(lineLabels.get(LinedType.FOOTNOTE),  7, 0);
        add(lineLabels.get(LinedType.ENDNOTE),   7, 1);
        add(lineLabels.get(LinedType.HYPERLINK), 7, 2);

        add(otherLabels.get(AuxiliaryType.REF_LINK),    8, 0);
        add(otherLabels.get(AuxiliaryType.DIRECT_LINK), 8, 1);
        add(idLabels,                                   8, 2);

        add(refLabels.get(DirectoryType.FOOTNOTE), 9, 0);
        add(refLabels.get(DirectoryType.ENDNOTE),  9, 1);
        add(refLabels.get(DirectoryType.NOTE),     9, 2);

        add(formatLabels.get(FormatType.BOLD),     10, 0);
        add(formatLabels.get(FormatType.CODED),    10, 1);
        add(otherLabels.get(AuxiliaryType.AGENDA), 10, 2);

        add(formatLabels.get(FormatType.ITALICS),   11, 0);
        add(formatLabels.get(FormatType.UNDERLINE), 11, 1);
        add(otherLabels.get(AuxiliaryType.ESCAPE),  11, 2);
    }

    protected List<CheatsheetLabel> getLabels(){
        return labelList;
    }

    public abstract void updateLabels(ManuscriptDocument doc, int position);
}