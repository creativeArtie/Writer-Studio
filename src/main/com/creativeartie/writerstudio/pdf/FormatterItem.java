package com.creativeartie.writerstudio.pdf;

import java.io.*;
import java.util.*;

import com.google.common.collect.*;

import com.creativeartie.writerstudio.pdf.value.*;

/**
 * Defines the number of lines needed to allow text to fit into a region
 */
class FormatterItem extends ForwardingList<FormatterItem.Line>{
    public class Line extends ForwardingList<FormatterData>{
        private ArrayList<FormatterData> inputText;
        private float maxWidth;
        private float curWidth;
        private float textHeight;

        private Line(float width){
            this(width, new ArrayList<>());
        }

        private Line(float width, ArrayList<FormatterData> text){
            inputText = text;
            maxWidth = width;
            textHeight = 0;
            curWidth = 0;
        }

        private ArrayList<FormatterData> appendText(String string, SizedFont font)
                throws IOException{
            return appendText(FormatterData.createWords(string, font));
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
            return textHeight * divLeading;
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
    private float divWidth;
    private float divFirstIndent;
    private float divIndent;
    private float divLeading;
    private float divTopSpacing;
    private boolean newPage;
    private Margin divMargin;
    private TextAlignment divAlignment;
    private boolean noEdited;

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
        divTopSpacing = 0;
        newPage = false;
        noEdited = false;
    }

    public static FormatterItem copySplitItem(FormatterItem item){
        FormatterItem ans = new FormatterItem(item.divWidth, item.divAlignment);
        ans.divLeading = item.divLeading;
        ans.divFirstIndent = item.divIndent;
        ans.divIndent = item.divIndent;
        ans.divTopSpacing = item.divTopSpacing;
        ans.newPage = false;
        return ans;
    }

    public static FormatterItem copyFormat(FormatterItem item){
        FormatterItem ans = new FormatterItem(item.divWidth, item.divAlignment);
        ans.divLeading = item.divLeading;
        ans.divFirstIndent = item.divFirstIndent;
        ans.divIndent = item.divIndent;
        ans.divTopSpacing = item.divTopSpacing;
        ans.newPage = item.newPage;
        return ans;
    }

    public FormatterItem setLeading(float leading){
        formatChanged();
        divLeading = leading;
        return this;
    }

    public FormatterItem setFirstIndent(float indent){
        formatChanged();
        divFirstIndent = indent;
        return this;
    }

    public FormatterItem setIndent(float indent){
        formatChanged();
        divIndent = indent;
        return this;
    }

    public FormatterItem setNewPage(boolean b){
        newPage = b;
        return this;
    }

    public FormatterItem setTopSpacing(float padding){
        divTopSpacing = padding;
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
        formatChanged();
        return this;
    }

    public FormatterItem appendText(String text, SizedFont font) throws IOException{
        Line line;
        if (divLines.isEmpty()){
            line = new Line(divWidth - divFirstIndent);
            divLines.add(line);
        } else {
            line = divLines.get(divLines.size() - 1);
        }
        /// Append text to the previous line
        appendText(line.appendText(text, font));
        noEdited = true;
        return this;
    }

    private FormatterItem appendText(ArrayList<FormatterData> overflow){
        if (overflow.isEmpty()) return this;
        Line line = new Line(divWidth - divIndent);
        divLines.add(line);
        /// recursively call children
        appendText(line.appendText(overflow));
        return this;
    }

    @Override
    protected List<Line> delegate(){
        return ImmutableList.copyOf(divLines);
    }

    private void formatChanged(){
        // TODO redo adding text
        if (noEdited){
            throw new IllegalStateException("Format can not be change after text added.");
        }
    }
}