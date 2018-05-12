package com.creativeartie.writerstudio.lang.markup;

import com.creativeartie.writerstudio.lang.*;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.util.Optional;
import java.io.*;

import com.google.common.base.*;
import com.google.common.io.*;
import com.google.common.collect.*;

import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;
import com.creativeartie.writerstudio.main.Checker;

/** Main writing meta data and other matters. */
public final class WritingData extends Document{

    private List<CacheKeyList<TextDataSpanPrint>> cachePrint;
    private List<CacheKeyMain<String>> cacheMeta;


    /** Creates an empty {@linkplain WritingData}.
     *
     * @see WritingFile#opeFile(File)
     * @see WritingFile#newFile()
     * @see WritingFile#newSampleFile(File)
     */
    WritingData(){
        this("");
    }

    /** Creates a {@linkplain WritingData}.
     * @param text
     *      data text
     */
    WritingData(String text){
        super(text, TextDataParser.PARSER);
        ImmutableList.Builder<CacheKeyList<TextDataSpanPrint>> print =
            ImmutableList.builder();
        for (TextDataType.Area area: TextDataType.Area.values()){
            print.add(new CacheKeyList<>(TextDataSpanPrint.class));
        }
        cachePrint = print.build();

        ImmutableList.Builder<CacheKeyMain<String>> meta =
            ImmutableList.builder();
        for (TextDataType.Meta area: TextDataType.Meta.values()){
            meta.add(new CacheKeyMain<>(String.class));
        }
        cacheMeta = meta.build();
    }

    /** Gets a list of lines to print in a manuscript area.
     *
     * @param area
     *      target area
     * @return answer
     */
    public List<TextDataSpanPrint> getPrint(TextDataType.Area area){
        return getLocalCache(cachePrint.get(area.ordinal()), () ->
            ImmutableList.copyOf(getChildren(TextDataSpanPrint.class).stream()
                .filter(p -> p.getType() == area)
                .collect(Collectors.toList())
            )
        );
    }

    /** Sets a list of lines to print in a manuscript area.
     *
     * @param area
     *      target area
     * @param raw
     *      new raw text
     * @return answer
     */
    public void setPrintText(TextDataType.Area area, String raw)
            throws TextAreaLineException{
        raw = raw.replace(TOKEN_ESCAPE + LINED_END, LINED_END);
        List<TextDataSpanPrint> print = getPrint(area);
        int i = 0;
        for (String text: Splitter.on(CHAR_NEWLINE).split(raw)){
            if (i < print.size()){
                print.get(i).setData(text);
            } else {
                runCommand(() -> getRaw() + area.getKeyName() +
                    TextDataType.Format.CENTER.getKeyName() + text + LINED_END);
            }
            i++;
        }
        while (i < print.size()){
            print.get(i).deleteLine();
            i++;
        }
    }

    /** Gets meta data text.
     *
     * @param meta
     *      target meta data
     * @return answer
     */
    public String getMetaText(TextDataType.Meta meta){
        getLocalCache(cacheMeta, () -> getWritingData(meta)
            /// s = TextDataSPanMeta
            .flatMap(s -> span.getData())
            /// s = ContentSpan
            .map(s -> s.getTrimmed())
            .orElse("");
        );
    }

    /** Sets meta data text.
     *
     * @param meta
     *      target meta data
     * @param raw
     *      new raw text
     * @return answer
     */
    public void setMetaText(TextDataType.Meta meta, String raw){
        TextDataSpanMeta span = getWritingData(meta);
        if (span.isEmpty()){
            runCommand(() -> getRaw() + meta.getKeyName() +
                TextDataType.Format.TEXT.getKeyName() + "\n");
            span = getWritingData(meta);
        }
        span.get().editText(raw);
    }

    /** Gets meta data.
     *
     * @param meta
     *      target meta data
     * @return answer
     * @see #getMetaText(TextDataType.Meta)
     * @see #setMetaText(TextDataType.Meta, String)
     */
    private Optional<TextDataSpanMeta> getWritingData(TextDataType.Meta meta){
        for (SpanBranch span: this){
            if (span instanceof TextDataSpanMeta &&
                    ((TextDataSpan) span).getType() == meta){
                return Optional.of((TextDataSpanMeta) span);
            }
        }
        return Optional.empty();
    }

}