package com.creativeartie.writerstudio.export;

import java.util.*;

import com.creativeartie.writerstudio.lang.markup.*;

/** Draws contents on the page */
final class DivisionLine<T extends Number> {
    private final RenderDivision<T> divisionRenderer;
    private final ArrayList<ContentText<T>> textChildren;
    private T fillWidth;

    DivisionLine(RenderDivision<T> renderer){
        divisionRenderer = renderer;
        textChildren = new ArrayList<>();
    }

    Optional<ContentText<T>> append(FormatSpan child){
        ContentText text = null;
        if (child instanceof FormatSpanContent){
            text = new ContentTextBasic<>(divisionRenderer.getContentRender(),
                (FormatSpanContent) child);
        }

        assert text != null: "span type not ready: " + child.getClass();

        Optional<ContentText<T>> overflow = text.fitText(fillWidth);
        if (! text.getText().isEmpty()){
            fillWidth = divisionRenderer.newFillWidth(text);
            textChildren.add(text);
        }
        return overflow;
    }
}
