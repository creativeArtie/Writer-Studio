package com.creativeartie.writerstudio.export;

import java.util.*;

public final class ExportMatterContent<T extends Number> extends ExportMatter<T>{
    private T maxHeight;
    private ExportMatterFootnote exportFootnote;

    public ExportMatterContent(RenderMatter<T> render){
        super(render);
    }

    void setMaxHeight(T height){
        maxHeight = height;
    }

    void setFootnote(ExportMatterFootnote footnote){
        exportFootnote = footnote;
    }

    Optional<ExportLineMain<T>> append(ContentLine line){
        return addLine(new ExportLineMain<>(line, getRenderer().newLine()));
    }

    Optional<ExportLineMain<T>> append(ExportLineMain<T> line){
        return addLine(line);
    }

    private Optional<ExportLineMain<T>> addLine(ExportLineMain<T> lines){
        int i = 0;
        for (ExportLineData line: lines){
            if (render.canFitContent(maxHeight, line)){
                getChildren().add(line);
                exportFootnote.getChildren().addAll(line.getFootnotes());
            } else {
                return Optional.of(lines.splitAt(i)).map(l -> l.refresh());
            }
            i++;
        }
        return Optional.empty();
    }


}
