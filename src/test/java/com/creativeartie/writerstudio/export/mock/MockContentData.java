package com.creativeartie.writerstudio.export.mock;

import java.util.*;

import com.creativeartie.writerstudio.export.*;

import static org.junit.jupiter.api.Assertions.*;

public class MockContentData implements ContentData{

    public String contentText;

    public Optional<ContentLine> contentFootnote;

    public List<DataContentType> formatTypes;

    public Optional<String> linkPath;

    public MockContentData(String text){
        contentText = text;

        contentFootnote = Optional.empty();

        formatTypes = new ArrayList<>();

        linkPath = Optional.empty();;
    }

    @Override
    public void updatePageInfo(OutputPageInfo info){
    }

    @Override
    public String getText(){
        return contentText;
    }

    public MockContentData setText(String value){
        contentText = value;
        return this;
    }

    @Override
    public Optional<ContentLine> getFootnote(){
        return contentFootnote;
    }

    public MockContentData setFootnote(Optional<ContentLine> value){
        contentFootnote = value;
        return this;
    }

    @Override
    public List<DataContentType> getFormats(){
        return formatTypes;
    }

    @Override
    public Optional<String> getLinkPath(){
        return linkPath;
    }

    public MockContentData setLinkPath(Optional<String> value){
        linkPath = value;
        return this;
    }

    public void assertContent(ContentData test){
        assertContent(test, "ContentData");
    }

    public void assertContent(ContentData test, String name){
        assertAll(name,
            () -> assertEquals(contentText, test.getText()),
            () -> assertEquals(contentFootnote, test.getFootnote()),
            () -> assertArrayEquals(formatTypes.toArray(),
                test.getFormats().toArray()),
            () -> assertEquals(linkPath, test.getLinkPath())
        );
    }
}
