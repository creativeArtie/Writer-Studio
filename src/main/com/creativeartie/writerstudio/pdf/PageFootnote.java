package com.creativeartie.writerstudio.pdf;

import java.io.*; // IOException
import java.util.*; // ArrayList, Optional

import com.creativeartie.writerstudio.pdf.value.*; // (many)
import com.creativeartie.writerstudio.lang.*; // SpanBranch
import com.creativeartie.writerstudio.lang.markup.*; // FormattedSpan, LinedSpanPointNote, LinedSpanCite

import static com.creativeartie.writerstudio.main.Checker.*;

/** Renders the footnote of a page
 *
 * Purpose:
 * <ul>
 * <li>Render the footnote</li>
 * <li>Calculate the footnote number</li>
 * </ul>
 */
final class PageFootnote {
    private SectionContent<?> insertPage;
    private Optional<MatterArea> footnoteArea;
    private DivisionLine firstLine;

    private ArrayList<SpanBranch> insertedItems;
    private ArrayList<SpanBranch> pendingItems;
    private ArrayList<DivisionTextNote> pendingLines;

    /** Only Constructor
     *
     * @param parent
     *      parent section; not null
     */
    PageFootnote(SectionContent<?> parent){
        checkNotNull(parent, "parent");

        insertPage = parent;
        footnoteArea = Optional.empty();
        firstLine = new DivisionLine(parent.getPage().getRenderWidth());

        insertedItems = new ArrayList<>();
        pendingItems = new ArrayList<>();
        pendingLines = new ArrayList<>();
    }

    /** Adding a footnote into the {@link ContentText}, called by
     * {@link DivisionTextFormat}.
     *
     * This returns the current (temperory) number
     *
     * @param span
     *      footnote span; not null
     * @return answer
     * @throws IOException
     *         exception with content parsing
     */
    String addFootnote(SpanBranch span) throws IOException{
        checkNotNull(span, "span");

        /// check if span is in the inserted list
        int index = insertedItems.indexOf(span);
        boolean adding;

        /// check if span is in the pending list
        if (index == -1){
            index = pendingItems.indexOf(span);
            if (index != -1){
                index += insertedItems.size();
            }
        }

        /// add a new span to the pending list if not found
        if (index == -1){
            pendingItems.add(span);
            index = insertedItems.size() + pendingItems.size();
            adding = true;
        } else {
            index++;
            adding = false;
        }

        String ans = Utilities.toNumberSuperscript(index);

        /// add the span if none found
        if (adding){
            DivisionTextNote insert = new DivisionTextNote(insertPage);
            insert.setLeading(1);
            insert.setNumbering(ans);

            if (span instanceof LinedSpanPointNote){
                LinedSpanPointNote note = (LinedSpanPointNote) span;
                Optional<FormattedSpan> content = note.getFormattedSpan();
                if (content.isPresent()){
                    insert.addContent(content.get());
                }

            } else if (span instanceof LinedSpanCite){
                Optional<FormattedSpan> data = ((LinedSpanCite)span).getData()
                    .filter(s -> s instanceof InfoDataSpanFormatted)
                    .map(s -> ((InfoDataSpanFormatted)s).getData());
                if (data.isPresent()){
                    insert.addContent(data.get());
                }
            }
            pendingLines.add(insert);
        }

        assert pendingItems.size() == pendingLines.size();
        return ans;
    }

    /** Get the height with the adding footnotes of a line
     *
     * @param line
     *      adding line; not null, footnote span found must be already added
     * @return answer
     */
    float getHeight(DivisionText.Line line){
        checkNotNull(line, "line");

        float ans = footnoteArea.map(a -> a.getHeight())
            .orElse(firstLine.getHeight());
        for (ContentText text: line){
            /// ans += footnote != null? getHeight(footnote): 0f
            ans += text.getFootnote().map(s -> getHeight(s)).orElse(0f);
        }
        return ans;
    }

    public float getHeight(){
        return footnoteArea.map(a -> a.getHeight()).orElse(0f);
    }

    /** Finds the assoicated line and return its height
     *
     * @param span; not null
     *      target span
     * @return answer
     * @see getHeight(DivisionText.Line)
     */
    private float getHeight(SpanBranch span){
        checkNotNull(span, "span");

        if (! insertedItems.contains(span)){
            int index = pendingItems.indexOf(span);
            if (index == -1){
                throw new IllegalArgumentException("Span not found: " + span);
            }
            return pendingLines.get(index).getHeight();
        }

        /// don't add to the height if already inserted
        return 0f;
    }

    /** Insert a line into a pending list, but not into the footnote area.
     *
     * @param line; not null
     *      inserting lines; not null, next line of the last call's parameter
     * @return self
     */
    PageFootnote insertPending(DivisionText.Line line){
        checkNotNull(line, "line");

        int i = 0;
        for (ContentText content: line){
            content.getFootnote().ifPresent(s -> insertPending(s));
            i++;
        }
        return this;
    }

    /** Move the footnote into the inserted item, as needed
     * @param span
     *      inserting footnote span; not null
     * @see insertPending(DivisionText.Line)
     */
    private void insertPending(SpanBranch span){
        assert span != null;

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

    /** Gets or creates the footnote area
     * @return answer
     * @see insertPending(SpanBranch)
     */
    private MatterArea getFootnoteArea(){
        if (!footnoteArea.isPresent()){
            footnoteArea = Optional.of(new MatterArea(insertPage.getPage(),
                PageAlignment.BOTTOM));
            footnoteArea.get().add(firstLine);
        }
        return footnoteArea.get();
    }

    /** Move to the next page and returns the footnote {@link MatterArea}.
     * @return answer
     */
    MatterArea nextPage(){
        MatterArea area = footnoteArea.orElse(
            new MatterArea(insertPage.getPage(), PageAlignment.BOTTOM)
        );
        footnoteArea = Optional.empty();
        insertedItems.clear();
        return area;
    }

    /** Reset the footnote number of a text.
     * @param text
     *      output text; not null
     * @throws IOException
     *         exception with content parsing
     */
    void resetFootnote(ContentText text) throws IOException{
        checkNotNull(text, "text");

        Optional<SpanBranch> span = text.getFootnote();
        if (span.isPresent()){
            text.setText(resetFootnote(span.get()));
        }
    }

    /** Change the notenote number and return it
     * @param span
     *      footnote span; not null
     * @return answer
     * @throws IOException
     *         exception with content parsing
     */
    private String resetFootnote(SpanBranch span) throws IOException{
        checkNotNull(span, "span");

        int index = pendingItems.indexOf(span);
        if (index == -1){
            throw new IllegalArgumentException("Span not found: " + span);
        }

        String ans = Utilities.toNumberSuperscript(index + 1);
        pendingLines.get(index).setNumbering(ans);

        return ans;
    }
}
