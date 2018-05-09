package com.creativeartie.writerstudio.lang.markup;

import com.creativeartie.writerstudio.lang.*;

import java.util.*;
import java.util.function.*;
import java.util.Optional;
import java.io.*;

import com.google.common.base.*;
import com.google.common.io.*;

import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;
import com.creativeartie.writerstudio.main.Checker;

/**
 * Main document that put all the {@link Span spans} together. Represented in
 * design/ebnf.txt as {@code Manuscript}
 */
public class WritingText extends Document{

    public WritingText(File file) throws IOException{
        this(Files.asCharSource(Checker.checkNotNull(file, "textFile"),
            Charsets.UTF_8).read());
    }

    public WritingText(){
        this("");
    }

    public WritingText(String text){
        super(text, SectionParseHead.SECTION_1);
    }

    public boolean allHasHeading(){
        Optional<SectionSpanHead> found = spanAtFirst(SectionSpanHead.class);
        return found.isPresent() && found.get().getHeading().isPresent();
    }

    public int getPublishTotal(){
        int count = 0;
        for (SpanBranch span: this){
            count += count((SectionSpanHead) span, c -> c.getPublishTotal());
        }
        return count;
    }

    private int count(SectionSpanHead span, ToIntFunction<SectionSpanHead> counter){
        int count = counter.applyAsInt(span);
        for (SectionSpanHead child: span.getSections()){
            count += count(child, counter);
        }
        return count;
    }

    public int getNoteTotal(){
        int count = 0;
        for (SpanBranch span: this){
            count += count((SectionSpanHead) span, c -> c.getNoteTotal());
        }
        return count;
    }

    @Override
    public synchronized void insert(int index, String input){
        super.insert(index, input);
    }

    @Override
    public synchronized void delete(int start, int end){
        super.delete(start, end);
    }

    @Override
    public String toString(){
        return "Document" + super.toString();
    }

}
