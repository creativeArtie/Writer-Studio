package com.creativeartie.writerstudio.export;

import java.io.*; // IOException
import java.util.*; // Optional

import com.creativeartie.writerstudio.export.value.*; // Utilities
import com.creativeartie.writerstudio.file.*; // ManuscriptFile
import com.creativeartie.writerstudio.lang.markup.*; // LinedSpanPointNote

public class SectionContentEndnote extends SectionContent<LinedSpanPointNote> {
    private int noteNumber;

    public SectionContentEndnote(WritingExporter parent) throws IOException{
        super(parent);
        noteNumber = 1;
    }

    @Override
    protected MatterArea parseHeader(ManuscriptFile data) throws IOException{
        return null;
    }

    @Override
    protected DivisionText parseSpan(LinedSpanPointNote span) throws IOException{
        Optional<FormattedSpan> text = span.getFormattedSpan();
        if (text.isPresent() && ! text.get().isEmpty()){
            DivisionTextFormatted ans = newFormatDivision();
            ans.appendText(Utilities.toRomanSuperscript(noteNumber),
                newFont().changeToSuperscript());
            return ans.addContent(text.get());
        }
        noteNumber++;
        return null;
    }
}