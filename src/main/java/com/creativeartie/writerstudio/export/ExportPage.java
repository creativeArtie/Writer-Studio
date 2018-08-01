package com.creativeartie.writerstudio.export;

import java.util.*;


public final class ExportPage<T extends Number>{

    private final ContentSection inputContent;
    private final RenderPage<T> renderExporter;
    private ExportMatterRunning<T> outputHeader;
    private ExportMatterRunning<T> outputFooter;
    private ExportMatterContent<T> outputContent;
    private ExportMatterFootnote<T> outputFootnote;

    private int contentPointer;
    private Optional<ExportLineMain<T>> overflowLine;

    ExportPage(ContentSection input, RenderPage<T> render){
        inputContent = input;
        renderExporter = render;
        contentPointer = 0;
        overflowLine = Optional.empty();
    }

    private ExportPage(ExportPage<T> self){
        inputContent = self.inputContent;
        renderExporter = self.renderExporter;
        contentPointer = self.contentPointer;
        overflowLine = self.overflowLine;
    }

    Optional<ExportPage<T>> render(){
        OuputPageInfo info = renderExporter.getPageInfo();
        outputHeader = new ExportMatterRunning<>(inputContent.getHeader(info),
            renderExporter.getHeader());
        outputFooter = new ExportMatterRunning<>(inputContent.getFooter(info),
            renderExporter.getFooter());
        outputFootnote = new ExportMatterFootnote<>(renderExporter.getFootnote());
        outputContent = new ExportMatterContent<>(renderExporter.getContent());

        outputHeader.render();
        outputFooter.render();
        outputContent.setMaxHeight(renderExporter.getEmptyHeight(
            outputHeader.getFillHeight(), outputFooter.getFillHeight()
        ));
        outputContent.setFootnote(outputFootnote);

        if (overflowLine.isPresent()){
            overflowLine = outputContent.append(overflowLine.get());
            if (overflowLine.isPresent()){
                return Optional.of(new ExportPage<T>(this));
            }
        }

        for(ContentLine line: inputContent.getContent(info, contentPointer)){
            contentPointer++;
            overflowLine = outputContent.append(line);
            if (overflowLine.isPresent()){
                return Optional.of(new ExportPage<T>(this));
            }
        }
        return null;
    }
}
