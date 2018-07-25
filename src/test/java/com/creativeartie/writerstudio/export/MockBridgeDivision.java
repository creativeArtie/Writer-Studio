package com.creativeartie.writerstudio.export;

import java.util.*;

public final class MockBridgeDivision implements BridgeDivision{

    private static Iterable<BridgeContent> buildList(String ... texts){
        ArrayList<BridgeContent> content = new ArrayList<>();
        for (String text: texts){
            content.add((BridgeContent) new MockBridgeContent(text));
        }
        return content;
    }

    private Iterable<BridgeContent> listContent;

    public MockBridgeDivision(Iterable<BridgeContent> content){
        listContent = content;
    }

    public MockBridgeDivision(String ... texts){
        this(buildList(texts));
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
