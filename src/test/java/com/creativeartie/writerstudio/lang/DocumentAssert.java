package com.creativeartie.writerstudio.lang;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.util.stream.*;
import java.util.*;

public class DocumentAssert {

    private static class LastBranch extends SpanBranch{

        private LastBranch(List<Span> children){
            super(children);
        }

        @Override public List<StyleInfo> getBranchStyles(){
            return new ArrayList<>();
        }
        @Override protected SetupParser getParser(String text){
            return null;
        }

        @Override public String toString(){
            return "unused:" + get(0);
        }
    }

    private static final SetupParser END_PARSER = (pointer) -> {
        ArrayList<Span> children = new ArrayList<>();
        pointer.getTo(children, ((char)0) + "");
        return Optional.of(new LastBranch(children));
    };

    public static DocumentAssert assertDoc(int size, String raw,
            SetupParser ... parsers){
        Document test = new Document(rawText,
            Stream.of(parsers, new SetupParser[]{END_PARSER})
                .flatMap(Stream::of)
                .toArray(SetupParser[]::new)) {
        };
        return assertDoc(childrenSize, rawText, test);
    }

    public static DocumentAssert assertDoc(int size, String raw, Document test){
        return new DocumentAssert(test).assertDoc(size, raw);
    }

    private final Document doc;
    private IDAssertions idTester;

    private DocumentAssert(Document document){
        doc = document;
        idTester = new IDAssertions();
    }

    public Document getDocument(){
        return doc;
    }
}
