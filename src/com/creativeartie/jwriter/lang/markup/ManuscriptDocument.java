package com.creativeartie.jwriter.lang.markup;

import com.creativeartie.jwriter.lang.*;

import java.util.*;
import java.util.Optional;
import java.io.*;

import com.google.common.base.*;
import com.google.common.io.*;

import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;
import com.creativeartie.jwriter.main.Checker;

public class ManuscriptDocument extends Document{

    public ManuscriptDocument(File file) throws IOException{
        this(Files.asCharSource(Checker.checkNotNull(file, "textFile"),
            Charsets.UTF_8).read());
    }

    public ManuscriptDocument(){
        this("");
    }

    public ManuscriptDocument(String text){
        super(text, new MainParser());
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

    public SectionHeading getSections(){
        SectionHeading heading = new SectionHeading();
        SectionHeading ans = heading;
        Optional<SectionOutline> outline = Optional.empty();
        for(CatalogueData data: getCatalogue().getCategory(TYPE_SECTION
            .toArray(new String[0])).values())
        {

            MainSpanSection section = (MainSpanSection) data.getTarget();
            Optional<LinedSpanSection> found = section.getSelfSection();
            if (found.isPresent()){
                if (found.get().getLinedType() == LinedType.HEADING){
                    heading = heading.appendHeading(found.get());
                    outline = Optional.empty();
                } else {
                    assert found.get().getLinedType() == LinedType.OUTLINE;
                    if (outline.isPresent()){
                        outline = outline.get().append(found.get());
                    }
                    if (! outline.isPresent()){
                        outline = Optional.of(heading.appendOutline(
                            found.get()));
                    }
                }
            }
        }
        return ans;
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
