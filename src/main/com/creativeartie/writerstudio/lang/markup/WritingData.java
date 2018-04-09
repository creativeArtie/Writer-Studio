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
 * Main writing meta data.
 */
public class WritingData extends Document{

    public WritingData(File file) throws IOException{
        this(Files.asCharSource(Checker.checkNotNull(file, "textFile"),
            Charsets.UTF_8).read());
    }

    public WritingData(){
        this("");
    }

    public WritingData(String text){
        super(text, TextDataParser.PARSER);
    }

    public List<TextDataSpanPrint> getPrint(TextDataType.Area area){
        ArrayList<TextDataSpanPrint> ans = new ArrayList<>();
        for (SpanBranch span: this){
            if (span instanceof TextDataSpanPrint){
                TextDataSpanPrint print = (TextDataSpanPrint) span;
                if (print.getType() == area){
                    ans.add(print);
                }
            }
        }
        return ans;
    }

    public void newPrintText(TextDataType.Area area, String raw,
            TextDataType.Format format){
        if (! format.isAlignType()){
            throw new IllegalArgumentException("Unknown alignment: " + format);
        }

        runCommand(() -> getRaw() + area.getKeyName() + format.getKeyName() +
            "\n");
    }

    public void removePrintText(TextDataSpanPrint span){
        runCommand(span, () -> "");
    }

    public String getMetaText(TextDataType.Meta meta){
        TextDataSpanMeta span = getWritingData(meta);
        if (span != null){
            return span.getData().map(s -> s.getTrimmed()).orElse("");
        }
        return "";
    }

    public void setMetaText(TextDataType.Meta meta, String raw){
        TextDataSpanMeta span = getWritingData(meta);
        if (span == null){
            runCommand(() -> getRaw() + meta.getKeyName() +
                TextDataType.Format.TEXT.getKeyName() + "\n");
            span = getWritingData(meta);
        }
        span.editText(raw);

    }

    private TextDataSpanMeta getWritingData(TextDataType.Meta meta){
        for (SpanBranch span: this){
            if (span instanceof TextDataSpanMeta &&
                    ((TextDataSpan) span).getType() == meta){
                return (TextDataSpanMeta) span;
            }
        }
        return null;
    }

    @Override
    protected void docEdited(){
        // TODO docEdited
    }

    @Override
    protected void childEdited(){
        // TODO childEdited
    }
}