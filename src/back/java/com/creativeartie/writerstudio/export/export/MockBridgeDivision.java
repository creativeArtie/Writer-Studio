package com.creativeartie.writerstudio.export;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public final class MockBridgeDivision implements BridgeDivision{

    public static <T extends Number> void test(String[][] texts, T size,
        ExportDivisionText<T> test
    ){
        int i = 0;
        for (ExportDivisionTextLine<?> line: test){
            int j = 0;
            for(ExportContentText text: line){
                assertEquals(texts[i][j], text.getText(), "getText()");
                j++;
            }
            i++;
        }
        assertEquals(size, test.getFillHeight(), "getFillHeight()");
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
        this(DataLineType.LEFT, buildList(texts));
    }

    public MockBridgeDivision(DataLineType type, Iterable<BridgeContent> content){
        lineType = type;
        listContent = content;
    }

    public MockBridgeDivision(Iterable<BridgeContent> content){
        this(DataLineType.LEFT, content);
    }

    @Override
    public Iterator<BridgeContent> iterator(){
        return listContent.iterator();
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
