# File for test data in [SectionSupplementTest.java](../../src/test/java/com/creativeartie/writerstudio/lang/markup/SectionSupplementTest.java)

The following code (copied from `test()` method) will produce the text of this
file: [sectionDebug_note.txt](sectionDebug_note.txt)
~~~java
WritingText[] texts = new WritingText[FILE_SIZE];
for (int i = 1; i <= FILE_SIZE; i++){
    File file = new File("build/resources/test/sectionDebug" + i + ".txt");
    texts[i - 1] = new WritingText(Files
        .asCharSource(file, Charsets.UTF_8).read());
    System.out.println(texts[i - 1]);
    System.out.println();
}
~~~

The resulting file is then input into [section_debug.py](section_debug.py). The bash script
[test_section_debug_py.sh](test_section_debug_py.sh) will do this part automatically.

Once done, the file will needed to be copy into
[src/test/resources/sectionDebugOutcomes.txt].

# File for test file [pdf-stress.txt](src/test/resources/pdf-stress.txt)
This file is made from repeating [stressTestBase.txt](), (maybe).

# Manual Tests For `src.creativeartie.writerstudio.window` package

List of tests
[ ] add each line
[ ] create heading and select
