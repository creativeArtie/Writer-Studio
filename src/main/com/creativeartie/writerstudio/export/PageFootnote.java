package com.creativeartie.writerstudio.export;

import java.util.*;
import java.io.*;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.*;

import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.*;
import com.creativeartie.writerstudio.export.value.*;

public class PageFootnote {
    private ArrayList<SpanBranch> insertedItems;
    private ArrayList<SpanBranch> pendingItems;
    private ArrayList<DivisionTextNote> pendingLines;
    private Optional<MatterArea> footnoteArea;
    private SectionContent<?> insertPage;

    public PageFootnote(SectionContent<?> parent){
        insertPage = parent;
        footnoteArea = Optional.empty();
        insertedItems = new ArrayList<>();
        pendingItems = new ArrayList<>();
        pendingLines = new ArrayList<>();
    }

    public float getHeight(){
        return footnoteArea.map(a -> a.getHeight()).orElse(0f) +
            (float) pendingLines.stream().mapToDouble(l -> l.getHeight()).sum();
    }

    public float getHeight(DivisionText.Line line){
        float ans = footnoteArea.map(a -> a.getHeight()).orElse(0f);
        for (ContentText text: line){
            ans += text.getTarget().map(s -> getHeight(s)).orElse(0f);
        }
        return ans;
    }

    private float getHeight(SpanBranch span){
        if (! insertedItems.contains(span)){
            int index = pendingItems.indexOf(span);
            if (index == -1){
                throw new IllegalArgumentException("Span not found: " + span);
            }
            return pendingLines.get(index).getHeight();
        }
        return 0f;
    }

    public MatterArea nextPage(){
        MatterArea area = footnoteArea.orElse(new MatterArea(insertPage.getPage(),
            PageAlignment.BOTTOM));
        insertedItems.clear();
        footnoteArea = Optional.empty();
        return area;
    }

    public void resetFootnote(ContentText text) throws IOException{
        Optional<SpanBranch> span = text.getTarget();
        if (span.isPresent()){
            text.setText(resetFootnote(span.get()));
        }
    }

    public String resetFootnote(SpanBranch span) throws IOException{
        int index = pendingItems.indexOf(span);
        if (index == -1){
            throw new IllegalArgumentException("Span not found: " + span);
        }
        String ans = Utilities.toNumberSuperscript(index + 1);
        pendingLines.get(index).setNumbering(ans);
        return ans;
    }

    public String addFootnote(SpanBranch span)
            throws IOException{
        int index = insertedItems.indexOf(span);
        boolean adding;
        if (index == -1){
            index = pendingItems.indexOf(span);
            if (index != -1){
                index += insertedItems.size();
            }
        }
        if (index == -1){
            pendingItems.add(span);
            index = insertedItems.size() + pendingItems.size();
            adding = true;
        } else {
            index++;
            adding = false;
        }
        String ans = Utilities.toNumberSuperscript(index);
        if (adding){
            if (span instanceof LinedSpanPointNote){
                LinedSpanPointNote note = (LinedSpanPointNote) span;
                DivisionTextNote insert = new DivisionTextNote(insertPage);
                insert.setLeading(1);
                insert.setNumbering(ans);
                Optional<FormatSpanMain> content = note.getFormattedSpan();
                if (content.isPresent()){
                    insert.addContent(content.get());
                }
                pendingLines.add(insert);
            }
        }

        assert pendingItems.size() == pendingLines.size();
        return ans;
    }

    public PageFootnote insertAll(){
        if (! pendingItems.isEmpty()){
            insertedItems.addAll(pendingItems);
            getFootnoteArea().addAll(pendingLines);
        }
        pendingItems.clear();
        pendingLines.clear();
        return this;
    }

    public PageFootnote insertPending(DivisionText.Line line){
        for (ContentText content: line){
            content.getTarget().ifPresent(s -> insertPending(s));
        }
        return this;
    }

    private void insertPending(SpanBranch span){
        if (insertedItems.contains(span)){
            return;
        }
        if (pendingItems.contains(span)){
            int index = pendingItems.indexOf(span);
             if (index != -1){
                insertedItems.add(pendingItems.remove(index));
                getFootnoteArea().add(pendingLines.remove(index));
                return;
            }
        }
        throw new IllegalArgumentException("Span not found: " + span);
    }

    private MatterArea getFootnoteArea(){
        if (!footnoteArea.isPresent()){
            footnoteArea = Optional.of(new MatterArea(insertPage.getPage(),
                PageAlignment.BOTTOM));
            footnoteArea.get().add(new DivisionLine(insertPage.getPage()
                .getRenderWidth()));
        }
        return footnoteArea.get();
    }
}
