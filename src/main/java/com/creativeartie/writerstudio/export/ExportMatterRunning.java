package com.creativeartie.writerstudio.export;

import java.util.*;

/** Export a section of text, like paragraphs and headings. */
final class ExportMatterRunning<T extends Number> extends ExportMatter<T>{

    ExportMatterRunning(BridgeMatter input, RenderMatter<T> render){
        super(render);
        setMaxHeight(render.toZero());
        for (BridgeDivision division: input.getContent()){
            fillContent(
                new ExportDivisionText<>(division, render.getContentRender())
            );
        }
    }
}
