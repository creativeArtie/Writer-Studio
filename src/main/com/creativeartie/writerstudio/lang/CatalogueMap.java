package com.creativeartie.writerstudio.lang;

import java.util.*; // HashSet, SortedMap, TreeMap, Map

import com.google.common.collect.*; // ImmutableList, ImmutableSortedMap

import static com.creativeartie.writerstudio.main.Checker.*;

/**
 * A map of {@link CatalogueData} that is serachable by
 * {@link CatalogueIdentity}.
 */
public final class CatalogueMap extends ForwardingSortedMap<CatalogueIdentity,
        CatalogueData>{
    private final TreeMap<CatalogueIdentity, CatalogueData> idMap;
    private final ArrayList<CatalogueMap> otherMaps;
    private Optional<Map<CatalogueIdentity, CatalogueData>> delegateMap;

    /** {@linkplain CatalogueMap}'s constructor.*/
    CatalogueMap(){
        idMap = new TreeMap<>();
        otherMaps = new ArrayList<>();
        delegateMap = Optional.empty();
    }

    void add(CatalogueMap map){
        otherMaps.add(map);
    }

    void remove(CatalogueMap map){
        otherMaps.remove(map);
    }

    void add(SpanBranch span, HashSet<SpanBranch> edited){
        checkNotNull(span, "span");
        if (span instanceof Catalogued){
            Catalogued adding = (Catalogued)span;
            Optional<CatalogueIdentity> found = adding.getSpanIdentity();
            if (found.isPresent()){
                CatalogueIdentity id = found.get();
                CatalogueData data = null;
                for (CatalogueMap map: otherMaps){
                    data = map.get(id);
                    if (data != null){
                        data.addExternal(adding);
                        edited.add(span);
                        return;
                    }
                }
                if (data == null) {
                    data = idMap.get(id);
                }
                if (data == null){
                    data = new CatalogueData(this, id);
                    idMap.put(id, data);
                }
                data.add(adding);
                edited.add(span);
            }
        }
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

    public SortedMap<CatalogueIdentity, CatalogueData> getCategory(
            String ... category){
        checkNotEmpty(category, "category");

        SortedMap<CatalogueIdentity, CatalogueData> map = delegate();
        CatalogueIdentity first = new CatalogueIdentity(
            ImmutableList.copyOf(category), "");

        category[category.length - 1] = category[category.length - 1] + (char)0;
        CatalogueIdentity last = new CatalogueIdentity(
            ImmutableList.copyOf(category), "");
        return map.subMap(first, last);
    }

    public TreeSet<SpanBranch> getIds(String base){
        TreeSet<SpanBranch> spans = new TreeSet<>(Comparator.comparingInt(
            span -> span.getStart()));
        for (Entry<CatalogueIdentity, CatalogueData> entry: delegate()
                .entrySet()){
            if (entry.getKey().getBase().equals(base)){
                for (SpanBranch span: entry.getValue().getIds()){
                    spans.add(span);
                }
            }
        }
        return spans;
    }
}
