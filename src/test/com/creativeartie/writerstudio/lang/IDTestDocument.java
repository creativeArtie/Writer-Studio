package com.creativeartie.writerstudio.lang;

import static org.junit.Assert.*;

import com.google.common.base.CharMatcher;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public class IDTestDocument {
    private static final String STARTER   = "|";
    private static final String SEPARATOR = STARTER;

    private static String checkText(String raw){
        if (CharMatcher.anyOf(STARTER + SEPARATOR + "\n").matchesAnyOf(raw)){
            throw new IllegalArgumentException(
                    "id contains illegal characters:" + raw);
        }
        return raw;
    }

    private static String addIdText(CatalogueIdentity id){
        StringBuilder output = new StringBuilder();
        output.append(id.getCategories().size()).append(STARTER);
        for (String cat: id.getCategories()){
            output.append(checkText(cat)).append(SEPARATOR);
        }
        if (output.length() > 0){
            output.substring(0, output.length() - 1);
        }
        output.append(checkText(id.getName())).append("\n");
        return output.toString();
    }

    private ArrayList<CatalogueIdentity> ids;
    private ArrayList<CatalogueStatus> status;
    private ArrayList<Integer> order;
    private StringBuilder doc;

    public IDTestDocument(){
        ids = new ArrayList<>();
        status = new ArrayList<>();
        order = new ArrayList<>();
        doc = new StringBuilder();
    }

    public IDTestDocument addId(IDBuilder id){
        doc.append("id" + STARTER).append(addIdText(id.build()));
        return this;
    }

    public IDTestDocument addRef(IDBuilder id){
        doc.append("ref" + STARTER).append(addIdText(id.build()));
        return this;
    }

    public IDTestDocument addId(IDBuilder addId, int i) {
        return addId(addId, CatalogueStatus.UNUSED, i);
    }

    public IDTestDocument addId(IDBuilder addId, CatalogueStatus newStatus,
        int i)
    {
        CatalogueIdentity id = addId.build();
        ids.add(id);
        status.add(newStatus);
        order.add(i);
        doc.append("id" + STARTER).append(addIdText(id));
        return this;
    }

    public IDTestDocument addRef(IDBuilder addId, int i) {
        return addRef(addId, CatalogueStatus.NOT_FOUND, i);
    }

    public IDTestDocument addRef(IDBuilder addId, CatalogueStatus newStatus,
        int i)
    {
        CatalogueIdentity id = addId.build();
        ids.add(id);
        status.add(newStatus);
        order.add(i);
        doc.append("ref" + STARTER).append(addIdText(id));
        return this;
    }

    public void assertIds(){
        assertIds(build(), false);
    }

    public void assertIds(boolean showIds){
        assertIds(build(), showIds);
    }

    public void assertIds(Document doc){
        assertIds(doc, false);
    }

    public void assertIds(Document doc, boolean showIds){
        if (showIds){
            for(int i = 0; i < order.size(); i++){
                int idx = order.indexOf(i);
                System.out.print(ids.get(idx));
            }
            System.out.println();
        }

        Map<CatalogueIdentity, CatalogueData> map = doc.getCatalogue();
        if (showIds){
            for(Map.Entry<CatalogueIdentity, CatalogueData> entry:
                map.entrySet())
            {
                System.out.print(entry.getKey());
            }
            System.out.println();
            System.out.println();
        }
        assertEquals("Wrong map size", ids.size(), map.size());
        int i = 0;
        for (Map.Entry<CatalogueIdentity, CatalogueData> entry: map.entrySet()){
            int idx = order.indexOf(i);
            assert idx != -1: idx;
            assertEquals("Wrong span order at " + ids.get(idx), ids.get(idx),
                entry.getKey());
            assertEquals("Wrong id status at " + ids.get(idx),
                status.get(idx), entry.getValue().getState());
            i++;
        }
    }

    public Document build(){
        return new Document(doc.toString(), new SetupParser[]{(pointer) -> {
            ArrayList<Span> children = new ArrayList<>();
            TypeParser.PARSER.parse(children, pointer);
            pointer.matches(children, CharMatcher.javaDigit());
            int size = Integer.parseInt(children.get(children.size() - 1)
                .getRaw());
            pointer.startsWith(children, STARTER);
            for(int i = 0; i < size; i++){
                pointer.getTo(children, SEPARATOR, "\n");
                pointer.startsWith(children, SEPARATOR);
            }
            pointer.getTo(children, "\n");
            pointer.startsWith(children, "\n");
            return Optional.of(new IdSpan(children));
        }}){
            @Override protected void docEdited(){}
            @Override protected void childEdited(){}
        };
    }

    private class IdSpan extends SpanBranch implements Catalogued{

        public IdSpan(List<Span> spans) {
            super(spans);
        }

        @Override
        public List<StyleInfo> getBranchStyles() {
            return new ArrayList<>();
        }

        @Override
        public boolean isId(){
            return spanAtFirst(TypeSpan.class).get().isId();
        }

        @Override
        public Optional<CatalogueIdentity> getSpanIdentity() {
            ArrayList<String> categories = new ArrayList<>();
            int size = Integer.parseInt(get(1).getRaw());
            int idx = 3;
            for (int cat = 0; cat < size; cat++){
                String found = get(idx).getRaw();
                if (! found.equals(SEPARATOR)){
                    categories.add(found);
                    idx += 2;
                } else {
                    categories.add("");
                    idx++;
                }
            }

            String id = get(size() - 2).getRaw();
            if (id.equals(SEPARATOR)){
                id = "";
            }
            return Optional.of(new CatalogueIdentity(categories, id));
        }

        @Override
        protected SetupParser getParser(String text){
            return null;
        }

        @Override
        protected void childEdited(){}

        @Override
        protected void docEdited(){}
    }

    private static class TypeSpan extends SpanBranch {

        public TypeSpan(List<Span> spans) {
            super(spans);
        }

        public boolean isId(){
            return getRaw().equals("id" + STARTER);
        }

        @Override
        public List<StyleInfo> getBranchStyles() {
            return new ArrayList<>();
        }

        @Override
        protected SetupParser getParser(String text){
            return null;
        }

        @Override
        protected void childEdited(){}

        @Override
        protected void docEdited(){}
    }

    private enum TypeParser implements SetupParser{
        PARSER;

        @Override
        public Optional<SpanBranch> parse(SetupPointer pointer) {
            ArrayList<Span> children = new ArrayList<>();
            pointer.getTo(children, STARTER);
            pointer.startsWith(children, STARTER);
            return Optional.of(new TypeSpan(children));
        }

    }
}
