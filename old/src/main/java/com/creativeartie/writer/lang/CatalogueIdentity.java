package com.creativeartie.writer.lang;

import java.util.*;

import com.google.common.collect.*;

import static com.creativeartie.writer.main.ParameterChecker.*;

/** Marker to group {@link SpanBranch}.
 *
 * Purpose:
 * <ul>
 * <li>Breaks up into categories and name </li>
 * <li>Maintains a unique, unchanging identitfier</li>
 * <li>Allows sorting.</li>
 * <li>Does some help identity related tasks.</li>
 * </ul>
 */
public final class CatalogueIdentity implements Comparable<CatalogueIdentity>{

    /// %Part 1: Constructors and Fields #######################################

    private final ImmutableList<String> categoryPart;

    private final String namePart;

    /** Creates a {@linkplain CatalogueIdentity} base on the location.
     *
     * @param categories
     *      id categories
     * @param span
     *      location extracting span
     */
    public CatalogueIdentity(List<String> categories, Span span){
        argumentNotNull(categories, "categories");
        categoryPart = ImmutableList.copyOf(categories);

        int ptr = argumentNotNull(span, "span").getStart();
        int padding = String.valueOf(span.getDocument().getLocalEnd())
            .length();
        namePart = String.format("%0" + padding + "d",  ptr);
    }

    /** Creates a basic {@linkplain CatalogueIdentity}.
     *
     * @param category
     *      id's category
     * @param name
     *      id's name
     */
    public CatalogueIdentity(List<String> category, String name){
        argumentNotNull(category, "categories");
        categoryPart = ImmutableList.copyOf(category);

        namePart = argumentNotNull(name, "name");
    }

    /// %Part 2: Identifiers and Grouping ######################################

    /** Get the full identity, each part separated by the char "-".
     *
     * @return answer
     */
    public String getFullIdentity(){
        StringBuilder builder = new StringBuilder();

        /// Append categories
        for(String category: getCategories()){
            builder.append(category);
            builder.append("-");
        }

        /// Append names
        builder.append(getName());

        return builder.toString();
    }

    /** Gets the category lists.
     *
     * @return answer
     */
    public List<String> getCategories(){
        return categoryPart;
    }

    /** Gets the first, main category.
     *
     * @return answer
     */
    public String getMain(){
        if (categoryPart.isEmpty()){
            return "";
        }
        return categoryPart.get(0);
    }

    /** Gets the name of the {@linkplain CatalogueIdentity}.
     *
     * @return answer
     */
    public String getName(){
        return namePart;
    }


    /// %Part 3: Helpful Methods ###############################################

    /** Find the status base on a {@link CatalogueMap}.
     *
     * @param map
     *      use map
     * @return answer
     */
    CatalogueStatus getStatus(CatalogueMap map){
        argumentNotNull(map, "map");

        return map.get(this).getState();
    }

    /// %Part 4: Overriding ####################################################
    /// %Part 4.1 Comparing ====================================================

    @Override
    public int compareTo(CatalogueIdentity that){
        if (that == null){
            return 1;
        }
        return ComparisonChain.start()
            .compare(categoryPart, that.categoryPart,
                CatalogueIdentity::compareCategory)
            .compare(namePart, that.namePart)
            .result();
    }

    /**
     * Compares two lists of catergories.
     * @param self
     *      self categories
     * @param that
     *      that categories
     * @return answer
     */
    private static int compareCategory(List<String> self, List<String> that){
        assert self != null: "Null self";
        assert that != null: "Null that";

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

    /// %Part 4.2 Equals and Hash ==============================================

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

    /// %Part 4.3 Others =======================================================

    @Override
    public String toString(){
        return "(" + getFullIdentity() + ")";
    }
}
