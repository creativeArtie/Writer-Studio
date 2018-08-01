package com.creativeartie.writerstudio.export.mock;

import java.util.*;

import com.creativeartie.writerstudio.export.*;

import static org.junit.jupiter.api.Assertions.*;

public class MockContentData implements ContentData{

    public String contentText;

    public Optional<ContentLine> contentFootnote;

    public boolean isBold;
    public boolean isItalics;
    public boolean isUnderline;
    public boolean isCoded;
    public boolean isSuperscript;

    public Optional<String> linkPath;

    public MockContentData(String text){
        contentText = text;

        contentFootnote = Optional.empty();

        isBold = false;
        isItalics = false;
        isUnderline = false;
        isCoded = false;
        isSuperscript = false;

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
    public boolean isBold(){
        return isBold;
    }

    public MockContentData setBold(boolean value){
        isBold = value;
        return this;
    }

    @Override
    public boolean isItalics(){
        return isItalics;
    }

    public MockContentData setItalics(boolean value){
        isItalics = value;
        return this;
    }

    @Override
    public boolean isUnderline(){
        return isUnderline;
    }

    public MockContentData setUnderline(boolean value){
        isUnderline = value;
        return this;
    }

    @Override
    public boolean isCoded(){
        return isCoded;
    }

    public MockContentData setCoded(boolean value){
        isCoded = value;
        return this;
    }

    @Override
    public boolean isSuperscript(){
        return isSuperscript;
    }

    public MockContentData setSuperscript(boolean value){
        isSuperscript = value;
        return this;
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
            () -> assertEquals(isBold, test.isBold()),
            () -> assertEquals(isItalics, test.isItalics()),
            () -> assertEquals(isUnderline, test.isUnderline()),
            () -> assertEquals(isCoded, test.isCoded()),
            () -> assertEquals(isSuperscript, test.isSuperscript()),
            () -> assertEquals(linkPath, test.getLinkPath())
        );
    }
}
