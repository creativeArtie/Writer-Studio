package com.creativeartie.writerstudio.pdf;

import java.util.*;
import java.util.Optional;
import java.util.function.*;
import java.io.*;
import com.google.common.base.*;

import com.creativeartie.writerstudio.pdf.value.*;
import com.creativeartie.writerstudio.main.*;
import org.apache.pdfbox.pdmodel.interactive.action.*;
import org.apache.pdfbox.pdmodel.interactive.annotation.*;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.*;

/**
 * Decides the length of a text and if it need to be keep with the last text.
 */
final class FormatterData{
    private static final String SPACE = " ";

    public static ArrayList<FormatterData> createWords(String text, SizedFont font)
            throws IOException{

        FormatterData space = new FormatterData(SPACE, font, true);
        CharMatcher whitespace = CharMatcher.whitespace();

        ArrayList<FormatterData> holder = new ArrayList<>();
        for (String word: Splitter.on(whitespace).omitEmptyStrings()
                .split(text)){
            holder.add(new FormatterData(word, font, false));
        }
        int i = 0;
        boolean isFirst = true;
        ArrayList<FormatterData> ans = new ArrayList<>();
        if (whitespace.indexIn(text) == 0){
            ans.add(space);
        }
        for (FormatterData item: holder){
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

    private SizedFont textFont;
    private String outputText;
    private float textWidth;
    private float textHeight;
    private boolean spaceText;
    private Optional<Consumer<FormatterData>> textChange;
    private Optional<String> linkPath;

    private FormatterData(String word, SizedFont font, boolean space)
            throws IOException{
        outputText = word;
        textFont = font;
        textWidth = textFont.getWidth(word);
        textHeight = textFont.getHeight();
        spaceText = space;
        textChange = Optional.empty();
        linkPath = Optional.empty();
    }

    public FormatterData setListener(Consumer<FormatterData> consumer){
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

    public FormatterData setLinkPath(String path){
        linkPath = Optional.ofNullable(path);
        return this;
    }

    public FormatterData setText(String text){
        outputText = text;
        textChange.ifPresent(consume -> consume.accept(this));
        return this;
    }

    public boolean isSpaceText(){
        return spaceText;
    }

    public SizedFont getFont(){
        return textFont;
    }

    public ArrayList<PostTextEditor> getPostTextConsumers(PDRectangle rect){
        ArrayList<PostTextEditor> ans = new ArrayList<>();
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