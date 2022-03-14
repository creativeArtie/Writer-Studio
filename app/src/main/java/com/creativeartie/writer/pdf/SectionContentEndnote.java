package com.creativeartie.writer.pdf;

import java.io.*; // IOException
import java.util.*; // Optional

import com.creativeartie.writer.pdf.value.*; // Utilities
import com.creativeartie.writer.lang.markup.*; // LinedSpanPointNote

import static com.creativeartie.writer.main.Checker.*;

/** A {@link SectionContent} for endnotes
 */
class SectionContentEndnote extends SectionContent<LinedSpanPointNote> {
    private int noteNumber;

    /** Only construcutor.
     *
     * @param parent
     *      input parent data
     */
    public SectionContentEndnote(WritingExporter parent) throws IOException{
        super(parent);
        noteNumber = 1;
    }

    @Override
    protected MatterArea parseHeader(WritingFile data) throws IOException{
        return null;
    }

    @Override
    protected DivisionText parseSpan(LinedSpanPointNote span) throws IOException{
        checkNotNull(span, "span");
        Optional<FormattedSpan> text = span.getFormattedSpan();
        if (text.isPresent() && ! text.get().isEmpty()){
            DivisionTextFormatted ans = newFormatDivision();
            ans.appendText(Utilities.toRomanSuperscript(noteNumber),
                newFont().changeToSuperscript());
            noteNumber++;
            return ans.addContent(text.get());
        }
        noteNumber++;
        return null;
    }
}
