package com.creativeartie.writer.export;

public class OutputPageInfo {
    private final int pageNumber;
    private final OutputNoteInfo endnoteList;
    private final OutputNoteInfo footnoteList;

    public OutputPageInfo(int page, OutputNoteInfo endnotes){
        pageNumber = page;
        endnoteList = endnotes;
        footnoteList = new OutputNoteInfo();
    }

    public int getPageNumber(){
        return pageNumber;
    }
}
