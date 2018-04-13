# Coding Standards

## Source File Structure
- Class name:
    - is uppercase cameral
    - have some organizaiton system when sorted by alphebetally in a package
- Beginning comments are currently not used
- Import
    - is group by libraries in the following order:
        1. JUnit
        2. Java standard library
        3. Google Guava library
        4. JavaFX and RichTextFX (and only in window and stat package).
        5. Apache PDFBox (and only in export package).
        6. project libraries
    - use wildcard if possible
    - put the import classes in comment at the end of each wildcard statement, or
        `// (many)` for 4 or more classes
    - no static import except for JUnit, Checker, and AuxiliaryData
    - no line wrapping
- Class comment
    - short describing on the first line, and a noun phrase.
    - if the class is the head of the group of classes add purpose undered list
- order of class elements:
    - initalization methods first
    - the major and complex methods second, group by feature / method relations
    - getters before setters and after the the major methods
    - overriding methods last, order from direct parent class to `Object` class
- the order can be change when grouping is involved.
- no new line after the last brace

## Formattting and Comments
- Special characters
    - use the actual unicode character or special escape sequence
- Indentation: 4 spaces
- Column limit: 80 characters
### Class declation
- use the lowest access
- add final if possible
- keep the implementing interface on the same line
- wrap line with 2 indents
- add a new line after the class name
### Class variables
- don't comment them, names should be enough to describe them
- use 2+ words
- group by not mutable and mutable
- maintain order in the order of use
### Methods
- comments
    - no comments for overrided
    - one short description
    - for `@link`: only for project library class, and don't repeat
    - for `@linkplain`: for all other classes
    - for `@param`
        - use `the value` for set methods or
        - use long description using the parameter name then
        - simicolon and requirements
    - for `@return`
        - use `self`, or `answer`
    - for `@throws`
        - try to be descriptive, but mostly copy and paste text
    - for `@see`
        - for set and get methods, its counterpart
        - for private methods, its callers
- declaration
    - keep throws in the same line
    - reduce the amount of parameters
    - wrapped lines are two indent
    - parameters are 1 word, lowercase
- method layout
    - add assert statement (for private methods) or Checker methods
        - Construtor methods:
            - allows mix between class field initalization and checking
            - don't check for variables for the super method, unless it is being
              used
    - new line, unless the rest of the code is 1 line
    - group statements by groups with a comments at their top
    - method variables are a 1 word, lowercase
- llambda
    - parameter is a single letter
    - add class name comment as needed: `//c = Object`
    - for single variable and statment, remove brackets (`()` / `{}`).
- braces
    - usually add them, unless making things tighter is easier to read
    - if not using braces keep the statement in a single line:
       `if (correct) return true;`
    - add braces on the same line for empty block: `pubic void do(){}`
    - for `case`, leave as the same indent as the `switch` statement
- wrapping
    - if a line needs to be wrapped, either:
        - wrap with indent for shorter statments
        - wrap at the most subjectively most readiable way[^line-wrap]
    - if line is break at the bracket:
        - add the end bracket at the next next lines
        - content in the bracket gets another indent
[^line-wrap]: althought do attempt to reduce the amount of line breaks
- spacing
    - add one after comma,
    - add one after `)` (collapse space with `{`'s)
    - add one before and after `:`
    - add one before `{` (collapse space with `)`'s)
    - add one after `?`
- grouping parentheses: always
- for enum and arrays
    - wrap line and new lines for grouping
    - if enum has it only method, always keep one item per line
- horizontal alignment are use if the code of line are copy, paste and edit.
- annotation are in its only line
- comments use:
    - `///` for documation, `//` for temperatory code commetted out
    - `/*`... `*/` only for code commented out
    - `//` for to do item