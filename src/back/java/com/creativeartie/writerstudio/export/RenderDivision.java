package com.creativeartie.writerstudio.export;

interface RenderDivision<T extends Number>{

    public void render(Division<T> division);

    public RenderContentFont getContentRender();
}
