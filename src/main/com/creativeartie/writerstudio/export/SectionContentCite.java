package com.creativeartie.writerstudio.export;

import java.io.*; // IOException

import com.creativeartie.writerstudio.export.value.*; // Utilities
import com.creativeartie.writerstudio.file.*; // ManuscriptFile
import com.creativeartie.writerstudio.lang.markup.*; // FormattedSpan

public class SectionContentCite extends SectionContent<FormattedSpan> {

    public SectionContentCite(WritingExporter parent) throws IOException{
        super(parent);
        ContentFont font = newFont().changeBold(true).changeSize(16);
        addLine(new DivisionText(getPage().getRenderWidth(),
            LineAlignment.CENTER).appendText("Work(s) Cited", font));
    }

    @Override
    protected MatterArea parseHeader(ManuscriptFile data) throws IOException{
        return null;
    }

    @Override
    protected DivisionText parseSpan(FormattedSpan span) throws IOException{
        DivisionTextFormatted line = newFormatDivision();
        line.setIndent(Utilities.inchToPoint(0.5f));
        return line.addContent(span);
    }
}