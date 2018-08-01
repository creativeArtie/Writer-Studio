package com.creativeartie.writerstudio.export;

public interface RenderData<T extends Number> {

    public String[] splitContent(String text, T width);

    public T getWidth(ContentData style, String text);

    public T getHeight(ContentData style, String text);
}
