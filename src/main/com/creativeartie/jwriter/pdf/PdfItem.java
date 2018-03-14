package com.creativeartie.jwriter.pdf;

import java.io.*;
import java.util.*;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.*;
import com.google.common.collect.*;

import com.creativeartie.jwriter.pdf.value.*;

/**
 * Represent a single line of writing text, like paragraph, list item, text box,
 * etc.
 */
class PdfItem extends ForwardingList<PdfItem.Line>{
    public class Line extends ForwardingList<PdfData>{
        private ArrayList<PdfData> inputText;
        private float maxWidth;
        private float curWidth;
        private float textHeight;

        private Line(float width){
            this(width, new ArrayList<>());
        }

        private Line(float width, ArrayList<PdfData> text){
            inputText = text;
            maxWidth = width;
            textHeight = 0;
            curWidth = 0;
        }

        private ArrayList<PdfData> appendText(String string, SizedFont font)
                throws IOException{
            return appendText(PdfData.createWords(string, font));
        }

        private ArrayList<PdfData> appendText(ArrayList<PdfData> texts){
            ArrayList<PdfData> overflow = null;
            for (PdfData text: texts){
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
        protected List<PdfData> delegate(){
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
    private boolean noEdited;
    private TextAlignment divAlignment;

    public PdfItem(float width){
        this (width, TextAlignment.LEFT);
    }

    public PdfItem(float width, TextAlignment alignment){
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

    public PdfItem setLeading(float leading){
        formatChanged();
        divLeading = leading;
        return this;
    }

    public PdfItem setFirstIndent(float indent){
        formatChanged();
        divFirstIndent = indent;
        return this;
    }

    public PdfItem setIndent(float indent){
        formatChanged();
        divIndent = indent;
        return this;
    }

    public PdfItem setNewPage(boolean b){
        newPage = b;
        return this;
    }

    public PdfItem setTopSpacing(float padding){
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

    TextAlignment getTextAlignment(){
        return divAlignment;
    }

    public PdfItem setTextAlignment(TextAlignment alignment){
        divAlignment = alignment;
        return this;
    }

    public PdfItem appendText(String text, SizedFont font) throws IOException{
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

    private PdfItem appendText(ArrayList<PdfData> overflow){
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