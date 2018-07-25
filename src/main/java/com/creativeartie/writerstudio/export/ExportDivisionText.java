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
        ExportDivisionTextLine<T> line = new ExportDivisionTextLine<>(
            getRender());
        outputLines.add(line);
        for(BridgeContent content: contents){
            List<ExportDivisionTextLine<T>> lines = line.append(content,
                outputLines.isEmpty());
            if (! lines.isEmpty()){
                outputLines.addAll(lines);
                line = lines.get(lines.size() - 1);
            }
            System.out.println(this);
        }
        if (line.isFilled()){
            outputLines.add(line);
        }
    }

    @Override
    protected List<ExportDivisionTextLine<T>> delegate(){
        return outputLines;
    }}
