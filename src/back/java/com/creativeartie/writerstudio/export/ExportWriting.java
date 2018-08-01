package com.creativeartie.writerstudio.export;

import java.util.*;

/** Renders the entire document*/
public final class ExportWriting<T extends Number>
    extends ExportCollection<T, ExportSection<T>>
{
    private BridgeWriting contentBridge;
    private RenderWriting contentRender;
    private ArrayList<ExportSection<T>> contentExport;

    public ExportWriting(BridgeWriting bridge, RenderWriting<T> render){
        contentBridge = bridge;
        contentRender = render;
        contentExport = new ArrayList<>();
        for (BridgeSection content: bridge){
            contentExport.add(new ExportSection<>(content, render));
        }
    }

    @Override
    List<ExportSection<T>> delegateRaw(){
        return contentExport;
    }
}
