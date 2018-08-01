package com.creativeartie.writerstudio.export;

import java.util.*;

/** Export a part of a page like the header and footer. */
abstract class ExportMatter<T extends Number>
    extends ExportCollection<T, ExportDivisionText<T>>
{
    private final RenderMatter<T> contentRender;

    private Optional<T> maxHeight;
    private T fillHeight;
    private ArrayList<ExportDivisionText<T>> lineList;

    ExportMatter(RenderMatter<T> render){
        contentRender = render;
        lineList = new ArrayList<>();
        maxHeight = Optional.empty();
    }

    Optional<T> getMaxHeight(){
        return maxHeight;
    }

    void setMaxHeight(Optional<T> height){
        maxHeight = height;
    }

    T getFillHeight(){
        return fillHeight;
    }

    void setFillHeight(T height){
        fillHeight = height;
    }

    boolean fillContent(ExportDivisionText<T> content){
        T adding = content.getFillHeight();
        if(contentRender.toZero().equals(maxHeight) ||
            contentRender.canFit(this, adding)
        ){
            lineList.add(content);
            fillHeight = contentRender.addSize(this, adding);
            return true;
        }
        return false;
    }

    void removeLast(int lines){
        lineList.subList(lines, lineList.size()).clear();
    }

    @Override
    protected List<ExportDivisionText<T>> delegateRaw(){
        return lineList;
    }
}
