package com.creativeartie.writer.writing;

/**
 * List of css styles for the manuscript. The order is important to switch
 * between patterns and class names.
 *
 * @see    IdTypes#toTypedStyles()
 * @author wai
 */
public enum SpanStyles {
    ID("identifier"), OPERATOR("operator"), NAME("name"), TEXT("text"), ERROR(
        "error"
    ),

    // @orderFor IdTypes#toTypedStyles
    FOOTNOTE("footnote"), ENDNOTE("endnote"), SOURCE("source"),
    // @endOrder

    TODO("todo"),

    // @orderFor LinePhrase.TextPhrase and LinePhrase.Format
    BOLD("bold"), ITALICS("italics"), UNDERLINE("underline"),
    // @endOrder

    ESCAPE("escape"),

    // @orderFor LineTypes
    PARAGRAPH("paragraph");
    // @endOrder

    private String styleName;

    SpanStyles(String name) {
        styleName = name;
    }

    public String getStyle() {
        return styleName;
    }
}
