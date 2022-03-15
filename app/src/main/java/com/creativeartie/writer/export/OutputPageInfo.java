package com.creativeartie.writer.export;

public class OutputPageInfo {
    private final int pageNumber;
    public OutputPageInfo(int page, OutputNoteInfo endnotes){
        pageNumber = page;
        new OutputNoteInfo();
    }

    public int getPageNumber(){
        return pageNumber;
    }
}
