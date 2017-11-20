package com.creativeartie.jwriter.lang;

import java.util.*;
import com.google.common.collect.*;

import com.creativeartie.jwriter.main.*;

import static com.google.common.base.Preconditions.*;

public final class CatalogueIdentity implements Comparable<CatalogueIdentity>{
    private final ImmutableList<String> categoryPart;
    private final String namePart;

    public CatalogueIdentity(List<String> categories, Span span){
        checkNotNull(categories, "Categories list cannot be null.");
        checkNotNull(span, "Span cannot be null.");

        categoryPart = ImmutableList.copyOf(categories);

        int ptr = span.getStart();
        int padding = String.valueOf(span.getDocument().getLength())
            .length();
        namePart = String.format("%0" + padding + "d",  ptr);

    }

    public CatalogueIdentity(List<String> category, String name){
        checkNotNull(category, "Categories list cannot be null.");
        checkNotNull(name, "Name cannot be null.");

        categoryPart = ImmutableList.copyOf(category);
        namePart = name;
    }

    public CatalogueStatus getStatus(CatalogueMap parent){
        checkNotNull(parent, "Parent map can not be null.");

        return parent.get(this).getState();
    }

    public List<String> getCategories(){
        return categoryPart;
    }

    public String getName(){
        return namePart;
    }

    public String getFullIdentity(){
        StringBuilder builder = new StringBuilder();
        int i = 0;
        for(String category: getCategories()){
            builder.append(category);
            builder.append("-");
        }
        builder.append(getName());
        return builder.toString();
    }

    @Override
    public String toString(){
        return "(" + getFullIdentity() + ")";
    }

    @Override
    public int compareTo(CatalogueIdentity that){
        if (that == null){
            return 1;
        }
        return ComparisonChain.start()
            .compare(categoryPart, that.categoryPart, (o1, o2) -> compareCategory(o1, o2))
            .compare(namePart, that.namePart)
            .result();
    }

    private static int compareCategory(List<String> self, List<String> that){
        checkNotNull(self, "This object's category list (self) cannot be null");
        checkNotNull(self, "That object's category list (self) cannot be null");

        int i = 0;
        for (String cat: self){
            if (i >= that.size()){
                return 1;
            }
            int compare = cat.compareToIgnoreCase(that.get(i));
            if (compare != 0){
                return compare;
            }
            i++;
        }
        if (i < that.size()){
            return -1;
        }
        return 0;
    }

    @Override
    public boolean equals(Object compareObj){
        if (compareObj instanceof CatalogueIdentity){ /// compareObject != null
            CatalogueIdentity span = (CatalogueIdentity) compareObj;
            if (categoryPart.equals(span.categoryPart)){
                return namePart.equals(span.namePart);
            }
        }
        return false;
    }

    @Override
    public int hashCode(){
        return Objects.hash(categoryPart, namePart);
    }
}
