package com.creativeartie.writerstudio.export;

import java.util.*;

/** Export a section of text, like paragraphs and headings. */
final class ExportPage<T extends Number>{

    private ExportMatterRunning sectionHeader;
    private ExportMatterRunning sectionFooter;
    private ExportMatterFootnote sectionFootnote;
    private ExportMatterContent sectionContent;

    ExportPage(BridgeSection content, RenderSection renderer){/*
        sectionHeader = new ExportMatterRunning(content.getHeader(),
            renderer.makeHeaderRender());
        sectionFooter = new ExportMatterRunning(content.getFooter(),
            renderer.makeFooterRender());

        T height = renderer.getRunningHeight(this);

        sectionFootnote = new ExportMatter(renderer.makeFootnoteRender();
        sectionContent = new ExportMatterContent(renderer.makeContentRender());*/
    }

    Optional<ExportPage> fillContent(){
        Optional<ExportPage> overflow = Optional.empty();
        return overflow;
    }
}
