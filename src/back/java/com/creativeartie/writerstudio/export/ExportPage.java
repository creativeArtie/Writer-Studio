package com.creativeartie.writerstudio.export;

import java.util.*;

/** Export a section of text, like paragraphs and headings. */
final class ExportPage<T extends Number>{

    private RenderPage<T> contentRender;
    private ExportMatterRunning contentHeader;
    private ExportMatterRunning contentFooter;
    private ExportMatterFootnote contentFootnote;
    private ExportMatterContent contentContent;

    private Iterator<BridgeDivision> childContents;
    private Optional<ExportDivisionText> overflow;
    private T fillHeight;

    private PageInfo pageInfo;

    ExportPage(RenderPage<T> render){
        pageInfo = render.getPagInfo();
    }

    Optional<ExportPage> render(){
        Optional<ExportPage> overflow = Optional.empty();

        PageInfo info = render.getPagInfo();
        sectionHeader = new ExportMatterRunning(bridge.getHeader(info),
            render.newHeader());
        sectionFooter = new ExportMatterRunning(bridge.getFooter(info),
            render.newFooter());

        fillheight = contentRender.calcaluteHeight(this);
        if (childContents == null){
            childContents = bridge.getContent(info);
        }


        return overflow;
    }

    ExportMatterRunning getHeader(){
        return sectionHeader;
    }

    ExportMatterRunning getFooter(){
        return sectionFooter;
    }

    T getFillHeight(){
        return fillHeight;
    }
}
