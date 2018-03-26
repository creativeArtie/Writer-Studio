package com.creativeartie.writerstudio.export;

import java.io.*;
import java.awt.*;
import java.util.*;
import com.creativeartie.writerstudio.file.*;
import com.creativeartie.writerstudio.resource.*;
import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.*;

import com.creativeartie.writerstudio.export.value.*;

import com.google.common.collect.*;

public class DataContentLineMain extends DataContentLine{
    private ArrayList<ArrayList<DataContentLineNote>> pointerNotes;

    public DataContentLineMain(Data input, FormatterItem item,
            FormatSpanMain span, SizedFont font) throws IOException{
        super(input, item, span, font);
        if (pointerNotes == null){
            pointerNotes = new ArrayList<>();
        }
    }

    @Override
    protected String getText(FormatSpan span){
        if (span instanceof FormatSpanDirectory){
            FormatSpanDirectory ref = (FormatSpanDirectory) span;
            Optional<SpanBranch> base = ref.getTarget();
            Optional<LinedSpanPointNote> note = base
                .filter(t -> t instanceof LinedSpanPointNote)
                .map(t -> (LinedSpanPointNote) t);
            if (! note.isPresent()){
                return span.getRaw();
            }
            Optional<String> text = note
                .filter(t -> t.getDirectoryType() == DirectoryType.ENDNOTE)
                .map(t -> getContentData().addEndnote(t));
            if (! text.isPresent()){
                // DataContentLineNote line = DataContentLineNote.getNote(input, note
                //    .get(), baseFont, baseWidth);
                // pointerNotes.add(line);
                // return line.getText();

            }
            return text.orElse(span.getRaw());
        }
        return super.getText(span);
    }


    public ArrayList<DataContentLineNote> listNotes(int line){
        return pointerNotes.get(line);
    }

}