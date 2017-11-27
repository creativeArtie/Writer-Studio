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

abstract class PaneCheatsheetView extends GridPane{

    private TreeMap<LinedType, PaneCheatsheetLabel> lines;
    private TreeMap<FormatType, PaneCheatsheetLabel> formats;
    private TreeMap<EditionType, PaneCheatsheetLabel> editions;
    private TreeMap<InfoFieldType, PaneCheatsheetLabel> infos;
    private TreeMap<AuxiliaryType, PaneCheatsheetLabel> others;
    private TreeMap<DirectoryType, PaneCheatsheetLabel> idTypes;
    private PaneCheatsheetLabel id;
    private ArrayList<PaneCheatsheetLabel> labelList;

    PaneCheatsheetView(){
        labelList = new ArrayList<>();
        lines = new TreeMap<>();
        PaneCheatsheetLabel holder;
        for(LinedType type: LinedType.values()){
            holder = PaneCheatsheetLabel.getLabel(type);
            lines.put(type, holder);
            labelList.add(holder);
        }

        formats = new TreeMap<>();
        for(FormatType type: FormatType.values()){
            holder = PaneCheatsheetLabel.getLabel(type);
            formats.put(type, holder);
            labelList.add(holder);
        }

        editions = new TreeMap<>();
        for (EditionType type: EditionType.values()){
            if (type != EditionType.NONE){
                holder = PaneCheatsheetLabel.getLabel(type);
                editions.put(type, holder);
                labelList.add(holder);
            }
        }

        infos = new TreeMap<>();
        for (InfoFieldType type: InfoFieldType.values()){
            if (type != InfoFieldType.ERROR){
                holder = PaneCheatsheetLabel.getLabel(type);
                infos.put(type, holder);
                labelList.add(holder);
            }
        }

        idTypes = new TreeMap<>();
        for (DirectoryType type: DirectoryType.values()){
            if (type != DirectoryType.COMMENT && type != DirectoryType.LINK){
                holder = PaneCheatsheetLabel.getLabel(type);
                idTypes.put(type, holder);
                labelList.add(holder);
            }
        }

        id = PaneCheatsheetLabel.getIdentityLabel();
        labelList.add(id);

        others = new TreeMap<>();
        for (AuxiliaryType type: AuxiliaryType.getFormatTypes()){
            holder = PaneCheatsheetLabel.getLabel(type);
            others.put(type, holder);
            labelList.add(holder);
        }
        layoutLabels();
    }

    private void layoutLabels(){
        add(lines.get(LinedType.PARAGRAPH), 0, 0);
        add(lines.get(LinedType.QUOTE),     0, 1);
        add(lines.get(LinedType.AGENDA),    0, 2);

        add(lines.get(LinedType.NUMBERED), 1, 0);
        add(lines.get(LinedType.BULLET),   1, 1);
        add(lines.get(LinedType.BREAK),    1, 2);

        add(lines.get(LinedType.HEADING),    2, 0, 3, 1);
        add(lines.get(LinedType.OUTLINE),    2, 1, 3, 1);
        add(editions.get(EditionType.STUB),  2, 2);
        add(editions.get(EditionType.DRAFT), 3, 2);
        add(editions.get(EditionType.FINAL), 4, 2);

        add(lines.get(LinedType.NOTE),    5, 0);
        add(lines.get(LinedType.SOURCE),  5, 1);
        add(editions.get(EditionType.OTHER), 5, 2);

        add(infos.get(InfoFieldType.SOURCE),   6, 0);
        add(infos.get(InfoFieldType.IN_TEXT),  6, 1);
        add(infos.get(InfoFieldType.FOOTNOTE), 6, 2);


        add(lines.get(LinedType.FOOTNOTE),  7, 0);
        add(lines.get(LinedType.ENDNOTE),   7, 1);
        add(lines.get(LinedType.HYPERLINK), 7, 2);

        add(others.get(AuxiliaryType.REF_LINK),    8, 0);
        add(others.get(AuxiliaryType.DIRECT_LINK), 8, 1);
        add(id,                                    8, 2);

        add(idTypes.get(DirectoryType.FOOTNOTE), 9, 0);
        add(idTypes.get(DirectoryType.ENDNOTE),  9, 1);
        add(idTypes.get(DirectoryType.NOTE),     9, 2);

        add(formats.get(FormatType.BOLD),     10, 0);
        add(formats.get(FormatType.CODED),    10, 1);
        add(others.get(AuxiliaryType.AGENDA), 10, 2);

        add(formats.get(FormatType.ITALICS),   11, 0);
        add(formats.get(FormatType.UNDERLINE), 11, 1);
        add(others.get(AuxiliaryType.ESCAPE),  11, 2);
    }

    protected List<PaneCheatsheetLabel> getLabels(){
        return labelList;
    }

    public abstract void updateLabels(ManuscriptDocument doc, int position);
}