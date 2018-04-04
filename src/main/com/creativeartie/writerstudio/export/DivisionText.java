package com.creativeartie.writerstudio.export;

import java.io.*;
import java.util.*;

import com.google.common.collect.*;

import org.apache.pdfbox.pdmodel.common.*;

import com.creativeartie.writerstudio.export.value.*;

public class DivisionText extends ForwardingList<DivisionText.Line>
        implements Division{

    public class Line extends ForwardingList<ContentText>{
        private ArrayList<ContentText> inputText;
        private float maxWidth;
        private float curWidth;
        private float textHeight;
        private float lineIndent;

        private Line(float width, float indent){
            lineIndent = indent;
            inputText = new ArrayList<>();
            maxWidth = width;
            textHeight = 0;
            curWidth = 0;
        }

        public float getIndent(){
            return lineIndent;
        }

        private ArrayList<ContentText> appendText(List<ContentText> texts){
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

    public DivisionText(float width){
        this (width, LineAlignment.LEFT);
    }

    public DivisionText(float width, LineAlignment alignment){
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

    public static DivisionText splitItem(DivisionText item){
        DivisionText ans = copyFormat(item);
        ans.divFirstIndent = ans.divIndent;
        ans.divPrefix = Optional.empty();
        ans.divPrefixDistance = 0f;
        return ans;
    }

    public static DivisionText copyFormat(DivisionText item){
        DivisionText ans = new DivisionText(item.divWidth, item.divAlignment);
        ans.divLeading = item.divLeading;
        ans.divFirstIndent = item.divFirstIndent;
        ans.divIndent = item.divIndent;
        ans.divBottomSpacing = item.divBottomSpacing;
        ans.divPrefix = item.divPrefix;
        ans.divPrefixDistance = item.divPrefixDistance;
        return ans;
    }

    public DivisionText setLeading(float leading){
        divLeading = leading;
        reflowText();
        return this;
    }

    public float getLeading(){
        return divLeading;
    }

    public DivisionText setFirstIndent(float indent){
        divFirstIndent = indent;
        reflowText();
        return this;
    }

    public DivisionText setIndent(float indent){
        divIndent = indent;
        reflowText();
        return this;
    }

    public DivisionText setWidth(float width){
        divWidth = width;
        reflowText();
        return this;
    }

    public DivisionText setBottomSpacing(float padding){
        divBottomSpacing = padding;
        reflowText();
        return this;
    }

    @Override
    public float getHeight(){
        float ans = 0;
        for (Line line: divLines){
            ans += line.getHeight();
        }
        return ans;
    }

    @Override
    public float getStartY(){
        return isEmpty()? 0: get(0).getTextHeight() * (getLeading());
    }

    @Override
    public List<ContentPostEditor> getPostTextConsumers(PDRectangle rect){
        return new ArrayList<>();
    }

    public float getHeight(int line){
        return get(line).getHeight();
    }

    @Override
    public float getWidth(){
        return divWidth;
    }

    LineAlignment getLineAlignment(){
        return divAlignment;
    }

    public DivisionText setLineAlignment(LineAlignment alignment){
        divAlignment = alignment;
        return this;
    }

    public DivisionText setPrefix(String prefix, float distance){
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

    public List<ContentText> addLine(Line line){
        ArrayList<ContentText> add = new ArrayList<>();
        for (ContentText text: line){
            add.add(new ContentText(text));
        }
        appendText(add, getLine());
        return add;
    }

    public DivisionText appendSimpleText(String text, ContentFont font)
            throws IOException{
        appendText(text, font);
        return this;
    }

    public ArrayList<ContentText> appendText(String text, ContentFont font)
            throws IOException{
        /// Append text to the previous line
        ArrayList<ContentText> data = ContentText.createWords(text, font);
        appendText(data, getLine());
        return data;
    }

    private Line getLine(){
        if (divLines.isEmpty()){
            Line line = new Line(divWidth - divFirstIndent, divFirstIndent);
            divLines.add(line);
            return line;
        }
        return divLines.get(divLines.size() - 1);
    }

    private void appendText(List<ContentText> overflow){
        if (overflow.isEmpty()) return;
        Line line = new Line(divWidth - divIndent, divIndent);
        divLines.add(line);
        appendText(overflow, line);
    }

    private void appendText(List<ContentText> overflow, Line line){
        /// recursively call children
        appendText(line.appendText(overflow));
    }

    @Override
    protected List<Line> delegate(){
        return ImmutableList.copyOf(divLines);
    }

    private void reflowText(){
        if (noFormatting){
            return;
        }

        /// Load the data
        ArrayList<ContentText> data = new ArrayList<>();
        for (Line line: divLines){
            data.addAll(line);
        }
        /// Clear lines and redo all
        divLines.clear();
        Line first = new Line(divWidth - divFirstIndent, divFirstIndent);
        divLines.add(first);
        appendText(data, first);
    }
}