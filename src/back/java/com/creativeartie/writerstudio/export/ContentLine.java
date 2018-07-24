package com.creativeartie.writerstudio.export;

/** Draws contents on the page */
final class ContentLine<T extends Number> extends Content<T>{

    private final T lineWidth;
    private final T lineHeight;

    ContentLine(RenderContent<T> renderer, T width, T height){
        super(renderer);
        lineWidth = width;
        lineHeight = height;

    }

    T getWidth(){
        return lineWidth;
    }

    T getHeight(){
        return lineHeight;
    }
}
