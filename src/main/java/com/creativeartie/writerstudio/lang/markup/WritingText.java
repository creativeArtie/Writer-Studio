package com.creativeartie.writerstudio.lang.markup;

import com.creativeartie.writerstudio.lang.*;

import java.util.*;
import java.util.function.*;
import java.util.Optional;
import java.io.*;

import com.google.common.base.*;
import com.google.common.io.*;

import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writerstudio.main.ParameterChecker.*;

/** Main document that contain the content.
 *
 * Impelments the rule {@code design/ebnf.txt Data}.
 */
public class WritingText extends Document{

    private final CacheKeyMain<Boolean> cacheHeadings;
    private final CacheKeyMain<Integer> cachePublish;
    private final CacheKeyMain<Integer> cacheNote;

    /** Creates a {@linkplain WritingText}.
     *
     * @param file
     *      file text source
     * @see WritingFile#newSampleFile(File)
     */
    WritingText(File file) throws IOException{
        this(Files.asCharSource(argumentNotNull(file, "textFile"),
            Charsets.UTF_8).read());
    }

    /** Creates an empty  {@linkplain WritingText}.
     *
     * @see WritingFile#newFile()
     */
    WritingText(){
        this("");
    }

    /** Creates a {@linkplain WritingText}.
     *
     * @param text
     *      raw content text
     * @see WritingFile#open(File)
     */
    public WritingText(String text){
        super(text, SectionParseHead.SECTION_1);
        cacheHeadings = CacheKeyMain.booleanKey();
        cachePublish = CacheKeyMain.integerKey();
        cacheNote = CacheKeyMain.integerKey();
    }

    /** Check if all section has a heading.
     */
    public boolean allHasHeading(){
        return getLocalCache(cacheHeadings, () ->
            spanAtFirst(SectionSpanHead.class)
                .flatMap(s -> s.getHeading())
                .isPresent()
        );
    }

    public int getPublishTotal(){
        return getLocalCache(cachePublish, () -> stream()
            .map(s -> (SectionSpanHead) s)
            .mapToInt(s -> s.getPublishTotal())
            .sum()
        );
    }

    public int getNoteTotal(){
        return getLocalCache(cacheNote, () -> stream()
            .map(s -> (SectionSpanHead) s)
            .mapToInt(s -> s.getNoteTotal())
            .sum()
        );
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
