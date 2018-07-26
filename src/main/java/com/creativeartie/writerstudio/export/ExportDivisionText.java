package com.creativeartie.writerstudio.export;

import java.util.*;

/** Export a section of text, like paragraphs and headings. */
final class ExportDivisionText<T extends Number>
    extends ExportCollection<T, ExportDivisionTextLine<T>>
{

    private final BridgeDivision spanLine;
    private final ArrayList<ExportDivisionTextLine<T>> outputLines;
    private final RenderDivision<T> lineRender;
    private final DataLineType lineType;

    ExportDivisionText(BridgeDivision line, RenderDivision<T> render){
        spanLine = line;
        outputLines = new ArrayList<>();
        lineType = line.getLineType();
        lineRender = render;
        fillContents(line.getContent());
    }

    private void fillContents(Iterable<BridgeContent> contents){
        assert outputLines.isEmpty(): "outputLines not Empty";
        ExportDivisionTextLine<T> line = new ExportDivisionTextLine<>(
            lineRender);
        outputLines.add(line);
        for (BridgeContent content: contents){
            Optional<ExportContentText<T>> overflow = line.append(content,
                lineType, outputLines.size() <= 1);
            while(overflow.isPresent()){
                line = new ExportDivisionTextLine<>(lineRender);
                outputLines.add(line);
                overflow = line.append(overflow.get());
            }
        }
    }

    @Override
    protected List<ExportDivisionTextLine<T>> delegateRaw(){
        return outputLines;
    }}
