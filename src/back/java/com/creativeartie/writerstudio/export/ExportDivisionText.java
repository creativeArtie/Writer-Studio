package com.creativeartie.writerstudio.export;

import java.util.*;

/** Export a section of text, like paragraphs and headings. */
final class ExportDivisionText<T extends Number>
    extends ExportCollection<T, ExportDivisionTextLine<T>>
{

    private final BridgeDivision contentBridge;
    private final RenderDivision<T> contentRender;

    private final ArrayList<ExportDivisionTextLine<T>> outputLines;
    private final boolean hasStart;

    private final DataLineType lineType;

    ExportDivisionText(BridgeDivision bridge, RenderDivision<T> render){
        contentBridge = bridge;
        outputLines = new ArrayList<>();
        lineType = bridge.getLineType();
        contentRender = render;
        fillContents(bridge);
        hasStart = true;
    }

    private void fillContents(Iterable<BridgeContent> contents){
        assert outputLines.isEmpty(): "outputLines not Empty";
        ExportDivisionTextLine<T> line = new ExportDivisionTextLine<>(
            contentRender);
        outputLines.add(line);
        for (BridgeContent content: contents){
            Optional<ExportContentText<T>> overflow = line.append(content,
                lineType, hasStart && outputLines.size() <= 1);
            while(overflow.isPresent()){
                line = new ExportDivisionTextLine<>(contentRender);
                outputLines.add(line);
                overflow = line.append(overflow.get());
            }
        }
        outputLines.removeIf(l -> l.isEmpty());
    }

    T getFillHeight(){
        T running = contentRender.toZero();
        for (ExportDivisionTextLine<T> child: outputLines){
            running = child.addHeight(running);
        }
        return running;
    }

    Optional<ExportDivisionText<T>> split(T height, boolean footnotes){
        ArrayList<ExportDivisionTextLine<T>> overflow = new ArrayList<>();
        /*T running = contentRender.toZero();
        for (ExportDivisionTextLine<T> child: outputLines){
            T size = child.addHeight(running);
        }*/
        return null;

    }

    @Override
    protected List<ExportDivisionTextLine<T>> delegateRaw(){
        return outputLines;
    }}
