package com.creativeartie.jwriter.window;

import java.util.*;
import javafx.collections.*;
import javafx.beans.property.*;

import com.creativeartie.jwriter.lang.*;
import com.creativeartie.jwriter.lang.markup.*;
import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;

import com.google.common.collect.*;

/**
 * A data object a {@linkplain TableView}.
 */
public class NotesData{
    /** Only references are found. */
    public static final int NO_ID  = -2;
    /** Only one id is found (which is good!). */
    public static final int SINGLE = -1;
    /// For more than one ids are found
    /// public static final int MULIPLE >= 0

    /** {@linkplain NotesData} factory builder thingy. */
    public static ObservableList<NotesData> extractData(
        Collection<CatalogueData> data, DirectoryType type)
    {
        ArrayList<NotesData> out = new ArrayList<>();
        for (CatalogueData load: data){
            int size = load.getIds().size();
            switch (size){
            case 0: /// no id is found
                out.add(new NotesData(load, NO_ID, type));
                break;
            case 1: /// exactly on id is found
                out.add(new NotesData(load, SINGLE, type));
                break;
            default: /// multiple id is found
                assert size > 1;
                for(int i = 0; i < size; i++){
                    out.add(new NotesData(load, i, type));
                }
            }
        }
        return FXCollections.observableArrayList(out);
    }

    /**
     * This exists to deal with a single column in the {@linkplain TableView}.
     */
    public class IdentityData{
        private IdentityData(){}

        public String getName() {
            return targetId.getName();
        }

        public String getTarget() {
            return targetNum >= 0? "(" + targetNum + ")" : "";
        }
    }

    /// Property to use as columns
    private final ReadOnlyStringWrapper catalogueCategory;
    private final ReadOnlyObjectWrapper<IdentityData> catalogueIdentity;
    private final ReadOnlyStringWrapper refText;
    private final ReadOnlyObjectWrapper<Optional<Range<Integer>>> spanLocation;
    private final ReadOnlyObjectWrapper<Optional<SpanBranch>> targetSpan;

    /// Proerty to select the actual span connected.
    private final CatalogueIdentity targetId;
    private final int targetNum;

    private NotesData(CatalogueData data, int target, DirectoryType type){
        /// For category, name, targetId
        CatalogueIdentity id = data.getKey();
        /// For catalogueCategory, refText
        String categories = getCategories(id.getCategories());

        /// For spanLocation
        Optional<Range<Integer>> location = Optional.empty();
        /// For location, targetSpan
        Optional<SpanBranch> span = Optional.empty();
        switch(target){
            case NO_ID:
                /// keep Optional.empty()
                break;
            case SINGLE:
                /// get the only id span
                span = Optional.of(data.getTarget());
                location = Optional.of(span.get().getRange());
                break;
            default:
                /// get for the id span at index
                span = Optional.of(data.getIds().get(target));
                location = Optional.of(span.get().getRange());
        }

        /// set the fields:
        targetId = id;
        targetNum = target;
        spanLocation = new ReadOnlyObjectWrapper<>(location);
        catalogueIdentity = new ReadOnlyObjectWrapper<>(new IdentityData());
        targetSpan = new ReadOnlyObjectWrapper<>(span);
        catalogueCategory = new ReadOnlyStringWrapper(categories);
        refText = new ReadOnlyStringWrapper(buildRefText(type, categories, id
            .getName()));
    }

    /**
     * Create a string like "\{^id\}. Helper method of
     * {@link #NotesData(CatalogueData, int, DirectoryType)}.
     */
    private static String buildRefText(DirectoryType type, String category,
            String name){
        if (type == DirectoryType.COMMENT){
            return "";
        }
        if (! category.isEmpty()){
            category += DIRECTORY_CATEGORY;
        }
        return type.getStart() + category + name + type.getEnd();
    }

    public ReadOnlyStringProperty categoryProperty(){
        return catalogueCategory.getReadOnlyProperty();
    }

    public String getCatalogueCategory(){
        return catalogueCategory.getValue();
    }

    public ReadOnlyObjectProperty<IdentityData> catalogueIdentityProperty(){
        return catalogueIdentity.getReadOnlyProperty();
    }

    public IdentityData getCatalogueIdentity(){
        return catalogueIdentity.getValue();
    }

    public ReadOnlyStringProperty refTextProperty(){
        return refText.getReadOnlyProperty();
    }

    public String getRefText(){
        return refText.getValue();
    }

    public ReadOnlyObjectProperty<Optional<Range<Integer>>> spanLocationProperty(){
        return spanLocation.getReadOnlyProperty();
    }

    public Optional<Range<Integer>> spanLocationCount(){
        return spanLocation.getValue();
    }

    public ReadOnlyObjectProperty<Optional<SpanBranch>> targetSpanProperty(){
        return targetSpan.getReadOnlyProperty();
    }

    public Optional<SpanBranch> getTargetSpan(){
        return targetSpan.getValue();
    }

    public CatalogueIdentity getIdentity(){
        return targetId;
    }

    public int getTarget(){
        return targetNum;
    }

    @SuppressWarnings("fallthrough")
    private static String getCategories(List<String> categories){
        int status = 0;
        String ans = "";
        for(String category: categories){
            switch (status){
            default:
                ans += DIRECTORY_CATEGORY;
                /** FALL THROUGH **/
            case 1:
                ans += category;
                /** FALL THROUGH **/
            case 0:
                status++;
            }
        }
        return ans;
    }

}