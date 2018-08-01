package com.creativeartie.writerstudio.export;

import java.util.*;

abstract class ExportMatter<T extends Number>
    extends ExportCollection<ExportLineMain<T>>
{
    private final RenderMatter<T> renderExporter;
    private final ArrayList<ExportLineMain<T>> outputContent;
    private T fillHeight;

    public ExportMatter(RenderMatter<T> renderer){
        renderExporter = renderer;
        outputContent = new ArrayList<>();
    }

    RenderMatter<T> getRenderer(){
        return renderExporter;
    }

    @Override
    protected List<ExportLineMain<T>> getChildren(){
        return outputContent;
    }

    void updateHeight(ExportLineMain<T> line){
        fillHeight = renderExporter.addHeight(fillHeight, line.getFillHeight());
    }

    public T getFillHeight(){
        return fillHeight;
    }
}
