package com.creativeartie.writerstudio.export;

/** Draws contents on the page
 */
interface RenderContent<T extends Number> {

    void render(Content content);
}
