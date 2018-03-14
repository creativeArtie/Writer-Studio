package com.creativeartie.jwriter.pdf;

import java.io.*;
import java.util.*;

import org.apache.pdfbox.pdmodel.*;

import com.google.common.collect.*;

/**
 * Prints the footnotes and do some preview of changes.
 */
class PdfMatterFootnote extends PdfMatter{

    @Override
    float getWidth(){
        return 0f;
    }

    @Override
    public float getXLocation(){
        return 0;
    }

    @Override
    public float getYLocation(){
        return 0;
    }

    @Override
    public List<PdfItem> delegate(){
        return null;
    }
}