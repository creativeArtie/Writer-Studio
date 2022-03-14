package com.creativeartie.writer.pdf;

import java.io.*; // IOException

import com.creativeartie.writer.pdf.value.*; // Utilities
import com.creativeartie.writer.lang.markup.*; // FormattedSpan

import static com.creativeartie.writer.main.Checker.*;

/** A {@link SectionContent} for citations
 */
final class SectionContentCite extends SectionContent<FormattedSpan> {

    /** Only construcutor.
     *
     * @param parent
     *      input parent data
     */
    SectionContentCite(WritingExporter parent) throws IOException{
        super(parent);
    }

    /** Add a title to the section.
     *
     * @param data
     *      rendering data
     */
    void addTitle(WritingData data) throws IOException{
        checkNotNull(data, "data");
        addLines(data.getMatter(TextTypeMatter.SOURCE_STARTER));
    }

    @Override
    protected MatterArea parseHeader(WritingFile data) throws IOException{
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
