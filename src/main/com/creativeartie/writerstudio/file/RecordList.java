package com.creativeartie.writerstudio.file;

import java.util.*; // ArrayList Iterator, List, Scanner;
import java.time.*; // Duration, LocalDate, YearMonth;

import com.google.common.collect.*; // AbstractIterator, ForwardingListm, ImmutableList;

import com.creativeartie.writerstudio.lang.markup.*; // WritingText;

import static com.creativeartie.writerstudio.main.ParameterChecker.*;

/** Stores a list of {@link Record}
 *
 * Purpose:
 * <ul>
 * <li> Loads and save a list of {@link Record records}.</li>
 * <li> Gets & edits the current record </li>
 * <li> Organize and navigates records in {@link YearMonth}.</li>
 * </ul>
 */
public final class RecordList extends ForwardingList<Record>{

    /// %Part 1: Constructors ##################################################
    /// %Part 1.1: Static constructors =========================================

    /** Creates {@link RecordList} with a file
     *
     * @param file
     *      record file
     * @return answer
     */
    @Deprecated
    public static RecordList build(java.io.File file)
            throws java.io.IOException{
        return new RecordList(argumentNotNull(file, "file"));
    }

    /// %Part 1.2: Instance Setup ==============================================

    private final ArrayList<Record> recordList;

    /** Creates an empty {@linkplain RecordList}.
     *
     * @see ManuscriptFile#withManuscript(WritingText)
     * @see ManuscriptFile#newFile()
     */
    RecordList(){
        recordList = new ArrayList<>();
        recordList.add(Record.firstRecord());
    }

    /** Creates a {@ RecordList} with the extract file text.
     *
     * @param text
     *      extracted file text
     * @see ManuscriptFile#open(File)
     */
    RecordList(String text){
        argumentNotNull(text, "text");

        recordList = new ArrayList<>();
        fillData(new Scanner(text));
    }

    /** Creates a {@link RecordList} from a file.
     *
     * @param file
     *      input file
     * @see #build(java.io.File)
     */
    @Deprecated
    private RecordList(java.io.File file) throws java.io.IOException{
        assert file != null: "Null file.";

        recordList = new ArrayList<>();
        try (Scanner data = new Scanner(file)){
            fillData(data);
        }
    }

    /// %Part 2: Loading & Saving ##############################################

    /** Fills record list from a {@linkplain Scanner}
     *
     * @param data
     *      unparsed data
     * @see #RecordList(String)
     * @see #RecordList(File)
     */
    private void fillData(Scanner data){
        assert data != null: "Null data";
        int written = 0;
        Record current = null;

        /// Fill the list
        while (data.hasNextInt()){
            current = Record.builder(current, LocalDate.ofYearDay(
                    data.nextInt(), data.nextInt())
                )
                .setPublishTotal(data.nextInt())
                .setNoteTotal(data.nextInt())
                .setWriteDuration(Duration.parse(data.next()))
                .setPublishGoal(data.nextInt())
                .setTimeGoal(Duration.parse(data.next()))
                .build();
            recordList.add(current);
        }

        /// Add today record, as neeeded.
        updateRecord(current.getPublishTotal(), current.getNoteTotal());
    }

    /** Get the save text to put into a file.
     *
     * @return answer
     * @see ManuscriptFile#save()
     */
    String getSaveText(){
        StringBuilder ans = new StringBuilder();

        for (Record out: this){
            ans.append(out.getRecordDate().getYear())     .append(" ");
            ans.append(out.getRecordDate().getDayOfYear()).append(" ");
            ans.append(out.getPublishTotal())             .append(" ");
            ans.append(out.getNoteTotal())                .append(" ");
            ans.append(out.getWriteTime())                .append(" ");
            ans.append(out.getPublishGoal())              .append(" ");
            ans.append(out.getTimeGoal())                 .append("\n");
        }

        return ans.toString();
    }

    /// %Part 3: Record Get and Set ############################################

    /** Get the today's current record.
     *
     * @return answer
     */
    public Record getRecord(){
        assert ! recordList.isEmpty() : "empty recordList";
        return recordList.get(recordList.size() - 1);
    }

    /** Starts to record time and update word count.
     *
     * Adds a new records if date have changed.
     *
     * @param doc
     *      word count from document
     */
    public void startWriting(WritingText doc){
        argumentNotNull(doc, "doc");
        int publish = doc.getPublishTotal();
        int note = doc.getNoteTotal();

        updateRecord(publish, note);
        getRecord().startWriting(publish, note);
    }

    /** Starts to record time and update word count.
     *
     * Adds a new records if date have changed.
     *
     * @param doc
     *      word count from document
     */
    public void stopWriting(WritingText doc){
        argumentNotNull(doc, "doc");
        int publish = doc.getPublishTotal();
        int note = doc.getNoteTotal();

        updateRecord(publish, note);
        getRecord().stopWriting(publish, note);
    }

    /** Updates the record, and create new record as needed.
     *
     * @param publish
     *      publishing word count
     * @param note
     *      note word count
     * @see #fillData(Scanner)
     * @see #startWriting(WritingText)
     * @see #stopWriting(WritingText)
     */
    private void updateRecord(int publish, int note){
        assert publish >= 0: "publish < 0";
        assert note >= 0: "note < 0";

        /// if current record != today, stop and add new
        Record record = getRecord();
        if (!record.getRecordDate().equals(LocalDate.now())){
            record.stopWriting(publish, note);
            recordList.add(Record.newRecord(record));
        }
    }

    /// %Part 4: Records and Months ############################################
    /// %Part 4.1: Records in a Month ==========================================

    /** Gets all records found in a {@link YearMonth month}.
     *
     * @param
     *      showing months
     * @return answer
     */
    public Iterator<Record> getMonth(YearMonth month){
        argumentNotNull(month, "month");

        /// Finds no Records
        if (getStartMonth().isAfter(month) || getEndMonth().isBefore(month)){
            return new AbstractIterator<Record>(){
                protected Record computeNext(){
                    return endOfData();
                }
            };
        }

        /// Finds a list of records
        return new AbstractIterator<Record>(){
            private int ptr = findMonth(month);

            protected Record computeNext(){
                /// This is the last record.
                if (ptr >= size()){
                    return endOfData();
                }

                /// Get and check the next record
                Record record = get(ptr);
                LocalDate date = record.getRecordDate();
                if (date.getYear()  == month.getYear() &&
                    date.getMonth() == month.getMonth()){
                    ptr++;
                    return record;
                }
                return endOfData();
            }
        };
    }

    /** Finds the first record index in a month.
     *
     * @param month
     *      showing month
     * @return answer
     * @see #getMonth(YearMonth)
     */
    private int findMonth(YearMonth month){
        assert month != null: "Null Month";

        int ptr = 0;
        for(Record record: this){
            LocalDate date = record.getRecordDate();
            if (date.getYear()  == month.getYear() &&
                date.getMonth() == month.getMonth()){
                /// month found
                return ptr;
            }
            ptr++;
        }
        return size();
    }

    /// %Part 4.2: First and Last Months =======================================
    /** Gets the {@link YearMonth} of the first record.
     *
     * @return answer
     */
    public YearMonth getStartMonth(){
        LocalDate start = recordList.get(0).getRecordDate();
        return YearMonth.of(start.getYear(), start.getMonth());
    }

    /** Gets the {@link YearMonth} of the last record.
     *
     * @return answer
     */
    public YearMonth getEndMonth(){
        LocalDate end = recordList.get(size() - 1).getRecordDate();
        return YearMonth.of(end.getYear(), end.getMonth());
    }

    /// %Part 5: Overrides Methods =============================================

    @Override
    protected List<Record> delegate(){
        return ImmutableList.copyOf(recordList);
    }
}