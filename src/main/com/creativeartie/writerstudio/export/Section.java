package com.creativeartie.writerstudio.export;

import java.io.*;
import java.util.*;

import org.apache.pdfbox.pdmodel.*;

import com.creativeartie.writerstudio.file.*;
import com.creativeartie.writerstudio.resource.*;
import com.creativeartie.writerstudio.export.value.*;

public abstract class Section implements AutoCloseable{

    private WritingExporter sectionParent;

     Section(WritingExporter parent){
        sectionParent = parent;
    }

    PDDocument getPdfDocument(){
        return sectionParent.getPdfDocument();
    }

    WritingExporter getParent(){
        return sectionParent;
    }

    ContentFont newFont(){
        return sectionParent.new PdfFont();
    }

    /** method is here to resolve the warning: <pre> auto-closeable resource
     * Section has a member method close() that could throw
     * InterruptedException </pre>
     */
    @Override
    public void close() throws IOException{}
}