package com.creativeartie.writerstudio.lang;

/** Information about {@link SpanLeaf}.
 */
public enum SpanLeafStyle{
    /** Type of text for formating and parsing information.*/
    KEYWORD,
    /** Type of text for pointing to or naming a span of text. */
    ID,
    /** Type of text to describe another text.*/
    FIELD,
    /** Type of text with special meaning.*/
    DATA,
    /** Type of text pointing a hyperlink or a local file.*/
    PATH,
    /** Basic text for any other purpose. */
    TEXT;
}
