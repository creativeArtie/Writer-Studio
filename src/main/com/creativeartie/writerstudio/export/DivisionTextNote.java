package com.creativeartie.writerstudio.export;

import java.io.*; // IOException

import com.creativeartie.writerstudio.export.value.*; // ContentFont

/**
 * A {@link Division} of text for {@link PageFootnote}.
 */
class DivisionTextNote extends DivisionTextFormatted{

    private final ContentFont superFont;

    /** Only constructor
     * @param content
     *  the parent content
     */
    DivisionTextNote(SectionContent<?> content){
        super(content);
        superFont = content.newFont().changeToSuperscript();
    }

    /** Set or change the numbering text.
     * If the first text is not a superscript do not set it.
     *
     * @param text
     *      the text to set
     * @return self
     * @throws IOException
     *      from {@link DivisionText#appendText(String, ContentFont)
     */
    DivisionTextNote setNumbering(String text) throws IOException{
        /// get(0) = DivisionLine.Line
        /// get(0).get(0) = ContentText

        if (isEmpty() || get(0).isEmpty()){
            appendText(text, superFont);
            return this;
        }
        if (get(0).get(0).getFont().equals(superFont)){
            get(0).get(0).setText(text);
        }
        return this;
    }
}