package com.creativeartie.writerstudio.export;

import java.util.*;

/** Export a section of text, like paragraphs and headings. */
final class ExportMatterRunning<T extends Number> extends ExportMatter<T>{

    ExportMatterRunning(BridgeMatterRunning bridge, RenderMatter<T> render){
        super(render);
        for (BridgeDivision division: bridge){
            fillContent(
                new ExportDivisionText<>(division, render.getContentRender())
            );
        }
    }
}
