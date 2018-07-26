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
    private DataLineType lineType;

    public MockBridgeDivision(DataLineType type, String ... texts){
        this(type, buildList(texts));
    }

    public MockBridgeDivision(String ... texts){
        this(DataLineType.DEFAULT, buildList(texts));
    }

    public MockBridgeDivision(DataLineType type, Iterable<BridgeContent> content){
        lineType = type;
        listContent = content;
    }

    public MockBridgeDivision(Iterable<BridgeContent> content){
        this(DataLineType.DEFAULT, content);
    }

    @Override
    public Iterable<BridgeContent> getContent(){
        return listContent;
    }

    public MockBridgeDivision setListContent(String ... texts){
        listContent = buildList(texts);
        return this;
    }

    @Override
    public DataLineType getLineType(){
        return lineType;
    }

    public void setLineType(DataLineType type){
        lineType = type;
    }
}
