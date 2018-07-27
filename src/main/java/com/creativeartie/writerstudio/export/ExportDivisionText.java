package com.creativeartie.writerstudio.export;

import java.util.*;

/** Export a section of text, like paragraphs and headings. */
final class ExportDivisionText<T extends Number>
    extends ExportCollection<T, ExportDivisionTextLine<T>>
{

    private final BridgeDivision spanLine;
    private final RenderDivision<T> lineRender;

    private final ArrayList<ExportDivisionTextLine<T>> outputLines;
    private final boolean hasStart;

    private final DataLineType lineType;

    ExportDivisionText(BridgeDivision line, RenderDivision<T> render){
        spanLine = line;
        outputLines = new ArrayList<>();
        lineType = line.getLineType();
        lineRender = render;
        fillContents(line.getContent());
        hasStart = true;
    }

    private void fillContents(Iterable<BridgeContent> contents){
        assert outputLines.isEmpty(): "outputLines not Empty";
        ExportDivisionTextLine<T> line = new ExportDivisionTextLine<>(
            lineRender);
        outputLines.add(line);
        for (BridgeContent content: contents){
            Optional<ExportContentText<T>> overflow = line.append(content,
                lineType, hasStart && outputLines.size() <= 1);
            while(overflow.isPresent()){
                line = new ExportDivisionTextLine<>(lineRender);
                outputLines.add(line);
                overflow = line.append(overflow.get());
            }
        }
        outputLines.removeIf(l -> l.isEmpty());
    }

    T getHeight(){
        T running = lineRender.toZero();
        for (ExportDivisionTextLine<T> child: outputLines){
            running = child.addHeight(running);
        }
        return running;
    }

    @Override
    protected List<ExportDivisionTextLine<T>> delegateRaw(){
        return outputLines;
    }}
