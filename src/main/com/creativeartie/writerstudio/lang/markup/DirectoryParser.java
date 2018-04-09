package com.creativeartie.writerstudio.lang.markup;

import java.util.*; // ArrayList, Optional

import com.creativeartie.writerstudio.lang.*; // (many)

import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writerstudio.main.Checker.*;

/**
 * Parser for {@link DirectorySpan}.
 */
enum DirectoryParser implements SetupParser{
    REF_NOTE(DirectoryType.NOTE, CURLY_END),
    REF_FOOTNOTE(DirectoryType.FOOTNOTE, CURLY_END),
    REF_ENDNOTE(DirectoryType.ENDNOTE, CURLY_END),
    REF_LINK(DirectoryType.LINK, LINK_TEXT, LINK_END),
    ID_NOTE(DirectoryType.NOTE, DIRECTORY_END),
    ID_FOOTNOTE(DirectoryType.FOOTNOTE, DIRECTORY_END),
    ID_ENDNOTE(DirectoryType.ENDNOTE, LINED_DATA),
    ID_LINK(DirectoryType.LINK, LINED_DATA),
    ID_BOOKMARK(DirectoryType.LINK, DIRECTORY_END, EDITION_BEGIN);

    private static final int ID_SHIFT = 3;

    public static DirectoryParser getRefParser(DirectoryType type){
        if (type == DirectoryType.COMMENT){
            throw new IllegalArgumentException("No parser for Comments.");
        }
        return values()[type.ordinal() - 1];
    }

    public static DirectoryParser getIDParser(DirectoryType type){
        if (type == DirectoryType.COMMENT){
            throw new IllegalArgumentException("No parser for Comments.");
        }
        return values()[type.ordinal() + ID_SHIFT];
    }

    /// Shows how to end a text
    private final ContentParser idContent;
    private final String[] reparseEnders;

    /// Adds a root category to differentiate footnote, links, etc
    private final DirectoryType idType;

    private DirectoryParser(DirectoryType type, String ... enders){
        checkNotNull(enders, "enders");
        checkNotNull(type, "type");

        idType = type;

        /// adding DIRECTORY_CATEGORY into spanEnders by copying into new array
        String[] init = new String[enders.length + 1];
        System.arraycopy(enders, 0, init, 0, enders.length);
        init[enders.length] = DIRECTORY_CATEGORY;
        reparseEnders = enders;

        idContent = new ContentParser(StyleInfoLeaf.ID, init);
    }

    /** Check if the text can be parse at Directory level. */
    boolean canParse(String text){
        checkNotNull(text, "text");
        return AuxiliaryChecker.notCutoff(text, Arrays.asList(reparseEnders));
    }

    @Override
    public Optional<SpanBranch> parse(SetupPointer pointer){
        checkNotNull(pointer, "pointer");

        /// Setup for DirectorySpan
        ArrayList<Span> children = new ArrayList<>();

        /// Extract category & id
        boolean more;
        do {
            idContent.parse(children, pointer);

            /// last current is not an id but a category
            more = pointer.startsWith(children, DIRECTORY_CATEGORY);
        } while (more);

        /// Create span if there are Span(s) extracted
        if (children.size() > 0) {
            DirectorySpan ans = new DirectorySpan(children, idType, this);
            return Optional.of(ans);
        }
        return Optional.empty();
    }
}
