package com.creativeartie.writer.lang.markup;

import com.creativeartie.writer.lang.*;

import static com.creativeartie.writer.lang.markup.AuxiliaryData.*;

/** Categories of catalogue directories.
 *
 * The value order is set by:
 * <ul>
 * <li>{@link DirectoryParser#getRefParser(DirectoryType)}</li>
 * <li> {@link DirectoryParser#getIDParser(DirectoryType)}</li>
 * <li> {@link LinedParsePointer#parse(SetupPointer)}</li>
 * </ul>
 */
public enum DirectoryType{
    /** Category for id less note cards. */
    NOTE(TYPE_NOTE),
    /** Category for ided note cards. */
    RESEARCH(TYPE_RESEARCH),
    /** Category for footnotes. */
    FOOTNOTE(TYPE_FOOTNOTE),
    /** Category for endnotes. */
    ENDNOTE(TYPE_ENDNOTE),
    /** Category for hyperlinks and bookmarks. */
    LINK(TYPE_LINK);

    /** The first and upper most category. */
    private final String baseCategory;

    /** Creates a {@link DirectoryType}
     *
     * @param category
     *      base category text
     */
    private DirectoryType(String category){
        assert category != null;
        baseCategory = category;
    }

    /** Gets the base category.
     *
     * @return answer
     * @see DirectorySpan#buildId();
     */
    String getCategory(){
        return baseCategory;
    }
}
