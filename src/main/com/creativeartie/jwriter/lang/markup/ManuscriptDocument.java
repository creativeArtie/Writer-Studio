package com.creativeartie.jwriter.lang.markup;

import com.creativeartie.jwriter.lang.*;

import java.util.*;
import java.util.Optional;
import java.io.*;

import com.google.common.base.*;
import com.google.common.io.*;

import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;
import com.creativeartie.jwriter.main.Checker;

/**
 * Main document that put all the {@link Span spans} together. Represented in
 * design/ebnf.txt as {@code Manuscript}
 */
public class ManuscriptDocument extends Document{

    public ManuscriptDocument(File file) throws IOException{
        this(Files.asCharSource(Checker.checkNotNull(file, "textFile"),
            Charsets.UTF_8).read());
    }

    public ManuscriptDocument(){
        this("");
    }

    public ManuscriptDocument(String text){
        super(text, SectionParseHead.SECTION_1);
    }

    public LinedSpan getLine(int line){
        Iterator<SpanBranch> it = iterator();
        SpanBranch section = it.next();
        int found = 0;
        while (section.size() + found <= line){
            found += section.size();
            if (it.hasNext()){
                section = it.next();
            }
        }
        return (LinedSpan) section.get(line - found);
    }

    public boolean allHasHeading(){
        Optional<MainSpanSection> found = spanAtFirst(MainSpanSection.class);
        return found.isPresent() && found.get().getHeading().isPresent();
    }

    public int getPublishTotal(){
        int count = 0;
        for (SpanBranch span: this){
            count += ((MainSpan)span).getPublishTotal();
        }
        return count;
    }

    public int getNoteTotal(){
        int count = 0;
        for (SpanBranch span: this){
            count += ((MainSpan)span).getNoteTotal();
        }
        return count;
    }

    @Override
    protected void docEdited(){
        // TODO docEdited
    }

    @Override
    protected void childEdited(){
        // TODO docEdited
    }
}
