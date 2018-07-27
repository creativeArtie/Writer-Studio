package com.creativeartie.writerstudio.export;

import java.util.*;

/** Renders a line of text. */
final class ExportDivisionTextLine<T extends Number>
    extends ExportCollection<T, ExportContentText<T>>
{

    private ExportDivisionText exportParent;
    private final ArrayList<ExportContentText<T>> outputContent;
    private final RenderDivision<T> lineRender;
    private T fillWidth;
    private T fillHeight;

    ExportDivisionTextLine(RenderDivision<T> renderer){
        lineRender = renderer;
        outputContent = new ArrayList<>();
        fillWidth = renderer.toZero();
        fillHeight = renderer.toZero();
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
        Optional<ExportContentText<T>> overflow = text.splitContent(space);
        if (! text.isEmpty()){
            fillWidth = lineRender.calcaluteFill(this, text);
            outputContent.add(text);
        }
        return overflow;
    }

    T getFillWidth(){
        return fillWidth;
    }

    T getFillHeight(){
        return fillHeight;
    }

    T addHeight(T subTotal){
        if (subTotal == null){
            subTotal = lineRender.toZero();
        }
        fillHeight = lineRender.toZero();
        for (ExportContentText<T> text: this){
            fillHeight = lineRender.compareHeight(this, text.getHeight());
        }
        return lineRender.addRunning(this, subTotal);
    }

    @Override
    protected List<ExportContentText<T>> delegateRaw(){
        return outputContent;
    }

}
