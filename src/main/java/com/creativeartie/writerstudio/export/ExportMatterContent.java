package com.creativeartie.writerstudio.export;

import java.util.*;

/** Export a section of text, like paragraphs and headings. */
final class ExportMatterContent<T extends Number> extends ExportMatter<T>{
    private RenderSection<T> outputRender;
    private BridgeMatter inputBridge;

    ExportMatterContent(RenderSection<T> renderer){
        outputRender = renderer;
    }
}
