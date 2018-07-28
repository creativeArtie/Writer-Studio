package com.creativeartie.writerstudio.export;

import java.util.*;

/** Export a section of text, like paragraphs and headings. */
final class ExportMatterFootnote<T extends Number> extends ExportMatter<T>{

    ExportMatterFootnote(RenderMatter<T> render){
        super(render);
    }
}
