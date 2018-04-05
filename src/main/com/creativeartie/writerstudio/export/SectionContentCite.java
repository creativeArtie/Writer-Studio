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

public class SectionContentCite extends SectionContent<FormatSpanMain> {

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
    protected DivisionText parseSpan(FormatSpanMain span) throws IOException{
        DivisionTextFormatted line = newFormatDivision();
        line.setIndent(Utilities.inchToPoint(0.5f));
        return line.addContent(span);
    }
}