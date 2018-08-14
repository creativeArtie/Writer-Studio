package com.creativeartie.writerstudio.lang;

import java.util.*;

import com.google.common.collect.*;

import static com.creativeartie.writerstudio.main.ParameterChecker.*;

/** A list of {@link SpanBranch} with the same {@link CatalogueIdentity}.
 *
 * Purpose:
 * <ul>
 * <li> Calculates identity readiness and state </li>
 * <li> Stores a list of relative idList and refs </li>
 * <li> Identifies itself with location and identity </li>
 * </ul>
 */
public final class CatalogueData{

    /// %Part 1: Constructors and Fields #######################################

    private final CatalogueMap catalogueParent;
    private final CatalogueIdentity catalogueKey;

    private final ArrayList<SpanBranch> idSpans;
    private final ArrayList<SpanBranch> refSpans;

    private Optional<List<SpanBranch>> cacheIds;
    private Optional<List<SpanBranch>> cacheRefs;

    /** Creates a {@linkplain CatalogueData}.
     *
     * @param parent
     *      the parent map
     * @param id
     *      the data id
     */
    CatalogueData(CatalogueMap parent, CatalogueIdentity id){
        catalogueParent = argumentNotNull(parent, "parent");
        catalogueKey = argumentNotNull(id, "id");

        idSpans = new ArrayList<>();
        refSpans = new ArrayList<>();

        cacheIds = Optional.empty();
        cacheRefs = Optional.empty();
    }

    CatalogueData(CatalogueData self){
        catalogueParent = self.catalogueParent;
        catalogueKey = self.catalogueKey;

        idSpans = new ArrayList<>(self.idSpans);
        refSpans = new ArrayList<>(self.refSpans);

        cacheIds = Optional.empty();
        cacheRefs = Optional.empty();
    }

    /// %Part 2: States and Readiness ##########################################

    /** Gets the {@link CatalogueStatus} based on the Span stored.
     *
     * @return answer
     */
    public CatalogueStatus getState(){
        if (getIds().size() > 1){
            return CatalogueStatus.MULTIPLE;
        } else if (getIds().isEmpty()){
            assert !refSpans.isEmpty();
            return CatalogueStatus.NOT_FOUND;
        } else if (getRefs().isEmpty()){
            assert !getIds().isEmpty();
            return CatalogueStatus.UNUSED;
        }
        return CatalogueStatus.READY;
    }

    /** Check if this is ready (that is: {@code idSpan.size() == 1}).
     *
     * @return answer
     */
    public boolean isReady(){
        CatalogueStatus state = getState();
        return state == CatalogueStatus.READY ||
            getState() == CatalogueStatus.UNUSED;
    }

    /// %Part 3: Get Ids and Refs ##############################################

    /** Gets the target span.
     *
     * As oppose to {@link #getIds()}.
     *
     * @return answer
     */
    public SpanBranch getTarget(){
        stateCheck(getIds().size() == 1,
            "Id (" + catalogueKey + ") is in the wrong state.");

        return getIds().get(0);
    }

    /** Gets the complete set of idList.
     *
     * As oppose to {@link #getTarget()}.
     *
     * @return answer
     */
    public List<SpanBranch> getIds(){
        if (! cacheIds.isPresent()){
            cacheIds = Optional.of(ImmutableList.copyOf(idSpans));
        }
        return cacheIds.get();
    }

    /** Gets the complete set of refs.
     *
     * @return answer
     */
    public List<SpanBranch> getRefs(){
        if (! cacheRefs.isPresent()){
            cacheIds = Optional.of(ImmutableList.copyOf(refSpans));
        }
        return cacheRefs.get();
    }

    /// %Part 4: Adding a clearing #############################################

    /** Adds a {@link SpanBranch} as a {@linkplain Catalogued}.
     *
     * @param span
     *      adding catalouged span
     */
    void add(Catalogued span){
        argumentNotNull(span, "Span");
        argumentClass(span, "span", SpanBranch.class);
        (span.isId()? idSpans: refSpans).add((SpanBranch)span);
    }

    void add(CatalogueData data){
        for (SpanBranch span: data.idSpans){
            idSpans.add(span);
        }
        for (SpanBranch span: data.refSpans){
            refSpans.add(span);
        }
    }

    /** Removes all external {@link SpanBranch}. */
    void clearExternals(){
        cacheIds = Optional.empty();
    }

    /// %Part 5: Other Gets ####################################################

    /** Gets the parent map where this {@linkplain CatalogueData} is stored.
     * @return answer
     */
    public CatalogueMap getParent(){
        return catalogueParent;
    }

    /** Gets the {@linkplain CatalogueIdentity}.
     * @return answer
     */
    public CatalogueIdentity getKey(){
        return catalogueKey;
    }

    /// %Part 6: Overrides #####################################################

    @Override
    public String toString(){
        return catalogueKey.toString() + ": " +
            getState().toString() + "\n\tIds{\n\t" +
            getIds().toString().replace("\n", "\n\t\t") + "\n\t}Refs{" +
            getRefs().toString() + "}\n";
    }
}
