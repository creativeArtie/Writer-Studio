package com.creativeartie.writerstudio.export;

import java.util.*;

/** Export a section of text, like paragraphs and headings. */
final class ExportMatterContent<T extends Number> extends ExportMatter<T>{

    private ArrayList<ExportContentText> outputLines;
    private BridgeContent contentBridge;

    ExportMatterContent(RenderMatter<T> render){
        super(render);
    }
}
