# JWriter

## About
JWriter a word processor with note taking and goal setting ablities. It is
foused for authors to write fiction and non-fiction alike.  Notes can be put
into to the file anywhere yet allow easy to navigate to them. The goal settings
can be adjest as the document written.

Even though the markup language is made for this program, is very forgiving and
will highlight the syntax. In the future, there will be more support and will
have a [WYSIWYG mode](https://en.wikipedia.org/wiki/WYSIWYG). This make learning
the language as just a faster way to format and insert notes and agendas.

This project is currently in alpha version, and will be optimized and will have
more features.

## Markup

### Formats
There are four different formats. Formats can be combined, but are only restrict
to one line.

|Format   |Code          |
|---------|--------------|
|Italics  |`\*Text\*`    |
|Bold     |`\*\*Text\*\*`|
|Underline|`\_Text\_`    |
|Code     |`\`Text\``    |

There are also [footnote (syntax `{^footnote}`), endnote (syntax `{*endnote}`)](#note-styles),
[links (syntax `<@reference id|link text>` or `<link path|link text>`)](#hyperlink) and
[agenda (syntax `{! todo}`)](#agenda).

## Sections
Each section had a heading or an outline point at the top of the section. This
markup language allows you to have no heading or outline, but the section has to
be at the top of file. An outline point is nested inside a section with an
heading, or is before the first section with headings.


### Heading and outline

A heading or an outline can have an [id](Id) that can be linked to a different
point of file.  The syntax is `@category>id:` and it set before the first text,
other than the syntax used to indicated the heading and outline style.

There are six level for both headings and outlines.

A heading or an outline can also have a status with details, A status is
optional, and can be `#STUB`, `#DRAFT`, `#FINAL`, `#` (custom) and none. after
the status, there are details to go with the status. It will be at the end of
the line.

The syntax of a heading is:
~~~
= @category>id:Title for largest heading # status
== Title for heading 2 #DRAFT
======Smallest headings
~~~

The syntax of a outline is:
~~~
!# Largest outline point
!######Smallest outline pointe
~~~

### Main Line Styles

There are several types of line styles.

|Line styles  |Syntax    |Notes                                                |
|-------------|----------|-----------------------------------------------------|
|Numbered     | `# text` |Add tabs before `#` to increse level (up to six)     |
|Bullet       | `- text` |Add tabs before `-` to increse level (up to six)     |
|Quote        | `> text` |                                                     |
|Section Break| `***`    |It has no text after the `*` or allow spaces anywhere|
|Paragaph     | `text`   |There is no symbol used at the beginning of the line |

### Note Styles
In addition there are footnote, and endnote, that can be referred to even
outside of the section that the line belongs in. Footnotes appears at end of a
page, while endnote appears at the end of either a section or a document.

|Line styles|Syntax      |
|-----------|------------|
|Footnote   | `!^id:text`|
|Endnote    | `!*id:text`|


    NOTE(LinedParseRest.NOTE, LINED_NOTE),

    AGENDA(LinedParseRest.AGENDA, LINED_AGENDA),
    SOURCE(LinedParseCite.INSTANCE, LINED_CITE),

## Id


## Screen shots
![Main Window](design/main.png)
![Stats Window](design/stats.png)
