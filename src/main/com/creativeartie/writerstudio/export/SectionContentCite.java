package com.creativeartie.writerstudio.export;

import java.io.*; // IOException

import com.creativeartie.writerstudio.export.value.*; // Utilities
import com.creativeartie.writerstudio.file.*; // ManuscriptFile
import com.creativeartie.writerstudio.lang.markup.*; // FormattedSpan

import static com.creativeartie.writerstudio.main.Checker.*;

/** A {@link SectionContent} for citations
 */
final class SectionContentCite extends SectionContent<FormattedSpan> {

    /** Only construcutor.
     *
     * @param parent
     *      input parent data
     * @throws IOException
     *      exceptions thrown from uses of other classes
     */
    SectionContentCite(WritingExporter parent) throws IOException{
        super(parent);
    }

    /** Add a title to the section.
     *
     * @param data
     *      rendering data
     * @throws IOException
     *      exceptions thrown from uses of other classes
     */
    void addTitle(WritingData data) throws IOException{
        checkNotNull(data, "data");
        addLines(data.getPrint(TextDataType.Area.SOURCE_TITLE));
    }

    @Override
    protected MatterArea parseHeader(ManuscriptFile data) throws IOException{
        return null;
    }

    @Override
    protected DivisionText parseSpan(FormattedSpan span) throws IOException{
        checkNotNull(span, "span");
        DivisionTextFormatted line = newFormatDivision();
        line.setIndent(Utilities.inchToPoint(0.5f));
        return line.addContent(span);
    }
}