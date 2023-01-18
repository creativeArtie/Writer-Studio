package com.creativeartie.writer.export.markup;

import java.util.*;
import com.creativeartie.writer.export.*;
import com.creativeartie.writer.lang.markup.*;

public abstract class FormatData<T extends FormatSpan> implements ContentData {

    private T targetSpan;

    FormatData(T span){
        targetSpan = span;
    }

    T getTargetSpan(){
        return targetSpan;
    }

    abstract boolean isFootnote();

    public Optional<ContentLine> getFootnote(){
        return Optional.empty();
    }

    public List<DataContentType> getFormats(){
        ArrayList<DataContentType> ans = new ArrayList<>();
        if (targetSpan.isBold()) ans.add(DataContentType.BOLD);
        if (targetSpan.isItalics()) ans.add(DataContentType.ITALICS);
        if (targetSpan.isUnderline()) ans.add(DataContentType.UNDERLINE);
        if (isFootnote()) ans.add(DataContentType.SUPERSRCRIPT);
        return ans;
    }

    public Optional<String> getLinkPath(){
        return Optional.empty();
    }

    public final boolean isKeepLast(){
        return isFootnote();
    }
}
