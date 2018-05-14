package com.creativeartie.writerstudio.lang.markup;

import com.creativeartie.writerstudio.lang.*;

import java.util.*;
import java.util.Optional;
import java.util.stream.*;

import com.google.common.base.*;
import com.google.common.collect.*;

import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writerstudio.main.ParameterChecker.*;

/** Main writing meta data and other matters.
 *
 * Impelments the rule {@code design/ebnf.txt Data}.
 */
public final class WritingData extends Document{

    private List<CacheKeyList<TextDataSpanPrint>> cachePrint;
    private List<CacheKeyMain<String>> cacheMeta;

    /** Creates an empty {@linkplain WritingData}.
     *
     * @see WritingFile#newFile()
     */
    WritingData(){
        this("");
    }

    /** Creates a {@linkplain WritingData}.
     * @param text
     *      data text
     * @see WritingFile#opeFile(File)
     * @see WritingFile#newSampleFile(File)
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
        argumentNotNull(area, "area");

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
        argumentNotNull(area, "area");
        argumentNotNull(raw, "raw");

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
        argumentNotNull(meta, "meta");

        return getLocalCache(cacheMeta.get(meta.ordinal()), () ->
            getWritingData(meta)
            /// s = TextDataSpanMeta
            .flatMap(s -> s.getData())
            /// s = ContentSpan
            .map(s -> s.getTrimmed())
            .orElse("")
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
        argumentNotNull(meta, "meta");
        argumentNotNull(raw, "raw");

        Optional<TextDataSpanMeta> span = getWritingData(meta);
        if (! span.isPresent()){
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
        assert meta != null: "Null meta";

        for (TextDataSpanMeta span: getChildren(TextDataSpanMeta.class)){
            if (span.getType() == meta){
                return Optional.of(span);
            }
        }
        return Optional.empty();
    }

}