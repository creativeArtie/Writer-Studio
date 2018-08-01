package com.creativeartie.writerstudio.export;

import java.util.*;

public final class ExportLineMain<T extends Number>
    extends ExportCollection<ExportLineData<T>>
{

    private final ContentLine inputContent;
    private final RenderLine<T> renderExporter;
    private T fillHeight;
    private final ArrayList<ExportLineData<T>> outputContent;

    public ExportLineMain(ContentLine input, RenderLine<T> renderer){
        inputContent = input;
        renderExporter = renderer;
        outputContent = new ArrayList<>();
    }

    void render(){
    }

    T getFillHeight(){
        return fillHeight;
    }

    @Override
    protected List<ExportLineData<T>> getChildren(){
        return outputContent;
    }
}
