package com.creativeartie.writerstudio.export;

import java.util.*;

/** Renders a line of text. */
final class ExportDivisionTextLine<T extends Number>
    extends ExportCollection<T, ExportContentText<T>>
{

    private final ArrayList<ExportContentText<T>> outputContent;
    private final RenderDivision<T> lineRender;
    private T fillWidth;
    private T fillHeight;

    ExportDivisionTextLine(RenderDivision<T> renderer){
        lineRender = renderer;
        outputContent = new ArrayList<>();
        fillWidth = renderer.getZero();
    }

    boolean isFilled(){
        return ! outputContent.isEmpty();
    }

    Optional<ExportContentText<T>> append(BridgeContent text, DataLineType type,
        boolean first
    ){
        return append(new ExportContentText<>(text, type,lineRender.getContentRender()), first);
    }

    Optional<ExportContentText<T>> append(ExportContentText<T> text){
        return append(text, false);
    }

    private Optional<ExportContentText<T>> append(ExportContentText<T> text,
        boolean first
    ){
        T space = lineRender.calcaluteSpace(this, first);
        Optional<ExportContentText<T>> overflow = text.split(space);
        if (! text.isEmpty()){
            fillWidth = lineRender.calcaluteFill(this, text);
            outputContent.add(text);
        }
        return overflow;
    }


    T getFillWidth(){
        return fillWidth;
    }

    @Override
    protected List<ExportContentText<T>> delegateRaw(){
        return outputContent;
    }

}
