package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;

import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writerstudio.main.ParameterChecker.*;

/** Implements {@code design/ebnf.txt Directory}.
 *
 * The value order is required through:
 * <ul>
 * <li>{@link #getRefParser(DirectoryType}</li>
 * <li> {@link #getIDParser(DirectoryType}</li>
 * </ul>
 */
enum DirectoryParser implements SetupParser{
    /** Pointer  for {@link FormatSpanPointId} or a {@link InfoDataSpanRef}. */
    REF_NOTE(    DirectoryType.RESEARCH, CURLY_END),
    /** Pointer for {@link FormatSpanPointId}. */
    REF_FOOTNOTE(DirectoryType.FOOTNOTE, CURLY_END),
    /** Pointer for {@link FormatSpanPointId}. */
    REF_ENDNOTE( DirectoryType.ENDNOTE,  CURLY_END),
    /** Pointer for {@link FormatSpanLinkRef}. */
    REF_LINK(    DirectoryType.LINK,     LINK_TEXT, LINK_END),
    /** Pointer for {@link NoteCard}. */
    ID_NOTE(     DirectoryType.RESEARCH, DIRECTORY_END),
    /** Pointer for {@link LinedSpanNote footnote}. */
    ID_FOOTNOTE( DirectoryType.FOOTNOTE, DIRECTORY_END),
    /** Pointer for {@link LinedSpanNote endnote}.  */
    ID_ENDNOTE(  DirectoryType.ENDNOTE,  LINED_DATA),
    /** Pointer for {@link LinedSpanPointLink}. */
    ID_LINK(     DirectoryType.LINK,     LINED_DATA),
    /** Pointer for {@link LinedSpanLevelSection}. */
    ID_BOOKMARK( DirectoryType.LINK,     DIRECTORY_END, EDITION_BEGIN);

    private static final int ID_SHIFT = 3;

    /** Gets a reference {@link DirectoryParser}.
     *
     * @param type
     *      reference type
     * @return answer
     * @see FormatParsePointId#FormatParsePointId(DirectoryType, String, boolean[])
     */
    static DirectoryParser getRefParser(DirectoryType type){
        argumentNotEnum(type, "type", DirectoryType.NOTE);
        return values()[type.ordinal() - 1];
    }

    /** Gets a id {@link DirectoryParser}.
     *
     * @param type
     *      id type
     * @return answer
     * @see LinedParsePointer#parseCommon(SetupPointer, List)
     */
    static DirectoryParser getIDParser(DirectoryType type){
        argumentNotEnum(type, "type", DirectoryType.NOTE);
        return values()[type.ordinal() + ID_SHIFT];
    }

    /// Shows how to end a text
    private final ContentParser idContent;
    private final String[] reparseEnders;

    /// Adds a root category to differentiate footnote, links, etc
    private final DirectoryType idType;

    /** Creates a {@linkplain DirectoryParser}.
     *
     * @param type
     *      directory type
     * @param enders
     *      span enders
     */
    private DirectoryParser(DirectoryType type, String ... enders){
        argumentNotNull(type, "type");
        argumentNotNull(enders, "enders");

        idType = type;

        /// two version required: for parsing and for reparsing
        /// DIRECTORY_CATEGORY is needed for parsing and unwanted in the other
        String[] init = new String[enders.length + 1];
        System.arraycopy(enders, 0, init, 0, enders.length);
        init[enders.length] = DIRECTORY_CATEGORY;

        idContent = new ContentParser(SpanLeafStyle.ID, init);
        reparseEnders = enders;
    }

    /** Check if the text can be parse at Directory level.
     *
     * @param text
     *      new text
     * @return answer
     */
    boolean canParse(String text){
        argumentNotNull(text, "text");
        return AuxiliaryChecker.notCutoff(text, reparseEnders);
    }

    @Override
    public Optional<SpanBranch> parse(SetupPointer pointer){
        argumentNotNull(pointer, "pointer");

        /// Setup for DirectorySpan
        ArrayList<Span> children = new ArrayList<>();

        /// Extract category & id
        boolean more;
        do {
            idContent.parse(pointer, children);

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
