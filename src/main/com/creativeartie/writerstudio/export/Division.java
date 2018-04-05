package com.creativeartie.writerstudio.export;

import java.util.*; // List

import com.creativeartie.writerstudio.export.value.*; // ContentPostEditor

import org.apache.pdfbox.pdmodel.common.*; // PDRectangle

/**  A lines of text of a single drawing.
 * Purposes
 * <ul>
 * <li>defines where the pointer goes to.</li>
 * <li>render non-text related things</li>
 * </ul>
 */
interface Division{

    /** Get the height of the all the whole division.
     * @return answer
     * @see #getWidth()
     */
    public float getHeight();

    /** Get the width of the all the whole division.
     * @return answer
     * @see #getHeight()
     */
    public float getWidth();

    /** Get the starting point to print the text.
     * @return answer
     */
    public float getStartY();

    /** Add rendering that is not related to printing of text
     *
     * @param rect
     *      the location of the text
     * @return a list of function showing what need to be done
     * @see ContentText#getPostConsumer(PDRectangle)
     */
    public List<ContentPostEditor> getPostTextConsumers(PDRectangle rect);
}