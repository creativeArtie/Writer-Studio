package com.creativeartie.writerstudio.export;

import java.util.*;

import com.creativeartie.writerstudio.lang.markup.*;

/** Draws contents on the page */
abstract class ContentText<T extends Number> extends Content<T>{

    private final RenderContentFont<T> contentFont;
    private final FormatSpan renderSpan;

    ContentText(RenderContentFont<T> font, FormatSpan span){
        super(font);
        renderSpan = span;
        contentFont = font;
    }

    ContentText(ContentText<T> content){
        super(content.contentFont);
        renderSpan = content.renderSpan;
        contentFont = content.contentFont;
    }

    /** Check if text is bold.
     *
     * @return answer
     */
    final boolean isBold(){
        return renderSpan.isBold();
    }

    /** Check if text is itlalics.
     *
     * @return answer
     */
    final boolean isItalics(){
        return renderSpan.isItalics();
    }

    /** Check if text is underlined.
     *
     * @return answer
     */
    final boolean isUnderline(){
        return renderSpan.isUnderline();
    }

    /** Check if text is coded.
     *
     * @return answer
     */
    final boolean isCoded(){
        return renderSpan.isCoded();
    }

    final boolean isSuperscript(){
        return renderSpan instanceof FormatSpanPointId;
    }

    abstract String getText();

    @Override
    final T getWidth(){
        return contentFont.getWidth(getText());
    }

    @Override
    final T getHeight(){
        return contentFont.getHeight(getText());
    }

    final Optional<ContentText<T>> fitText(T width){
        String text = getText();
        return contentFont.splitText(text, width)
            .map(s -> splitText(s[1]));
    }

    abstract ContentText<T> splitText(String text);
}
