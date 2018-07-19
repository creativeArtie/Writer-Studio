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

    private List<CacheKeyList<TextSpanMatter>> cachePrint;
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
        super(text, TextParser.PARSER);
        ImmutableList.Builder<CacheKeyList<TextSpanMatter>> print =
            ImmutableList.builder();
        for (TextTypeMatter area: TextTypeMatter.values()){
            print.add(new CacheKeyList<>(TextSpanMatter.class));
        }
        cachePrint = print.build();

        ImmutableList.Builder<CacheKeyMain<String>> meta =
            ImmutableList.builder();
        for (TextTypeInfo area: TextTypeInfo.values()){
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
    public List<TextSpanMatter> getMatter(TextTypeMatter area){
        argumentNotNull(area, "area");

        return getLocalCache(cachePrint.get(area.ordinal()), () ->
            ImmutableList.copyOf(getChildren(TextSpanMatter.class).stream()
                .filter(p -> p.getRowType() == area)
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
     */
    public void setMatter(TextTypeMatter area, String raw){
        argumentNotNull(area, "area");
        argumentNotNull(raw, "raw");
        setFireReady(false);

        raw = raw.replace(TOKEN_ESCAPE + LINED_END, LINED_END);
        List<TextSpanMatter> print = getMatter(area);
        int i = 0;
        for (String text: Splitter.on(CHAR_NEWLINE).split(raw)){
            if (i < print.size()){
                print.get(i).setData(text);
            } else {
                addChild(TextParser.PARSER, area.getKeyName() + TEXT_SEPARATOR +
                    TextDataType.CENTER.getKeyName() + TEXT_SEPARATOR +
                    TextSpanMatter.fixText(text) + LINED_END);
            }
            i++;
        }
        while (i < print.size()){
            print.get(i).deleteLine();
            i++;
        }
        setFireReady(true);
    }

    /** Gets meta data text.
     *
     * @param meta
     *      target meta data
     * @return answer
     */
    public String getInfo(TextTypeInfo meta){
        argumentNotNull(meta, "meta");

        return getLocalCache(cacheMeta.get(meta.ordinal()), () ->
            getWritingData(meta)
            /// s = TextSpanInfo
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
     */
    public void setInfo(TextTypeInfo meta, String raw){
        argumentNotNull(meta, "meta");
        argumentNotNull(raw, "raw");
        setFireReady(false);

        Optional<TextSpanInfo> span = getWritingData(meta);
        if (! span.isPresent()){
            String text = TextSpanInfo.escapeText(raw);
            addChild(TextParser.PARSER, meta.getKeyName() + TEXT_SEPARATOR +
                TextDataType.TEXT.getKeyName() + TEXT_SEPARATOR + text + "\n");
        } else span.get().editText(raw);
        setFireReady(true);
    }

    /** Gets meta data.
     *
     * @param meta
     *      target meta data
     * @return answer
     * @see #getInfoText(TextTypeInfo)
     * @see #setMetaText(TextTypeInfo, String)
     */
    private Optional<TextSpanInfo> getWritingData(TextTypeInfo meta){
        assert meta != null: "Null meta";

        for (TextSpanInfo span: getChildren(TextSpanInfo.class)){
            if (span.getRowType() == meta){
                return Optional.of(span);
            }
        }
        return Optional.empty();
    }

}
