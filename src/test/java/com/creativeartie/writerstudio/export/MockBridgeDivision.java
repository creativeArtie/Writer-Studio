package com.creativeartie.writerstudio.export;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public final class MockBridgeDivision implements BridgeDivision{

    public static void test(String[][] expect, ExportDivisionText<?> test){
        int i = 0;
        for (ExportDivisionTextLine<?> line: test){
            int j = 0;
            for(ExportContentText text: line){
                assertEquals(expect[i][j], text.getText());
                j++;
            }
            i++;
        }
    }

    private static Iterable<BridgeContent> buildList(String ... texts){
        ArrayList<BridgeContent> content = new ArrayList<>();
        for (String text: texts){
            content.add((BridgeContent) new MockBridgeContent(text));
        }
        return content;
    }

    private Iterable<BridgeContent> listContent;

    public MockBridgeDivision(String ... texts){
        this(buildList(texts));
    }

    public MockBridgeDivision(Iterable<BridgeContent> content){
        listContent = content;
    }

    @Override
    public Iterable<BridgeContent> getContent(){
        return listContent;
    }

    public MockBridgeDivision setListContent(String ... texts){
        listContent = buildList(texts);
        return this;
    }

}
