package com.creativeartie.writerstudio.export;

import java.util.*;

/** Export a section of text, like paragraphs and headings. */
final class ExportDivisionText<T extends Number>
    extends ExportBaseParent<T, ExportDivisionTextLine<T>>
{


    private final BridgeDivision spanLine;
    private final ArrayList<ExportDivisionTextLine<T>> outputLines;

    ExportDivisionText(FactoryRender<T> renderer, BridgeDivision line){
        super(renderer);
        spanLine = line;
        outputLines = new ArrayList<>();
        fillContents(line.getContent());
    }

    private void fillContents(Iterable<BridgeContent> contents){
        assert outputLines.isEmpty(): "outputLines not Empty";
        ExportDivisionTextLine<T> line = new ExportDivisionTextLine<>(
            getRender());
        outputLines.add(line);
        for (BridgeContent content: contents){
            Optional<ExportContentText<T>> overflow = line.append(content,
                outputLines.size() <= 1);
            while(overflow.isPresent()){
                line = new ExportDivisionTextLine<>(getRender());
                outputLines.add(line);
                overflow = line.append(overflow.get());
            }
        }
        System.out.println(this);
    }

    @Override
    protected List<ExportDivisionTextLine<T>> delegate(){
        return outputLines;
    }}
