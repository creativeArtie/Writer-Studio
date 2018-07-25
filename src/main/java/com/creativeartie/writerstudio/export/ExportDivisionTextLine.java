package com.creativeartie.writerstudio.export;

import java.util.*;

import com.google.common.collect.*;

/** Creates the render box. */
final class ExportDivisionTextLine<T extends Number>
    extends ForwardingList<ExportContentText<T>> implements ExportBase<T>
{
    private final FactoryRender<T> renderFactory;
    private final ArrayList<ExportContentText<T>> outputContent;
    private T fillWidth;
    private T fillHeight;

    ExportDivisionTextLine(FactoryRender<T> factory){
        outputContent = new ArrayList<>();
        renderFactory = factory;
    }

    boolean isFilled(){
        return ! outputContent.isEmpty();
    }

    Optional<ExportDivisionTextLine<T>> split(BridgeContent content){
        return null;
    }

    @Override
    protected List<ExportContentText<T>> delegate(){
        return outputContent;
    }

    @Override
    public FactoryRender<T> getRender(){
        return renderFactory;
    }
}
