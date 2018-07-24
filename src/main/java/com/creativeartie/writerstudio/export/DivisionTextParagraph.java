package com.creativeartie.writerstudio.export;

import java.util.*;

import com.creativeartie.writerstudio.lang.markup.*;

/** Draws contents on the page */
final class DivisionTextParagraph<T extends Number> extends DivisionText<T>{

    private LinedSpanParagraph renderSpan;

    DivisionTextParagraph(RenderDivision<T> renderer, LinedSpanParagraph span){
        super(renderer, span);
        renderSpan = span;
    }

    @Override
    List<FormatSpan> getContent(){
        return renderSpan.getFormattedSpan()
            .map(s -> s.getChildren(FormatSpan.class))
            .orElse(new ArrayList<>());
    }
}
