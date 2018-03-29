package com.creativeartie.writerstudio.export;

import java.io.*;
import java.util.*;

import com.google.common.collect.*;

import com.creativeartie.writerstudio.export.value.*;

public class DivisionLine extends ForwardingList<DivisionLine.Line>{

    public class Line extends ForwardingList<ContentText>{
        private ArrayList<ContentText> inputText;
        private float maxWidth;
        private float curWidth;
        private float textHeight;
        private float lineIndent;

        private Line(float width, float indent){
            this(width, new ArrayList<>());
            lineIndent = indent;
        }

        private Line(float width, ArrayList<ContentText> text){
            inputText = text;
            maxWidth = width;
            textHeight = 0;
            curWidth = 0;
        }

        public float getIndent(){
            return lineIndent;
        }

        private ArrayList<ContentText> appendText(ArrayList<ContentText> texts){
            ArrayList<ContentText> overflow = null;
            for (ContentText text: texts){
                if (inputText.isEmpty() && text.isSpaceText()){
                    continue;
                }
                if (overflow == null){
                    if (curWidth + text.getWidth() > maxWidth){
                        /// Appending text does not fit line
                        overflow = new ArrayList<>();
                        if (inputText.isEmpty()){
                            /// Text can not fit line
                            inputText.add(text);
                            text.setListener(data -> reflowText());
                            curWidth = text.getWidth();
                            continue;
                        }
                        int last = inputText.size() - 1;
                        if (! inputText.get(last).isSpaceText()){
                            overflow.add(inputText.remove(last));
                        }
                        overflow.add(text);
                    } else {
                        /// text fit line
                        if (text.getHeight() > textHeight){
                            textHeight = text.getHeight();
                        }
                        inputText.add(text);
                        curWidth += text.getWidth();
                    }
                } else {
                    overflow.add(text);
                }
            }
            return overflow == null? new ArrayList<>(): overflow;
        }

        public float getHeight(){
            return (divLines.get(divLines.size() - 1) == this?
                    divBottomSpacing: 0f
                ) + textHeight * divLeading;
        }

        public float getTextHeight(){
            return textHeight;
        }

        public float getWidth(){
            return curWidth;
        }

        @Override
        protected List<ContentText> delegate(){
            return ImmutableList.copyOf(inputText);
        }
    }

    private ArrayList<Line> divLines;
    private Optional<String> divPrefix;
    private float divPrefixDistance;
    private float divWidth;
    private float divFirstIndent;
    private float divIndent;
    private float divLeading;
    private float divBottomSpacing;
    private LineAlignment divAlignment;

    private boolean noFormatting;

    public DivisionLine(float width){
        this (width, LineAlignment.LEFT);
    }

    public DivisionLine(float width, LineAlignment alignment){
        divLines = new ArrayList<>();
        divLeading = 2;
        divFirstIndent = 0;
        divIndent = 0;
        divWidth = width;
        divAlignment = alignment;
        divBottomSpacing = 0;
        divPrefix = Optional.empty();
        divPrefixDistance = 0f;
        noFormatting = false;
    }

    public static DivisionLine splitItem(DivisionLine item){
        DivisionLine ans = copyFormat(item);
        ans.divFirstIndent = ans.divIndent;
        ans.divPrefix = Optional.empty();
        ans.divPrefixDistance = 0f;
        return ans;
    }

    public static DivisionLine copyFormat(DivisionLine item){
        DivisionLine ans = new DivisionLine(item.divWidth, item.divAlignment);
        ans.divLeading = item.divLeading;
        ans.divFirstIndent = item.divFirstIndent;
        ans.divIndent = item.divIndent;
        ans.divBottomSpacing = item.divBottomSpacing;
        ans.divPrefix = item.divPrefix;
        ans.divPrefixDistance = item.divPrefixDistance;
        return ans;
    }

    public DivisionLine setLeading(float leading){
        reflowText();
        divLeading = leading;
        return this;
    }

    public float getLeading(){
        return divLeading;
    }

    public DivisionLine setFirstIndent(float indent){
        reflowText();
        divFirstIndent = indent;
        return this;
    }

    public DivisionLine setIndent(float indent){
        reflowText();
        divIndent = indent;
        return this;
    }

    public DivisionLine setBottomSpacing(float padding){
        reflowText();
        divBottomSpacing = padding;
        return this;
    }

    public float getHeight(){
        float ans = 0;
        for (Line line: divLines){
            ans += line.getHeight();
        }
        return ans;
    }

    public float getHeight(int line){
        return get(line).getHeight();
    }

    public float getWidth(){
        return divWidth;
    }

    LineAlignment getLineAlignment(){
        return divAlignment;
    }

    public DivisionLine setLineAlignment(LineAlignment alignment){
        divAlignment = alignment;
        return this;
    }

    public DivisionLine addLine(Line line){
        divLines.add(line);
        // reflowText();
        return this;
    }

    public DivisionLine setPrefix(String prefix, float distance){
        divPrefix = Optional.of(prefix);
        divPrefixDistance = distance;
        return this;
    }

    public float getPrefixDistance(){
        return divPrefixDistance;
    }

    public Optional<String> getPrefix(){
        return divPrefix;
    }

    public DivisionLine appendSimpleText(String text, ContentFont font)
            throws IOException{
        appendText(text, font);
        return this;
    }

    public ArrayList<ContentText> appendText(String text, ContentFont font)
            throws IOException{
        Line line;
        if (divLines.isEmpty()){
            line = new Line(divWidth - divFirstIndent, divFirstIndent);
            divLines.add(line);
        } else {
            line = divLines.get(divLines.size() - 1);
        }
        /// Append text to the previous line
        ArrayList<ContentText> data = ContentText.createWords(text, font);
        appendText(line.appendText(data));
        return data;
    }

    private void appendText(ArrayList<ContentText> overflow){
        if (overflow.isEmpty()) return;
        Line line = new Line(divWidth - divIndent, divIndent);
        divLines.add(line);
        /// recursively call children
        appendText(line.appendText(overflow));
        noFormatting = true;
    }

    @Override
    protected List<Line> delegate(){
        return ImmutableList.copyOf(divLines);
    }

    private void reflowText(){
        if (noFormatting){
            throw new IllegalStateException("No formatting allowed");
        }
        /* it is bugged
        /// Load the data
        ArrayList<ContentText> data = new ArrayList<>();
        for (Line line: divLines){
            data.addAll(line);
        }
        /// Clear lines and redo all
        divLines.clear();
        divLines.add(new Line(divWidth - divFirstIndent, divFirstIndent));
        appendText(data);*/
    }
}