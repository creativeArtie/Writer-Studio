package com.creativeartie.writerstudio.export;

import java.util.*;

import com.google.common.collect.*;

/** Export a section of text, like paragraphs and headings. */
final class ExportDivisionText<T extends Number>
    extends ForwardingList<ExportDivisionTextLine<T>> implements ExportBase<T>
{

    private final FactoryRender<T> renderFactory;
    private final BridgeDivision spanLine;
    private final ArrayList<ExportDivisionTextLine<T>> outputLines;

    ExportDivisionText(FactoryRender<T> factory, BridgeDivision line){
        renderFactory = factory;
        spanLine = line;
        outputLines = new ArrayList<>();
        fillContents(line.getContent());
    }

    private void fillContents(Iterable<BridgeContent> contents){
        ExportDivisionTextLine<T> line = new ExportDivisionTextLine<>(
            renderFactory);
        for(BridgeContent content: contents){
            Optional<ExportDivisionTextLine<T>> overflow = line.split(content);
            if (overflow.isPresent()){
                outputLines.add(line);
                line = overflow.get();
            }
        }
        if (line.isFilled()){
            outputLines.add(line);
        }
    }

    @Override
    protected List<ExportDivisionTextLine<T>> delegate(){
        return outputLines;
    }

    @Override
    public FactoryRender<T> getRender(){
        return renderFactory;
    }
}
