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
    private ArrayList<DivisionLine> pendingLines;
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

    public float getHeight(DivisionLine.Line line){
        float ans = getHeight();
        for (ContentText text: line){
            ans += text.getTarget().map(s -> getHeight(s)).orElse(0f);
        }
        return ans;
    }

    private float getHeight(SpanBranch checking){
        if (! insertedItems.contains(checking)){
            int index = pendingItems.indexOf(checking);
            if (index == -1){
                throw new IllegalArgumentException("Span not found:" +
                    checking);
            }
            return pendingLines.get(index).getHeight();
        }
        return 0f;
    }

    public MatterArea nextPage(){
        insertedItems.clear();
        MatterArea area = footnoteArea.orElse(new MatterArea(insertPage.getPage(),
            PageAlignment.BOTTOM));
        footnoteArea = Optional.empty();
        return area;
    }

    public String prepFootnote(SpanBranch target)
            throws IOException{
        System.out.println(target);
        int index = insertedItems.indexOf(target);
        boolean adding;
        System.out.println(index);
        if (index == -1){
            index = pendingItems.indexOf(target);
            if (index != -1){
                index += insertedItems.size();
            }
        }
        System.out.println(index);
        if (index == -1){
            pendingItems.add(target);
            index = insertedItems.size() + pendingItems.size();
            adding = true;
        } else {
            index++;
            adding = false;
        }
        System.out.println(index);
        String ans = Utilities.toNumberSuperscript(index);
        if (adding){
            if (target instanceof LinedSpanPointNote){
                LinedSpanPointNote note = (LinedSpanPointNote) target;
                DivisionLineFormatted insert = insertPage.newFormatDivision();
                if (footnoteArea.isPresent()){
                    insert.setLeading(1);
                }
                insert.appendSimpleText(ans, insertPage.newFont()
                    .changeToSuperscript());
                Optional<FormatSpanMain> content = note.getFormattedSpan();
                if (content.isPresent()){
                    insert.addContent(content.get());
                }
                pendingLines.add(insert);
            }
        }
        return ans;
    }

    public PageFootnote insertAll(){
        insertedItems.addAll(pendingItems);
        pendingItems.clear();
        pendingLines.clear();
        getFootnoteArea().addAll(pendingLines);
        return this;
    }

    public PageFootnote insertPending(SpanBranch span){
        if (insertedItems.contains(span)){
            return this;
        }
        if (pendingItems.contains(span)){
            if (pendingItems.indexOf(span) == 0){
                insertedItems.add(pendingItems.remove(0));
                getFootnoteArea().add(pendingLines.remove(0));
            } else {
                throw new IllegalArgumentException("Span is not the first: " +
                    span);
            }
            return this;
        }
        throw new IllegalArgumentException("Span not found: " + span);
    }

    private MatterArea getFootnoteArea(){
        if (!footnoteArea.isPresent()){
            footnoteArea = Optional.of(new MatterArea(insertPage.getPage(),
                PageAlignment.BOTTOM));
        }
        return footnoteArea.get();
    }
}
