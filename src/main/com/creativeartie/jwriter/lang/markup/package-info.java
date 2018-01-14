/**
 * Parse text to {@link SpanBranch} with {@link StyleInfo}. <br />
 *
 * Text is parsed with classes with {@code Parse}. {@linkplain SpanBranch}
 * classes are name with {@code Span} and {@link StyleInfo} are named with
 * {@code Type}.
 *
 * Generally, {@code Span} subclasses' return variables are inferred from the
 * list of children {@link Span}.
 *
 * The alphabet order of this package (excluding this file), shows the smallest
 * parts to largest parts. The prefix of each class means one of the following:
 *
 * <ol>
 * <li>{@code Basic}    : Basic text, interface two different {@link Span}.</li>
 * <li>{@code Content}  : Text span for many other {@link SpanBranch}</li>
 * <li>{@code Directory}: Span indiciates an id</li>
 * <li>{@code Edition}  : Span showing the status of a document section</li>
 * <li>{@code Format}   : Text span for storing formats</li>
 * <li>{@code InfoData} : Data span described by {@link InfoField}.</li>
 * <li>{@code InfoField}: Field span that describe {@code InfoData}.</li>
 * <li>{@code Lined}    : Span that store a line of text</li>
 * <li>{@code Main}     : Span that store one or more lines of text</li>
 * <li>{@code Section}  : Table of Content of the document</li>
 * </ol>
 */
package com.creativeartie.jwriter.lang.markup;