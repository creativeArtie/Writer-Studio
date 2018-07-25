package com.creativeartie.writerstudio.export;

/** Creates the render box. */
interface ExportBase<T extends Number> {
    public FactoryRender<T> getRender();

    public default RenderContent<T> getRenderContent(){
        return getRender().getRenderContent();
    }
}
