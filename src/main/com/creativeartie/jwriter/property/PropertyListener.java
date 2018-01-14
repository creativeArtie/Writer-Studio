package com.creativeartie.jwriter.property;

public interface PropertyListener<T>{
    public void update(Property<T> property, T value);
}
