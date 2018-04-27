package com.creativeartie.writerstudio.lang;

import java.util.*; // ArrayList, List, Optional;

import com.google.common.collect.*; // ImmutableList;

import static com.creativeartie.writerstudio.main.ParameterChecker.*;

/** A list of {@link SpanBranch} with the same {@link CatalogueIdentity}.
 *
 * Purpose:
 * <ul>
 * <li> Calculates identity readiness and state </li>
 * <li> Stores a list of relative ids and refs </li>
 * <li> Identifies itself with location and identity </li>
 * </ul>
 */
public final class CatalogueData{

    /// %Part 1: Constructors and Fields #######################################

    private final CatalogueMap catalogueParent;
    private final CatalogueIdentity catelogueKey;

    private final ArrayList<SpanBranch> idSpans;
    private final ArrayList<SpanBranch> refSpans;

    private final ArrayList<SpanBranch> externalIds;
    private final ArrayList<SpanBranch> externalRefs;

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
        catelogueKey = argumentNotNull(id, "id");

        idSpans = new ArrayList<>();
        refSpans = new ArrayList<>();

        externalIds = new ArrayList<>();
        externalRefs = new ArrayList<>();

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
            "Id (" + catelogueKey + ") is in the wrong state.");

        return getIds().get(0);
    }

    /** Gets the complete set of ids.
     *
     * As oppose to {@link #getTarget()}.
     *
     * @return answer
     */
    public List<SpanBranch> getIds(){
        if (! cacheIds.isPresent()){
            ImmutableList.Builder<SpanBranch> builder = ImmutableList.builder();
            builder.addAll(idSpans).addAll(externalIds);
            cacheIds = Optional.of(builder.build());
        }
        return cacheIds.get();
    }

    /** Gets the complete set of refs.
     *
     * @return answer
     */
    public List<SpanBranch> getRefs(){
        if (! cacheRefs.isPresent()){
            ImmutableList.Builder<SpanBranch> builder = ImmutableList.builder();
            builder.addAll(refSpans).addAll(externalRefs);
            cacheRefs = Optional.of(builder.build());
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
        argumentCheck(span instanceof SpanBranch, "span",
            " is not of type SpanBranch.");
        (span.isId()? idSpans: refSpans).add((SpanBranch)span);
    }

    /** Adds a extenral {@link SpanBranch} as a {@linkplain Catalogued}.
     *
     * @param span
     *      adding catalouged span
     */
    void addExternal(Catalogued span){
        argumentNotNull(span, "Span");
        argumentCheck(span instanceof SpanBranch, "span",
            " is not of type SpanBranch.");

        (span.isId()? externalIds: externalRefs).add((SpanBranch)span);
        cacheIds = Optional.empty();
    }

    /** Removes all external {@link SpanBranch}. */
    void clearExternals(){
        externalRefs.clear();
        externalIds.clear();
        cacheIds = Optional.empty();
    }

    /// %Part 5: Other Gets ####################################################

    /** Gets the parent map where this {@linkplain CatalogueData} is stored.
     * @return answer
     */
    public CatalogueMap getParent(){
        return catalogueParent;
    }

    /** Gets the {@linkplain CatalogueKey}.
     * @return answer
     */
    public CatalogueIdentity getKey(){
        return catelogueKey;
    }

    /// %Part 6: Overrides #####################################################

    @Override
    public String toString(){
        return catelogueKey.toString() + ": " +
            getState().toString() + "\n\tIds{\n\t" +
            getIds().toString().replace("\n", "\n\t\t") + "\n\t}Refs{" +
            getRefs().toString() + "}\n";
    }
}
