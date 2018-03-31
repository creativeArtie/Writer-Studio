package com.creativeartie.writerstudio.export;

import java.io.*;
import java.util.*;
import java.awt.*;

import org.apache.pdfbox.pdmodel.*;

import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.*;
import com.creativeartie.writerstudio.file.*;
import com.creativeartie.writerstudio.resource.*;
import com.creativeartie.writerstudio.export.value.*;

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
    protected DivisionLine parseSpan(LinedSpanPointNote span) throws IOException{
        Optional<FormatSpanMain> text = span.getFormattedSpan();
        if (text.isPresent() && ! text.get().isEmpty()){
            DivisionLineFormatted ans = newFormatDivision();
            ans.appendSimpleText(Utilities.toRomanSuperscript(noteNumber),
                newFont().changeToSuperscript());
            return ans.addContent(text.get());
        }
        noteNumber++;
        return null;
    }
}