package com.creativeartie.writerstudio.lang;

import java.util.*; // HashSet, SortedMap, TreeMap

import com.google.common.collect.*; // ImmutableList, ImmutableSortedMap

import static com.creativeartie.writerstudio.main.Checker.*;

/**
 * A map of {@link CatalogueData} that is serachable by
 * {@link CatalogueIdentity}.
 */
public final class CatalogueMap extends ForwardingSortedMap<CatalogueIdentity,
        CatalogueData>{
    private final TreeMap<CatalogueIdentity, CatalogueData> idMap;

    /** {@linkplain CatalogueMap}'s constructor.*/
    CatalogueMap(){
        idMap = new TreeMap<>();
    }

    void add(SpanBranch span, HashSet<SpanBranch> edited){
        checkNotNull(span, "span");
        if (span instanceof Catalogued){
            Catalogued adding = (Catalogued)span;
            Optional<CatalogueIdentity> found = adding.getSpanIdentity();
            if (found.isPresent()){
                CatalogueIdentity id = found.get();
                CatalogueData data = idMap.get(id);
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
    public SortedMap<CatalogueIdentity, CatalogueData> delegate(){
        return ImmutableSortedMap.copyOf(idMap);
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
        for (Entry<CatalogueIdentity, CatalogueData> entry: idMap.entrySet()){
            if (entry.getKey().getBase().equals(base)){
                for (SpanBranch span: entry.getValue().getIds()){
                    spans.add(span);
                }
            }
        }
        return spans;
    }
}
