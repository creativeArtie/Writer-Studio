package com.creativeartie.writerstudio.export;

import java.util.*; // List

import org.apache.pdfbox.pdmodel.common.*; // PDRectangle

import com.creativeartie.writerstudio.export.value.*; // ContentPostEditor

/**  A lines of text of a single drawing.
 * Purposes
 * <ul>
 * <li>defines where the pointer goes to.</li>
 * <li>render non-text related things</li>
 * </ul>
 */
interface Division{

    /** Get the starting point to print the text.
     * @return answer
     * @see #getHeight()
     * @see #getWidth()
     */
    public float getStartY();

    /** Get the height of the all the whole division.
     * @return answer
     * @see #getWidth()
     * @see #getStartY()
     */
    public float getHeight();

    /** Get the width of the all the whole division.
     * @return answer
     * @see #getHeight()
     * @see #getStartY()
     */
    public float getWidth();

    /** Add rendering that is not related to printing of text
     *
     * @param rect
     *      the location of the text
     * @return a list of function showing what need to be done
     * @see ContentText#getPostConsumer(PDRectangle)
     */
    public List<ContentPostEditor> getPostTextConsumers(PDRectangle rect);
}