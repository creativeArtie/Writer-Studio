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
        for (Span child: span){
            if (child instanceof FormatSpan){
                FormatSpan format = (FormatSpan) child;
                String text = getText(format);
                ContentFont font = parentDoc.new PdfFont();
                if (format.isCoded()){
                    font = font.changeToMono();
                }
                font = font.changeBold(format.isBold());
                font = font.changeItalics(format.isItalics());
                font = font.changeUnderline(format.isUnderline());
                if (child instanceof FormatSpanLink){
                    font = font.changeFontColor(Color.BLUE);
                }
                if (child instanceof FormatSpanDirectory){
                    font = getFont((FormatSpanDirectory) child, font);
                }
                setAddition(appendText(text, font), format);
            }
        }
        return this;
    }
    
    private ContentFont getFont(FormatSpanDirectory span, ContentFont font){
        if (span.getIdType() != DirectoryType.NOTE){
            return font.changeToSuperscript();
        }
        return font;
    }

    private String getText(FormatSpan span) throws IOException{
        if (span instanceof FormatSpanContent){
            return ((FormatSpanContent) span).getText();
        } else if (span instanceof FormatSpanLink){
            return ((FormatSpanLink)span).getText();
        } else if (span instanceof FormatSpanDirectory){
            FormatSpanDirectory ref = (FormatSpanDirectory) span;
            Optional<SpanBranch> base = ref.getTarget();
            Optional<LinedSpanPointNote> note = base
                .filter(t -> t instanceof LinedSpanPointNote)
                .map(t -> (LinedSpanPointNote) t);
            Optional<String> text = note
                .filter(t -> t.getDirectoryType() == DirectoryType.ENDNOTE)
                .map(t -> parentDoc.addEndnote(t));
            if (text.isPresent()){
                return text.get();
            }
            note = note.filter(t -> t.getDirectoryType() == DirectoryType
                .FOOTNOTE);
            if (note.isPresent()){
                return contentData.addFootnote(note.get());
            }
        }
        return span.getRaw();
    }

    private void setAddition(ArrayList<ContentText> formatter,
            FormatSpan span){
        if (span instanceof FormatSpanLinkDirect){
            String path = ((FormatSpanLinkDirect)span).getPath();
            for (ContentText data: formatter){
                data.setLinkPath(path);
            }
        } else if (span instanceof FormatSpanLinkRef){
            Optional<SpanBranch> target = ((FormatSpanLinkRef) span)
                .getPathSpan();
            target.filter(f -> f instanceof LinedSpanPointLink)
                .map(s -> (LinedSpanPointLink) s)
                .ifPresent(s ->
                    formatter.forEach(c -> c.setLinkPath(s.getPath()))
                );
        }
        Optional<FootnoteItem> line = contentData.popFootnote();
        line.ifPresent(l -> formatter.forEach(c -> c.setFootnote(line)));
    }
}