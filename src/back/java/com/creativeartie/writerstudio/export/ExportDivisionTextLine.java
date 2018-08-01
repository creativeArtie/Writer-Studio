package com.creativeartie.writerstudio.export;

import java.util.*;

/** Renders a line of text. */
final class ExportDivisionTextLine<T extends Number>
    extends ExportCollection<T, ExportContentText<T>>
{

    private final ArrayList<ExportContentText<T>> contentBridge;
    private final RenderDivision<T> contentRender;
    private T fillWidth;
    private T fillHeight;

    ExportDivisionTextLine(RenderDivision<T> render){
        contentRender = render;
        contentBridge = new ArrayList<>();
        fillWidth = render.toZero();
        fillHeight = render.toZero();
    }

    boolean isFilled(){
        return ! contentBridge.isEmpty();
    }

    Optional<ExportContentText<T>> append(BridgeContent text, DataLineType type,
        boolean first
    ){
        return append(new ExportContentText<>(text, type,contentRender.getContentRender()), first);
    }

    Optional<ExportContentText<T>> append(ExportContentText<T> text){
        return append(text, false);
    }

    private Optional<ExportContentText<T>> append(ExportContentText<T> text,
        boolean first
    ){
        T space = contentRender.calcaluteSpace(this, first);
        Optional<ExportContentText<T>> overflow = text.splitContent(space);
        if (! text.isEmpty()){
            fillWidth = contentRender.calcaluteFill(this, text);
            contentBridge.add(text);
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
            subTotal = contentRender.toZero();
        }
        fillHeight = contentRender.toZero();
        for (ExportContentText<T> text: this){
            fillHeight = contentRender.compareHeight(this, text.getHeight());
        }
        return contentRender.addRunning(this, subTotal);
    }

    @Override
    protected List<ExportContentText<T>> delegateRaw(){
        return contentBridge;
    }

}
