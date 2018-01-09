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
        /// First column
        if (! head.isPresent()){
            /// `none`
            head = parseContent(children, pointer);
        } else {
            head = head.filter(span ->
                span.getLinedType() == LinedType.HEADING &&
                span.getLevel() == 1
            ).flatMap(child -> {
                /// `Head 1`
                children.add(child);
                return parseContent(children, pointer);
            });
        }

        /// `none` + parseContent, `Head 1` + parseContent, `head > 1`, `out`:
        while (head.isPresent()){
            LinedSpanLevelSection line = head.get();
            if (line.getLinedType() == LinedType.HEADING){
                if (line.getLevel() == 1){
                    /// `Head 1`:
                    pointer.rollBack();
                    head = Optional.empty(); /// head = empty = exit while
                } else {
                    /// `Head > 1`:
                    head = parseSection(children, pointer, line, 2);
                }
            } else {
                /// `Out`:
                assert line.getLinedType() == LinedType.OUTLINE;
                head = parseOutline(children, pointer, line, 1);
            }
        }
        /// `Head 1`, `empty`:
        return Optional.ofNullable(children.isEmpty()? null:
            new SectionSpanHead(children));
    }

    private Optional<LinedSpanLevelSection> parseSection(List<Span> parent,
            SetupPointer pointer, LinedSpanLevelSection line, int level){
        assert pointer != null: "Null pointer";
        assert line != null: "Null line";
        assert level >= 1: "Wrong level: " + level;
        /// `Out`:
        assert line.getLinedType() == LinedType.HEADING: "Line is not heading";
        /// `Head < level`:
        assert line.getLevel() < level: "Level too low: " + level;
        Optional<LinedSpanLevelSection> head = Optional.empty();

        ArrayList<Span> children = new ArrayList<>();
        if (line.getLevel() > level){
            /// `Head > Level`:
            head = parseSection(children, pointer, line, level + 1);
        } else {
            /// `Head = Level`:
            assert line.getLevel() == level: "Wrong level: " + level;
            children.add(line);
            head = parseContent(children, pointer);
        }

        /// `Head > Level` +  parseSection, `Head = Level` + parseContent:
        while(head.isPresent()){
            line = head.get();
            if (line.getLinedType() == LinedType.HEADING){
                if (line.getLevel() <= level){
                    /// `Head = Level`, `Head < Level`:
                    pointer.rollBack();
                    head = Optional.empty(); /// head = empty = exit while
                } else {
                    head = parseSection(children, pointer, line, level + 1);
                }
            } else {
                /// `Out`
                assert line.getLinedType() == LinedType.OUTLINE;
                head = parseOutline(children, pointer, line, 1);
            }
        }
        /// `empty`:
        parent.add(new SectionSpanHead(children));
        return parseHead(pointer);
    }

    private Optional<LinedSpanLevelSection> parseOutline(List<Span> parent,
            SetupPointer pointer, LinedSpanLevelSection line, int level){
        assert pointer != null: "Null pointer";
        assert line != null: "Null line";
        /// `Head`:
        assert line.getLinedType() == LinedType.OUTLINE: "Line is not outline";
        /// `Out < Level`:
        assert line.getLevel() < level: "Level too low: " + level;
        Optional<LinedSpanLevelSection> head = Optional.empty();

        ArrayList<Span> children = new ArrayList<>();
        if (line.getLevel() > level){
            /// `Out > Level`:
            head = parseOutline(children, pointer, line, level + 1);
        } else if (line.getLevel() == level){
            /// `Out = Level`:
            head = parseContent(children, pointer);
        }

        /// `Out > Level` +  parseOutline, `Head = Level` + parseContent:
        while(head.isPresent()){
            line = head.get();
            if (line.getLinedType() == LinedType.HEADING){
                /// `Head`:
                pointer.rollBack(); /// head = empty = exit while
                head = Optional.empty();
            } else {
                assert line.getLinedType() == LinedType.OUTLINE;
                if (line.getLevel() > level){
                    /// `Out > Level`:
                    head = parseOutline(children, pointer, line, level + 1);
                } else {
                    /// `Out = Level`, `Out < Level`:
                    assert line.getLevel() == level || line.getLevel() < level;
                    pointer.rollBack();
                    head = Optional.empty(); /// head = empty = exit while
                }
            }
        }
        /// `empty`:
        parent.add(new SectionSpanOutline(children));
        return parseHead(pointer);
    }
}