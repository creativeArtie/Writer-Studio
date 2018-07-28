package com.creativeartie.writerstudio.export;

import java.util.*;

/** Export a section of text, like paragraphs and headings. */
final class ExportMatterContent<T extends Number> extends ExportMatter<T>{

    ExportMatterContent(RenderMatter<T> renderer){
        super(renderer);
    }
}
