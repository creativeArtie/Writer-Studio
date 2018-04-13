package com.creativeartie.writerstudio.export;

import java.io.*; // AutoClosable, IOException

import org.apache.pdfbox.pdmodel.*; // PDDocument

import com.creativeartie.writerstudio.export.value.*; // ContentFont

import static com.creativeartie.writerstudio.main.Checker.*;

/** Renders a section with one of more pages.
 *
 * Purpose:
 * <ul>
 * <li>Decided what to layout</li>
 * <li>Manage PDF pages</li>
 * <li>Fill {@link MatterArea}.</li>
 * </ul>
 */
abstract class Section implements AutoCloseable{

    private WritingExporter sectionParent;

    /** Only constructor
     *
     */
     Section(WritingExporter parent){
        checkNotNull(parent, "parent");
        sectionParent = parent;
    }

    /** gets the file input
     * @return answer
     */
    final WritingExporter getParent(){
        return sectionParent;
    }

    /** gets the file output
     * @return answer
     */
    final PDDocument getPdfDocument(){
        return sectionParent.getPdfDocument();
    }

    /** Creates a new font.
     *
     * Same as {@code getParent().new PdfFont()}.
     *
     * @return answer
     */
    final ContentFont newFont(){
        return sectionParent.new PdfFont();
    }

    /// method is here to resolve the warning: <pre> auto-closeable resource
    /// Section has a member method close() that could throw
    /// InterruptedException </pre>
    @Override
    public void close() throws IOException{}
}