package com.creativeartie.writer.export;

import java.util.*;

public final class ExportSection<T extends Number>
    extends ExportCollection<ExportPage<T>>
{
    private final ContentSection inputContent;
    private final RenderWriter<T> renderExporter;
    private final ArrayList<ExportPage<T>> outputContent;

    ExportSection(ContentSection input, RenderWriter<T> render){
        inputContent = input;
        renderExporter = render;
        outputContent = new ArrayList<>();
    }

    @Override
    protected List<ExportPage<T>> getChildren(){
        return outputContent;
    }

    protected void render(){
        outputContent.clear();
        ExportPage<T> page = new ExportPage<>(inputContent,
            renderExporter.newPage(inputContent.getPageType())
        );
        outputContent.add(page);
        Optional<ExportPage<T>> overflow = page.render();
        while (overflow.isPresent()){
            outputContent.add(overflow.get());
            overflow = overflow.get().render();
        }
    }

}
