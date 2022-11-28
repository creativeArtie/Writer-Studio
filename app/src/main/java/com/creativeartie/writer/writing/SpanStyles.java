package com.creativeartie.writer.writing;

public enum SpanStyles {
    ID("identifier"), OPERATOR("operator"), NAME("name"), TEXT("text"), ERROR(
        "error"
    ),

    // @orderFor IdTypes
    FOOTNOTE("footnote"), ENDNOTE("endnote"), SOURCE("source"),
    // @endOrder

    TODO("todo"),

    // @orderFor LinePhrase.TextPhrase and LinePhrase.Format
    BOLD("bold"), ITALICS("italics"), UNDERLINE("underline"),
    // @endOrder

    ESCAPE("escape"), PARAGRAPH("paragraph");

    private String styleName;

    SpanStyles(String name) {
        styleName = name;
    }

    public String getStyle() {
        return styleName;
    }
}
