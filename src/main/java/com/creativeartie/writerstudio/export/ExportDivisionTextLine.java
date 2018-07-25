package com.creativeartie.writerstudio.export;

import java.util.*;

/** Renders a line of text. */
final class ExportDivisionTextLine<T extends Number>
    extends ExportBaseParent<T, ExportContentText<T>>
{

    private final ArrayList<ExportContentText<T>> outputContent;
    private T fillWidth;
    private T fillHeight;

    ExportDivisionTextLine(FactoryRender<T> renderer){
        super(renderer);
        outputContent = new ArrayList<>();
        fillWidth = renderer.getZero();
    }

    boolean isFilled(){
        return ! outputContent.isEmpty();
    }

    Optional<ExportContentText<T>> append(BridgeContent text, boolean first){
        return append(new ExportContentText<>(getRender(), text), first);
    }

    Optional<ExportContentText<T>> append(ExportContentText<T> text){
        return append(text, false);
    }

    private Optional<ExportContentText<T>> append(ExportContentText<T> text,
        boolean first
    ){
        T space = getRenderDivision().calcaluteSpace(this, first);
        Optional<ExportContentText<T>> overflow = text.split(space);
        if (! text.isEmpty()){
            fillWidth = getRenderDivision().calcaluteFill(this, text);
            outputContent.add(text);
        }
        return overflow;
    }


    T getFillWidth(){
        return fillWidth;
    }

    @Override
    protected List<ExportContentText<T>> delegate(){
        return outputContent;
    }

}
