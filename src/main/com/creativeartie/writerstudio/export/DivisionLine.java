package com.creativeartie.writerstudio.export;

import java.io.*;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.*;

public class DivisionLine{
    /*

    public class Line extends ForwardingList<FormatterData>{
        private ArrayList<FormatterData> inputText;
        private float maxWidth;
        private float curWidth;
        private float textHeight;
        private float lineIndent;

        private Line(float width, float indent){
            this(width, new ArrayList<>());
            lineIndent = indent;
        }

        private Line(float width, ArrayList<FormatterData> text){
            inputText = text;
            maxWidth = width;
            textHeight = 0;
            curWidth = 0;
        }

        public float getIndent(){
            return lineIndent;
        }

        private ArrayList<FormatterData> appendText(ArrayList<FormatterData> texts){
            ArrayList<FormatterData> overflow = null;
            for (FormatterData text: texts){
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
        protected List<FormatterData> delegate(){
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
    private TextAlignment divAlignment;

    public FormatterItem(float width){
        this (width, TextAlignment.LEFT);
    }

    public FormatterItem(float width, TextAlignment alignment){
        divLines = new ArrayList<>();
        divLeading = 2;
        divFirstIndent = 0;
        divIndent = 0;
        divWidth = width;
        divAlignment = alignment;
        divBottomSpacing = 0;
        divPrefix = Optional.empty();
        divPrefixDistance = 0f;
    }

    public static FormatterItem splitItem(FormatterItem item){
        FormatterItem ans = copyFormat(item);
        ans.divFirstIndent = ans.divIndent;
        ans.divPrefix = Optional.empty();
        ans.divPrefixDistance = 0f;
        return ans;
    }

    public static FormatterItem copyFormat(FormatterItem item){
        FormatterItem ans = new FormatterItem(item.divWidth, item.divAlignment);
        ans.divLeading = item.divLeading;
        ans.divFirstIndent = item.divFirstIndent;
        ans.divIndent = item.divIndent;
        ans.divBottomSpacing = item.divBottomSpacing;
        ans.divPrefix = item.divPrefix;
        ans.divPrefixDistance = item.divPrefixDistance;
        return ans;
    }

    public FormatterItem setLeading(float leading){
        reflowText();
        divLeading = leading;
        return this;
    }

    public float getLeading(){
        return divLeading;
    }

    public FormatterItem setFirstIndent(float indent){
        reflowText();
        divFirstIndent = indent;
        return this;
    }

    public FormatterItem setIndent(float indent){
        reflowText();
        divIndent = indent;
        return this;
    }

    public FormatterItem setBottomSpacing(float padding){
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

    TextAlignment getTextAlignment(){
        return divAlignment;
    }

    public FormatterItem setTextAlignment(TextAlignment alignment){
        divAlignment = alignment;
        return this;
    }

    public FormatterItem addLine(Line line){
        divLines.add(line);
        reflowText();
        return this;
    }

    public FormatterItem setPrefix(String prefix, float distance){
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

    public FormatterItem appendSimpleText(String text, SizedFont font)
            throws IOException{
        appendText(text, font);
        return this;
    }

    public ArrayList<FormatterData> appendText(String text, SizedFont font)
            throws IOException{
        Line line;
        if (divLines.isEmpty()){
            line = new Line(divWidth - divFirstIndent, divFirstIndent);
            divLines.add(line);
        } else {
            line = divLines.get(divLines.size() - 1);
        }
        /// Append text to the previous line
        ArrayList<FormatterData> data = FormatterData.createWords(text, font);
        appendText(line.appendText(data));
        return data;
    }

    private FormatterItem appendText(ArrayList<FormatterData> overflow){
        if (overflow.isEmpty()) return this;
        Line line = new Line(divWidth - divIndent, divIndent);
        divLines.add(line);
        /// recursively call children
        appendText(line.appendText(overflow));
        return this;
    }

    @Override
    protected List<Line> delegate(){
        return ImmutableList.copyOf(divLines);
    }

    private void reflowText(){
        /// Load the data
        ArrayList<FormatterData> data = new ArrayList<>();
        for (Line line: divLines){
            data.addAll(line);
        }
        /// Clear lines and redo all
        divLines.clear();
        divLines.add(new Line(divWidth - divFirstIndent, divFirstIndent));
        appendText(data);
    }
    */
}