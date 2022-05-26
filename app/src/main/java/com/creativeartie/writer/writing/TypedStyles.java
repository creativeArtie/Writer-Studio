package com.creativeartie.writer.writing;

public enum TypedStyles {
    ID("identifier"), OPERATOR("operator"), NAME("name"), TEXT("text"), ERROR(
        "error"
    ),

    // @orderFor IdGroups
    FOOTNOTE("footnote"), ENDNOTE("endnote"), SOURCE("source"),
    // @endOrder

    TODO("todo"),

    // @orderFor TextPhrase.Word and TextPhrase.Format
    BOLD("bold"), ITALICS("italics"), UNDERLINE("underline"),
    // @endOrder TextPhrase.Word

    ESCAPE("escape"), PARAGRAPH("paragraph");

    private String styleName;

    TypedStyles(String name) {
        styleName = name;
    }

    public String getStyle() {
        return styleName;
    }
}
