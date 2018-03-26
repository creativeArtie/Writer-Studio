package com.creativeartie.writerstudio.pdf;

import java.util.*;

import com.creativeartie.writerstudio.file.*;
import com.creativeartie.writerstudio.resource.*;
import com.creativeartie.writerstudio.lang.markup.*;

import com.creativeartie.writerstudio.pdf.value.*;

/**
 * Gets the data from other packages.
 */
public class Data{

    /** Converts inches to points. */
    public static float inchToPoint(float inches){
        return inches * 72;
    }

    /** Converts centimeters to points. */
    public static float cmToPoint(float cm){
        return cm * 28.3465f;
    }

    /** Default page margin */
    private final Margin pageMargin;

    /** the file to export. */
    private final ManuscriptFile outputDoc;

    /** data related to the title. */
    private final Optional<DataTitle> dataTitle;

    /** data related to the content. */
    private final Optional<DataContent> dataContent;

    /** the parent data, if any.*/
    private final Optional<Data> dataMain;

    /** public constructor */
    public Data(ManuscriptFile doc){
        /// Must be create before creating the children data
        pageMargin = new Margin(cmToPoint(3f));
        outputDoc = doc;

        /// Creating the children data.
        dataTitle = Optional.of(new DataTitle(this));
        dataContent = Optional.of(new DataContent(this));
        dataMain = Optional.empty();
    }

    /**
     * package copy constructor for subclassing. This will make sure there is
     * only one instance of {@link DataTitle}, {@link DataWriting} for each
     * {@link ManuscriptFile}.*/
    Data(Data data){
        pageMargin = data.pageMargin;
        outputDoc = data.outputDoc;

        /// dataTitle and dataContent might not be created yet.
        dataTitle = Optional.empty();
        dataContent = Optional.empty();
        dataMain = Optional.of(data);
    }

    /** gets the page margin. */
    public Margin getMargin(){
        return pageMargin;
    }


    public WritingText getWritingText(){
        return outputDoc.getDocument();
    }

    public String getData(MetaData key){
        return outputDoc.getText(key);
    }

    public DataTitle getTitleData(){
        return dataTitle.orElseGet(() -> dataMain.get().getTitleData());
    }

    public DataContent getContentData(){
        return dataContent.orElseGet(() -> dataMain.get().getContentData());
    }
}