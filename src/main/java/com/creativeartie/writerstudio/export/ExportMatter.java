package com.creativeartie.writerstudio.export;

import java.util.*;

/** Export a section of text, like paragraphs and headings. */
abstract class ExportMatter<T extends Number>
    extends ExportCollection<T, ExportDivisionText<T>>
{
    private T fillHeight;

    ExportMatter(){
    }
}
