package com.creativeartie.writerstudio.export;

import java.util.*;

/** Export a part of a page like the header and footer. */
abstract class ExportMatter<T extends Number>
    extends ExportCollection<T, ExportDivisionText<T>>
{
    private T fillHeight;
    private ArrayList<ExportDivisionText<T>> lineList;

    ExportMatter(){
        lineList = new ArrayList<>();
    }

    List<ExportDivisionText<T>> fillContents(ExportDivisionText<T> content,
        T height
    ){
        ArrayList<ExportDivisionText<T>> overflow = new ArrayList<>();

        return overflow;
    }

    @Override
    protected List<ExportDivisionText<T>> delegateRaw(){
        return lineList;
    }
}
