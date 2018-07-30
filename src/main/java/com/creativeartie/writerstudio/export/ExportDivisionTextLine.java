package com.creativeartie.writerstudio.export;

import java.util.*;

/** Renders a line of text. */
final class ExportDivisionTextLine<T extends Number>
    extends ExportCollection<T, ExportContentText<T>>
{

    private final ArrayList<ExportContentText<T>> outputContent;
    private final RenderDivision<T> outputRender;
    private T fillWidth;
    private T fillHeight;

    ExportDivisionTextLine(RenderDivision<T> render){
        outputRender = render;
        outputContent = new ArrayList<>();
        fillWidth = render.toZero();
        fillHeight = render.toZero();
    }

    boolean isFilled(){
        return ! outputContent.isEmpty();
    }

    Optional<ExportContentText<T>> append(BridgeContent text, DataLineType type,
        boolean first
    ){
        return append(new ExportContentText<>(text, type,outputRender.getContentRender()), first);
    }

    Optional<ExportContentText<T>> append(ExportContentText<T> text){
        return append(text, false);
    }

    private Optional<ExportContentText<T>> append(ExportContentText<T> text,
        boolean first
    ){
        T space = outputRender.calcaluteSpace(this, first);
        Optional<ExportContentText<T>> overflow = text.splitContent(space);
        if (! text.isEmpty()){
            fillWidth = outputRender.calcaluteFill(this, text);
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

    ArrayList<ExportDivisionText<T>> getNotes(){
        ArrayList<ExportDivisionText<T>> notes = new ArrayList<>();
        /*for (ExportContentText<T> text: this){
            text.getFootnote()
                .map(b -> new ExportDivisionText(line,
                .filter(l -> ! notes.contains(l))
                .ifPresent(l -> notes.add(l));
        }*/
        return notes;
    }

    T addHeight(T subTotal){
        if (subTotal == null){
            subTotal = outputRender.toZero();
        }
        fillHeight = outputRender.toZero();
        for (ExportContentText<T> text: this){
            fillHeight = outputRender.compareHeight(this, text.getHeight());
        }
        return outputRender.addRunning(this, subTotal);
    }

    @Override
    protected List<ExportContentText<T>> delegateRaw(){
        return outputContent;
    }

}
