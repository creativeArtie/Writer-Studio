package com.creativeartie.writerstudio.export;

/** Draws contents on the page */
abstract class Content<T extends Number>{
    private final RenderContent<T> contentRenderer;

    Content(RenderContent<T> renderer){
        contentRenderer = renderer;
    }

    void render(RenderContent<T> renderer){
        renderer.render(this);
    }

    abstract T getWidth();

    abstract T getHeight();
}
