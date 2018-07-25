package com.creativeartie.writerstudio.export;

import java.util.*;
import java.util.function.*;

public final class MockBridgeContent implements BridgeContent{

    private static long counter = Long.MIN_VALUE;

    private static synchronized long createId(){
        return counter++;
    }

    private boolean[] testFormats;

    private String testText;
    private Optional<BridgeDivision> testFootnote;
    private Optional<String> testPath;

    private long uniqueId;

    public MockBridgeContent(String text){
        testFormats = new boolean[5];
        Arrays.fill(testFormats, false);

        testText = text;
        testFootnote = Optional.empty();
        testPath = Optional.empty();

        uniqueId = createId();
    }

    @Override
    public boolean isBold(){
        return testFormats[0];
    }

    public MockBridgeContent setIsBold(boolean value){
        testFormats[0] = value;
        return this;
    }

    @Override
    public boolean isItalics(){
        return testFormats[1];
    }

    public MockBridgeContent setIsItalics(boolean value){
        testFormats[1] = value;
        return this;
    }

    @Override
    public boolean isUnderline(){
        return testFormats[2];
    }

    public MockBridgeContent setIsUnderline(boolean value){
        testFormats[2] = value;
        return this;
    }

    @Override
    public boolean isCoded(){
        return testFormats[3];
    }

    public MockBridgeContent setIsCode(boolean value){
        testFormats[3] = value;
        return this;
    }

    @Override
    public boolean isSuperscript(){
        return testFormats[4];
    }

    public MockBridgeContent setIsSuperscript(boolean value){
        testFormats[4] = value;
        return this;
    }

    @Override
    public String getText(){
        return testText;
    }

    public MockBridgeContent setText(String print){
        testText = print;
        return this;
    }

    @Override
    public Optional<BridgeDivision> getNote(){
        return testFootnote;
    }

    public MockBridgeContent setNote(Optional<BridgeDivision> value){
        testFootnote = value;
        return this;
    }

    @Override
    public Optional<String> getLink(){
        return testPath;
    }

    public MockBridgeContent setLink(Optional<String> value){
        testPath = value;
        return this;
    }


    public long getUniqueId(){
        return uniqueId;
    }

    @Override
    public boolean isEquals(BridgeContent content){
        if (content instanceof MockBridgeContent){
            return uniqueId == ((MockBridgeContent)content).uniqueId;
        }
        return false;
    }

}
