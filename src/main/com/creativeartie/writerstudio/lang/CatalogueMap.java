package com.creativeartie.writerstudio.lang;

import java.util.*; // ArrayList, Comparator, HashSet, Map, Optional, SortedMap, TreeMap, TreeSet;

import com.google.common.collect.*; // ImmutableList, ImmutableSortedMap, ForwardingSortedMap;

import static com.creativeartie.writerstudio.main.ParameterChecker.*;

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
        CatalogueData>{

    /// %Part 1: Constructors ##################################################

    private final TreeMap<CatalogueIdentity, CatalogueData> idMap;
    private final ArrayList<CatalogueMap> otherMaps;

    private Optional<Map<CatalogueIdentity, CatalogueData>> delegateMap;

    /** {@linkplain CatalogueMap}'s constructor.*/
    CatalogueMap(){
        idMap = new TreeMap<>();
        otherMaps = new ArrayList<>();

        delegateMap = Optional.empty();
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
            String ... categories){
        argumentNotEmpty(categories, "categories");
        SortedMap<CatalogueIdentity, CatalogueData> map = delegate();

        /// get the first item
        CatalogueIdentity first = new CatalogueIdentity(
            ImmutableList.copyOf(categories), "");

        /// get the last item
        categories[categories.length - 1] = categories[categories.length - 1] +
            (char)0;
        CatalogueIdentity last = new CatalogueIdentity(
            ImmutableList.copyOf(categories), "");

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

        for (Entry<CatalogueIdentity, CatalogueData> entry: delegate()
                .entrySet()){
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
                CatalogueData data = null;

                /// check if it in a exteranl map
                for (CatalogueMap map: otherMaps){
                    data = map.get(id);
                    if (data != null){
                        data.addExternal(adding);
                        return;
                    }
                }

                /// Check if it's in a internal map
                if (data == null) {
                    data = idMap.get(id);
                }

                /// Adds a internal id
                if (data == null){
                    data = new CatalogueData(this, id);
                    idMap.put(id, data);
                }

                data.add(adding);
            }
        }
    }

    /// %Part 3: Other Maps ####################################################

    /** Adds a {@link CatalogueMap}.
     *
     * @param map
     *      map to add
     */
    void add(CatalogueMap map){
        argumentNotNull(map, "map");
        otherMaps.add(map);
    }

    /** Remove a {@link CatalogueMap}.
     *
     * @param map
     *      map to remove
     */
    void remove(CatalogueMap map){
        otherMaps.remove(map);
    }

    /// %Part 4: Overriding ####################################################

    @Override
    public SortedMap<CatalogueIdentity, CatalogueData> delegate(){
        ImmutableSortedMap.Builder<CatalogueIdentity, CatalogueData> builder =
            ImmutableSortedMap.naturalOrder();
        for (CatalogueMap map: otherMaps){
            builder.putAll(map);
        }
        builder.putAll(idMap);
        return builder.build();
    }

    @Override
    public void clear(){
        idMap.clear();
        for (CatalogueMap map: otherMaps){
            for (CatalogueData data: map.values()){
                data.clearExternals();
            }
        }
    }
}
