package com.creativeartie.writerstudio.export;

import java.io.*;
import java.util.*;
import java.awt.*;

import com.google.common.collect.*;

import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.*;
import com.creativeartie.writerstudio.export.value.*;

public class DivisionLineFormatted extends DivisionLine{

    private SectionContent<?> contentData;
    private WritingExporter parentDoc;

    public DivisionLineFormatted(SectionContent<?> content,
            WritingExporter doc){
        super(content.getPage().getRenderWidth());
        contentData = content;
        parentDoc = doc;
    }

    public DivisionLineFormatted addContent(FormatSpanMain span)
            throws IOException{
        for(Span child: span){
            if (child instanceof FormatSpan){
                FormatSpan format = (FormatSpan) child;
                ContentFont font = addFont(format);
                parseContent(format, font);
            }
        }
        return this;
    }

    private ContentFont addFont(FormatSpan span){
        ContentFont font = parentDoc.new PdfFont();
        if (span.isCoded()){
            font = font.changeToMono();
        }
        font = font.changeBold(span.isBold());
        font = font.changeItalics(span.isItalics());
        font = font.changeUnderline(span.isUnderline());
        return font;
    }

    private void parseContent(FormatSpan span, ContentFont font)
            throws IOException{
        if (span instanceof FormatSpanContent){
            parseContent((FormatSpanContent) span, font);
        } else if (span instanceof FormatSpanLink){
            parseContent((FormatSpanLink) span, font);
        } else if (span instanceof FormatSpanDirectory){
            parseContent((FormatSpanDirectory) span, font);
        }
    }

    private void parseContent(FormatSpanContent span, ContentFont font)
            throws IOException{
        String text = span.getText();
        appendText(text, font);
    }

    private void parseContent(FormatSpanLink span, ContentFont font)
            throws IOException{
        font = font.changeFontColor(Color.BLUE);
        String text = span.getText();
        Optional<String> path = Optional.empty();
        if (span instanceof FormatSpanLinkDirect){
            path = Optional.of(((FormatSpanLinkDirect)span).getPath());
        } else if (span instanceof FormatSpanLinkRef){
            Optional<SpanBranch> target = ((FormatSpanLinkRef) span)
                .getPathSpan();
            path = target.filter(f -> f instanceof LinedSpanPointLink)
                .map(s -> (LinedSpanPointLink) s)
                .map(s -> s.getPath());
        }
        if (path.isPresent()){
            for (ContentText content: appendText(text, font)) {
                content.setLinkPath(path.get());
            }
        } else {
            appendText(text, font);
        }
    }

    private void parseContent(FormatSpanDirectory span, ContentFont font)
            throws IOException{
        if (span.getIdType() != DirectoryType.NOTE){
            font = font.changeToSuperscript();
        }
        Optional<SpanBranch> base = span.getTarget();
        Optional<LinedSpanPointNote> note = base
            .filter(t -> t instanceof LinedSpanPointNote)
            .map(t -> (LinedSpanPointNote) t);
        Optional<String> text = note
            .filter(t -> t.getDirectoryType() == DirectoryType.ENDNOTE)
            .map(t -> parentDoc.addEndnote(t));
        if (text.isPresent()){
            appendText(text.get(), font);
            return;
        }

        note = note.filter(t -> t.getDirectoryType() == DirectoryType
            .FOOTNOTE);
        if (note.isPresent()){
            for (ContentText content: appendText(
                contentData.getFootnote().prepFootnote(note.get()), font
            )){
                content.setTarget(Optional.of((SpanBranch)note.get()));
            }
        }
    }

}