package com.creativeartie.writerstudio.lang;

import java.util.*;

import com.google.common.collect.*;

import static com.creativeartie.writerstudio.util.ParameterChecker.*;

/** A map of {@link CatalogueData} identifiable by {@link CatalogueIdentity}.
 *
 * Purpose:
 * <ul>
 * <li> Search {@link CatalogueIdentity} and {@link SpanBranch}. </li>
 * <li> Adds {@link SpanBranch} into {@link CatalogueData}.</li>
 * <li> Communicates with other {@link CatalogueMap}.</li>
 * </ul>
 */
public final class CatalogueMap extends ForwardingSortedMap<CatalogueIdentity,
    CatalogueData>
{

    /// %Part 1: Constructors and Fields #######################################

    private final TreeMap<CatalogueIdentity, CatalogueData> idMap;

    /** {@linkplain CatalogueMap}'s constructor.*/
    CatalogueMap(){
        idMap = new TreeMap<>();
    }

    /// %Part 2: Main Span Managerment #########################################
    /// %Part 2.1 Main Get Methods =============================================

    /** Gets the data in a category.
     *
     * @param categories
     *      category list
     * @return answer
     */
    public SortedMap<CatalogueIdentity, CatalogueData> getData(
        String ... categories
    ){
        argumentNotEmpty(categories, "categories");
        SortedMap<CatalogueIdentity, CatalogueData> map = delegate();

        /// get the first item
        CatalogueIdentity first = new CatalogueIdentity(
            ImmutableList.copyOf(categories), "");

        /// get the last item
        categories[categories.length - 1] = categories[categories.length - 1] +
            (char)0;
        CatalogueIdentity last = new CatalogueIdentity(
            ImmutableList.copyOf(categories), ""
        );

        return map.subMap(first, last);
    }

    /** Gets the id by main category, sorted by location.
     *
     * @param main
     *      main category
     * @return answer
     */
    public TreeSet<SpanBranch> getIds(String main){
        argumentNotEmpty(main, "main");
        TreeSet<SpanBranch> spans = new TreeSet<>(Comparator.comparingInt(
            s -> s.getStart()));

        for (Entry<CatalogueIdentity, CatalogueData> entry: delegate().entrySet()){
            if (entry.getKey().getMain().equals(main)){
                /// Found a match
                for (SpanBranch span: entry.getValue().getIds()){
                    spans.add(span);
                }
            }
        }
        return spans;
    }
    /// %Part 2.2: Main Add Methods ============================================

    /** Add a {@link SpanBranch} if it's a {@link Catalogued}.
     *
     * @param span
     *      the span to add
     */
    void add(SpanBranch span){
        argumentNotNull(span, "span");

        if (span instanceof Catalogued){
            /// Catalogued is found
            Catalogued adding = (Catalogued)span;
            Optional<CatalogueIdentity> found = adding.getSpanIdentity();
            if (found.isPresent()){

                /// Catalogued is found with a id
                CatalogueIdentity id = found.get();
                CatalogueData data = idMap.get(id);

                /// Adds a internal id if not found
                if (data == null){
                    data = new CatalogueData(this, id);
                    idMap.put(id, data);
                }

                data.add(adding);
            }
        }
    }

    void addMap(CatalogueMap map){
        for (Map.Entry<CatalogueIdentity, CatalogueData> data: map.entrySet()){
            if (idMap.containsKey(data.getKey())){
                idMap.get(data.getKey()).add(data.getValue());
            } else {
                idMap.put(data.getKey(), new CatalogueData(data.getValue()));
            }
        }
    }

    /// %Part 4: Overriding ####################################################

    @Override
    public SortedMap<CatalogueIdentity, CatalogueData> delegate(){
        return ImmutableSortedMap.copyOf(idMap);
    }

    void clearMap(){
        idMap.clear();
    }
}
