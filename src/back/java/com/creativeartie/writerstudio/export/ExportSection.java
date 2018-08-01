package com.creativeartie.writerstudio.export;

import java.util.*;

/** Export a part of a page like the header and footer. */
final class ExportSection<T extends Number>
    extends ExportCollection<T, ExportPage<T>>
{
    private BridgeSection contentBridge;
    private RenderWriting<T> contentRender;
    private ArrayList<ExportPage<T>> contentExport;

    public ExportSection(BridgeSection bridge, RenderWriting<T> render){
        contentBridge = bridge;
        contentRender = render;
        contentExport = new ArrayList<>();

        DataPageType type = bridge.getPageType();

        RenderPage<T> page = render.newPage(this, type);
        ExportPage<T> export = new ExportPage<T>(page);
        contentExport.add(export);

        while (overflow.isPresent()){
            contentExport.add(overflow.get());
            overflow = overflow.render();
        }
    }

    @Override
    List<ExportPage<T>> delegateRaw(){
        return contentExport;
    }
}
