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

    List<ExportDivisionTextLine<T>> append(BridgeContent content, boolean first){
        ExportContentText<T> append = new ExportContentText<>(getRender(),
            content);
        ArrayList<ExportDivisionTextLine<T>> children = new ArrayList<>();
        return append(content, first, append, children);
    }

    private List<ExportDivisionTextLine<T>> append(BridgeContent content,
        boolean first, ExportContentText<T> append,
        ArrayList<ExportDivisionTextLine<T>> children
    ){
        T space = getRenderDivision().calcaluteSpace(this, first);
        Optional<ExportContentText<T>> overflow = append.split(space);

        if (overflow.isPresent()){
            ExportDivisionTextLine<T> line =
                new ExportDivisionTextLine<>(getRender());
            children.add(line);
            line.append(content, first, overflow.get(), children);
        }

        return children;
    }


    T getFillWidth(){
        return fillWidth;
    }

    @Override
    protected List<ExportContentText<T>> delegate(){
        return outputContent;
    }

}
