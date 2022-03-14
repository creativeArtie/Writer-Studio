package com.creativeartie.writer.export;

import java.util.*;

public final class ExportWriter<T extends Number>
    extends ExportCollection<ExportSection<T>>
{
    private final ContentWriter inputContent;
    private final RenderWriter<T> renderExporter;
    private final ArrayList<ExportSection<T>> outputContent;

    public ExportWriter(ContentWriter input, RenderWriter<T> renderer){
        inputContent = input;
        renderExporter = renderer;
        outputContent = new ArrayList<>();
    }

    @Override
    protected List<ExportSection<T>> getChildren(){
        return outputContent;
    }

    public void render(){
        outputContent.clear();
        for (ContentSection content: inputContent){
            ExportSection<T> output = new ExportSection<>(content, renderExporter);
            outputContent.add(output);
            output.render();
        }
    }
}
