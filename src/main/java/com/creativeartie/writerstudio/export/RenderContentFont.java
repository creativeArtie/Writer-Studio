package com.creativeartie.writerstudio.export;

/** Draws contents on the page
 */
interface RenderContentFont<T extends Number> extends RenderContent<T>{

    public T getWidth(String text);

    public T getHeight(String text);

    public String[] splitText(String text, T width);
}
