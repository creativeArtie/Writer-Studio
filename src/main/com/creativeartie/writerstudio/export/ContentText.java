package com.creativeartie.writerstudio.export;

import java.util.*;
import java.util.Optional;
import java.util.function.*;
import java.io.*;
import com.google.common.base.*;

import com.creativeartie.writerstudio.export.value.*;
import com.creativeartie.writerstudio.main.*;
import org.apache.pdfbox.pdmodel.interactive.action.*;
import org.apache.pdfbox.pdmodel.interactive.annotation.*;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.*;

public class ContentText{
    private static final String SPACE = " ";
    public static ArrayList<ContentText> createWords(String text, ContentFont font)
            throws IOException{

        ContentText space = new ContentText(SPACE, font, true);
        CharMatcher whitespace = CharMatcher.whitespace();

        ArrayList<ContentText> holder = new ArrayList<>();
        for (String word: Splitter.on(whitespace).omitEmptyStrings()
                .split(text)){
            holder.add(new ContentText(word, font, false));
        }
        int i = 0;
        boolean isFirst = true;
        ArrayList<ContentText> ans = new ArrayList<>();
        if (whitespace.indexIn(text) == 0){
            ans.add(space);
        }
        for (ContentText item: holder){
            if (isFirst){
                isFirst = false;
            } else {
                ans.add(space);
            }
            ans.add(item);
        }
        if (whitespace.lastIndexIn(text) == text.length() - 1){
            ans.add(space);
        }
        return ans;
    }

    private ContentFont textFont;
    private String outputText;
    private float textWidth;
    private float textHeight;
    private boolean spaceText;
    private Optional<Consumer<ContentText>> textChange;
    private Optional<String> linkPath;
    private Optional<FootnoteItem> footnoteLine;

    private ContentText(String word, ContentFont font, boolean space)
            throws IOException{
        outputText = word;
        textFont = font;
        textWidth = textFont.getWidth(word);
        textHeight = textFont.getHeight();
        spaceText = space;
        textChange = Optional.empty();
        linkPath = Optional.empty();
        footnoteLine = Optional.empty();
    }

    public ContentText setListener(Consumer<ContentText> consumer){
        textChange = Optional.of(consumer);
        return this;
    }

    public float getWidth() {
        return textWidth;
    }

    public float getHeight(){
        return textHeight;
    }

    public String getText(){
        return outputText;
    }

    public ContentText setLinkPath(String path){
        linkPath = Optional.ofNullable(path);
        return this;
    }

    public ContentText setText(String text){
        outputText = text;
        textChange.ifPresent(consume -> consume.accept(this));
        return this;
    }

    public boolean isSpaceText(){
        return spaceText;
    }

    public ContentFont getFont(){
        return textFont;
    }
    
    public ContentText setFootnote(Optional<FootnoteItem> footnote){
        footnoteLine = footnote;
        return this;
    }
    
    public float getFootnoteHeight(){
        return footnoteLine.map(l -> l.getHeight()).orElse(0f);
    }
    
    public Optional<FootnoteItem> getFootnoteLine(){
        return footnoteLine;
    }

    public ArrayList<ContentPostEditor> getPostTextConsumers(PDRectangle rect){
        ArrayList<ContentPostEditor> ans = new ArrayList<>();
        if (textFont.isUnderline()){
            ans.add((page, stream) ->{
                stream.setStrokingColor(textFont.getColor());
                stream.moveTo(rect.getLowerLeftX(), rect.getLowerLeftY() - 2);
                stream.lineTo(rect.getUpperRightX(), rect.getLowerLeftY() - 2);
                stream.stroke();
            });
        }
        linkPath.ifPresent(path -> ans.add((page, stream) ->{
            PDAnnotationLink link = new PDAnnotationLink();

            // add an action
            PDActionURI action = new PDActionURI();
            action.setURI(path);
            link.setAction(action);
            link.setRectangle(rect);
            page.getAnnotations().add(link);
        }));
        return ans;
    }

    @Override
    public String toString(){
        return "\"" + outputText + "\"";
    }
}