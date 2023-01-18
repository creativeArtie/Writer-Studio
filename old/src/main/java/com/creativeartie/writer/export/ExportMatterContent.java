package com.creativeartie.writer.export;

import java.util.*;

public final class ExportMatterContent<T extends Number> extends ExportMatter<T>{
    private T maxHeight;
    private ExportMatterFootnote<T> exportFootnote;

    public ExportMatterContent(RenderMatter<T> render){
        super(render);
    }

    void setMaxHeight(T height){
        maxHeight = height;
    }

    void setFootnote(ExportMatterFootnote<T> footnote){
        exportFootnote = footnote;
    }

    Optional<ExportLineMain<T>> append(ContentLine line){
        return addLine(new ExportLineMain<>(line, getRenderer().newLine(line
            .getLineType())));
    }

    Optional<ExportLineMain<T>> append(ExportLineMain<T> line){
        return addLine(line);
    }

    private Optional<ExportLineMain<T>> addLine(ExportLineMain<T> lines){
        int i = 0;
        for (ExportLineData<T> line: lines){
            T grand = getFillHeight();
            grand = getRenderer().addHeight(grand, line.getAllHeight(exportFootnote));
            List<ExportLineMain<T>> notes = line.getFootnotes(exportFootnote);
            if (! notes.isEmpty() && exportFootnote.isEmpty()){
                grand = getRenderer().addHeight(grand,
                    getRenderer().getPadding());
            }
            if (getRenderer().canFitHeight(maxHeight, grand)){
                i++;
                exportFootnote.addNotes(notes);
                addFillHeight(grand);
            } else {
                ExportLineMain<T> split = lines.splitAt(i);
                getChildren().add(lines);
                return Optional.of(split);
            }
            getChildren().add(lines);
        }
        return Optional.empty();
    }
}
