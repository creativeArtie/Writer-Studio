package com.creativeartie.writerstudio.export;

import java.util.*;

/** Export a section of text, like paragraphs and headings. */
abstract class ExportMatter<T extends Number>
    extends ExportCollection<T, ExportDivisionText<T>>
{
    private T fillHeight;
    private ArrayList<ExportDivisionText> lineList;

    ExportMatter(BridgeMatter content, RenderMatter<T> render){
        lineList = new ArrayList<>();

    }

    private void fillContents(Iterable<BridgeMatter> content){

    }
}
