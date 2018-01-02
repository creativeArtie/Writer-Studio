package com.creativeartie.jwriter.lang.markup;

import java.util.*;
import java.util.function.*;

import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.main.Checker.*;
import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;

/**
 * Parser for {@code MainSpan*} classes. See {@code design/SectionParser.txt}
 * for details
 *
 */
enum SectionParser implements SetupParser {
    INSTANCE;

    private static Optional<LinedSpanLevelSection> parseHead(
            SetupPointer pointer){
        Optional<SpanBranch> head = LinedParseLevel.HEADING.parse(pointer);
        if (! head.isPresent()){
            return Optional.of( (LinedSpanLevelSection) head.get());
        }
        return LinedParseLevel.OUTLINE.parse(pointer).map(span ->
            (LinedSpanLevelSection) span);
    }

    private static Optional<LinedSpanLevelSection> parseContent(
            ArrayList<Span> children, SetupPointer pointer){
        while (pointer.hasNext()){
            pointer.mark();
            Optional<LinedSpanLevelSection> head = parseHead(pointer);
            if (head.isPresent()){
                return head;
            }
            if (! NoteCardParser.PARSER.parse(children, pointer)){
                for (SetupParser parser: SECTION_PARSERS){
                    if (parser.parse(children, pointer)){
                        /// break to restart parser checking
                        break;
                    }
                }
            }
        }
        /// No more text found
        return Optional.empty();
    }

    @Override
    public Optional<SpanBranch> parse(SetupPointer pointer){
        checkNotNull(pointer, "pointer");
        ArrayList<Span> children = new ArrayList<>();

        Optional<LinedSpanLevelSection> head = parseHead(pointer);
        if (! head.isPresent()){
            head = parseContent(children, pointer);
        } else {
            head = head.filter(span ->
                span.getLinedType() == LinedType.HEADING &&
                span.getLevel() == 1
            );
            if (head.isPresent()){
                children.add(head.get());
                head = parseContent(children, pointer);
            }
        }

        while (head.isPresent()){
            LinedSpanLevelSection line = head.get();
            if (line.getLinedType() == LinedType.HEADING){
                if (line.getLevel() == 1){
                    pointer.rollBack();
                    head = Optional.empty();
                } else {
                    head = parseSection(children, pointer, line, 2);
                }
            } else {
                assert line.getLinedType() == LinedType.OUTLINE;
                head = parseOutline(children, pointer, line, 1);
            }
        }
        return Optional.ofNullable(children.isEmpty()? null:
            new SectionSpanHead(children));
    }

    private Optional<LinedSpanLevelSection> parseSection(List<Span> parent,
            SetupPointer pointer, LinedSpanLevelSection line, int level){
        assert pointer != null: "Null pointer";
        assert line != null: "Null line";
        assert line.getLinedType() == LinedType.HEADING: "Line is not heading";
        assert line.getLevel() < level: "Level too low: " + level;
        Optional<LinedSpanLevelSection> head = Optional.empty();

        ArrayList<Span> children = new ArrayList<>();
        children.add(line);
        if (line.getLevel() > level){
            head = parseSection(children, pointer, line, level + 1);
        } else if (line.getLevel() == level){
            head = parseContent(children, pointer);
        }

        while(head.isPresent()){
            line = head.get();
            if (line.getLinedType() == LinedType.HEADING){
                if (line.getLevel() <= level){
                    pointer.rollBack();
                    head = Optional.empty();
                } else {
                    head = parseSection(children, pointer, line, level + 1);
                }
            } else {
                assert line.getLinedType() == LinedType.OUTLINE;
                head = parseOutline(children, pointer, line, 1);
            }
        }
        parent.add(new SectionSpanHead(children));
        return parseHead(pointer);
    }

    private Optional<LinedSpanLevelSection> parseOutline(List<Span> parent,
            SetupPointer pointer, LinedSpanLevelSection line, int level){
        assert pointer != null: "Null pointer";
        assert line != null: "Null line";
        assert line.getLinedType() == LinedType.OUTLINE: "Line is not outline";
        assert line.getLevel() < level: "Level too low: " + level;
        Optional<LinedSpanLevelSection> head = Optional.empty();

        ArrayList<Span> children = new ArrayList<>();
        if (line.getLevel() > level){
            head = parseOutline(children, pointer, line, level + 1);
        } else if (line.getLevel() == level){
            head = parseContent(children, pointer);
        }

        while(head.isPresent()){
            line = head.get();
            if (line.getLinedType() == LinedType.HEADING){
                pointer.rollBack();
                head = Optional.empty();
            } else {
                assert line.getLinedType() == LinedType.OUTLINE;
                if (line.getLevel() > level){
                    head = parseOutline(children, pointer, line, level + 1);
                } else {
                    assert line.getLevel() == level || line.getLevel() < level;
                    pointer.rollBack();
                    head = Optional.empty();
                }
            }
        }
        parent.add(new SectionSpanOutline(children));
        return parseHead(pointer);
    }

    /*
    @Override
    public Optional<SpanBranch> parse(SetupPointer pointer){
        checkNotNull(pointer, "pointer");
        ArrayList<Span> children = new ArrayList<>();
        /// Parsing the empty or Heading 1
        Optional<LinedSpanLevelSection> head = parseContent(pointer);
        if (! head.isPresent()){
            head = parseContent(children, pointer);
        } else {
            if (head.filter(span ->
                    span.getType() == LinedType.HEADING && span.getLevel() == 1
                ).isPresent()){
                children.add(head.get()));
                head = parseContent(children, pointer);
            } // else { /// go straight to heading > 1, or outline

        }
        /// head is now is either heading > 1, or outline, or next section
        while (head.isPresent()){
            if (head.get().getLinedType() == LinedType.HEADING){
                if (head.get().getLevel() == 1){
                    pointer.rollBack();
                    return Optional.ofNullable(children.isEmpty()? null:
                        new SectionSpanHead(children));
                }
                children.add(parseHead(pointer, head.get(), 2));
            } else {
                children.add(parseOutline(pointer, head.get(), 1);
            }
            head = parseHead(pointer); /// get next head
        }
    }

    private SpanBranch parseHead(SetupPointer pointer,
            LinedSpanLevelSection heading, int level){
        assert pointer != null: "Null pointer";
        assert heading != null: "Null heading";
        assert heading.getLevel() => level: "Level is below current level.";
        ArrayList<Span> children = new ArrayList<>();
        if (heading.getLevel() > level) {
            children.add(parseHead(pointer, heading, level + 1));
        }
        assert heading.getLevel() == level;
        children.add(heading);
        Optional<LinedSpanLevelSection> head = parseContent(children, pointer);
        while (head.isPresent()){
            heading = head.get();
            if (heading.getLinedType() == LinedType.HEADING()){
                if (heading.getLevel() < head.getLevel()){
                    assert ! children.isEmpty(): "Empty children";
                    return new SectionSpanHead(children);
                }
                children.add(parseHead(pointer, heading, level + 1));
            } else {
                children.add(parseOutline(pointer, heading, 1));
            }
        }
        return new SectionSpanHead(children);
    }

    private SpanBranch parseOutline(SetupPointer pointer,
            LinedSpanLevelSection heading,int level){
     /*   assert pointer != null: "Null pointer";
        ArrayList<Span> children = new ArrayList<>();
        Optional<SpanBranch> head = parseHead(pointer, level);
        if (! head.isPresent()){
            assert level == 1: "Level is not 1: " + level;
        }

        do {
            head = parseContent(children, pointer).filter(span ->
                /// filter out heading
                span.getLinedType() == LinedType.HEADING
            ).filter(span ->
                span.getLevel() < level
            ).ifPresent(span -> span.getLinedType() == LinedType.OUTLINE?
                /// add child of heading with a higher number or outline
                parseOutline(span): parseHead(pointer, level + 1)
            );
        } while (head.isPresent());
    }

    private Optional<SpanBranch> buildSpan(ArrayList<Span> children){
        return Optional.ofNullable(children.isEmpty()? null:
            new SectionSpanHead(children));
    }

    private static Optional<LinedSpanLevelSection> parseHead(
            SetupPointer pointer){
        Optional<SpanBranch> head = LinedParseLevel.HEADING.parse(pointer);
        if (! head.isPresent()){
            return Optional.of( (LinedSpanLevelSection) head.get());
        }
        return LinedParseLevel.OUTLINE.parse(pointer).map(span ->
            (LinedSpanLevelSection) span);
    }

    private static Optional<LinedSpanLevelSection> parseContent(
            ArrayList<Span> children, SetupPointer pointer){
        while (pointer.hasNext()){
            pointer.mark();
            Optional<SpanBranch> head = parseHead(pointer);
            if (head.isPresent()){
                return head;
            }
            if (MainParseNote.PARSER.parse(children, pointer)){
                continue;
            }
            for (SetupParser parser: SECTION_PARSERS){
                if (parser.parse(children, pointer)){
                    /// break to restart parser checking
                    break;
                }
            }
        }
        /// No more text found
        return Optional.empty();
    }

/*
    private Optional<SpanBranch> parseHead1(SetupPointer pointer){
        assert pointer != null: "Null pointer";
        ArrayList<Span> children = new ArrayList<>();
        Optional<LinedSpanLevelSection> header = parseHead(pointer);
        if (header.isPresent()){
            if (header.get().getLinedType() == LinedType.HEADING){
                if (header.get().getLevel() == 1){
                    children.add(head.get());
                }
            } else {
                parseChildHead(pointer, header.get(), 1);
            }
        }
    }

    private SpanBranch parseChildHead(SetupPointer pointer,
            LinedSpanLevelSection header, int level){
        ArrayList<Span> children = new ArrayList<>();
        if (header.getLevel() > level){
            children.add(parseChildHead(pointer, header, level + 1));
        } else if (header.getLevel() < level){
            return new SectionSpanHead(children);
        }

        children.add(header);
        Optional<LinedSpanLevelSection> find = parseContent(children, pointer);
        if (find.isPresent()){
            header = find.get();
            if (header.getLinedType() == LinedType.OUTLINE){
                children.add(parseOutline(pointer, header, level));
            }
            if (header.getLevel() > level){
                children.add(parseChildHead(pointer, header, level + 1));
            } else if (header.getLevel() < level){
                return new SectionSpanHead(children);
            }
        }
        return new SectionSpanHead(children);
    }

    private static Optional<LinedSpanLevelSection> parseContent(
            ArrayList<Span> children, SetupPointer pointer){
        while (pointer.hasNext()){
            pointer.mark();
            Optional<LinedSpanLevelSection> holder = parseHead(pointer);
            if (holder.isPresent()){
                return holder;
            }
            for (SetupParser parser: SECTION_PARSERS){
                if (parser.parse(children, pointer)){
                    break;
                }
            }
        }
        return Optional.empty();
    }*/
}