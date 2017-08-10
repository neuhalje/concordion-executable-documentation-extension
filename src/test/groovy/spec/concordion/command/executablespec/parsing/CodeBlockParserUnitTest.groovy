package spec.concordion.command.executablespec.parsing

import name.neuhalfen.concordion.extension.executablespec.parsing.MarkdownCodeBlockParser
import org.junit.Test

import static org.hamcrest.CoreMatchers.equalTo
import static org.hamcrest.MatcherAssert.assertThat

class CodeBlockParserUnitTest {

    @Test
    void textWithoutCodeBlock_nothingIsFound() {
        MarkdownCodeBlockParser sut = new MarkdownCodeBlockParser()

        def block = sut.findFirst("""
This is a test with a code block:

after block
""")

        assertThat(block.matches(), equalTo(false))
    }

    @Test
    void textWithoutCodeBlockEnd_nothingIsFound() {
        MarkdownCodeBlockParser sut = new MarkdownCodeBlockParser()

        def block = sut.findFirst("""
This is a test with a code block:
```
after block
""")

        assertThat(block.matches(), equalTo(false))
    }

    @Test
    void codeBlockWithoutConfig_isFound() {
        MarkdownCodeBlockParser sut = new MarkdownCodeBlockParser()

        def block = sut.findFirst("""```
block
content
```""")

        assertThat(block.matches(), equalTo(true))
        assertThat(block.extractConfig(), equalTo(""))
        assertThat(block.extractCode(), equalTo(
                """block
content"""))

    }

    @Test
    void textWithCodeBlock_isFound() {
        MarkdownCodeBlockParser sut = new MarkdownCodeBlockParser()

        def block = sut.findFirst("""``` block:config
block
content
```""")

        assertThat(block.matches(), equalTo(true))
        assertThat(block.extractConfig(), equalTo("block:config"))
        assertThat(block.extractCode(), equalTo(
                """block
content"""))

    }

    @Test
    void textWithCodeBlock_ConfigNotWithSpace_isFound() {
        MarkdownCodeBlockParser sut = new MarkdownCodeBlockParser()

        def block = sut.findFirst("""```block:config
block
content
```""")

        assertThat(block.matches(), equalTo(true))
        assertThat(block.extractConfig(), equalTo("block:config"))
        assertThat(block.extractCode(), equalTo(
                """block
content"""))

    }

    @Test
    void textWithCodeBlockInText_isFound() {
        MarkdownCodeBlockParser sut = new MarkdownCodeBlockParser()

        def block = sut.findFirst("""
This is a test with a code block:
``` block:config
block
content
```
after block
""")

        assertThat(block.matches(), equalTo(true))
        assertThat(block.extractConfig(), equalTo("block:config"))
        assertThat(block.extractCode(), equalTo(
                """block
content"""))

    }


    @Test
    void textWithTwoCodeBlocks_firstIsFound() {
        MarkdownCodeBlockParser sut = new MarkdownCodeBlockParser()

        def block = sut.findFirst("""
This is a test with a code block:
``` block:config
block
content
```

after block

```
second block: not found
```
""")

        assertThat(block.matches(), equalTo(true))
        assertThat(block.extractConfig(), equalTo("block:config"))
        assertThat(block.extractCode(), equalTo(
                """block
content"""))

    }




    @Test
    void config_languageOnly_configIsParsed() {
        MarkdownCodeBlockParser sut = new MarkdownCodeBlockParser()

        def block = sut.findFirst("""
```language
block
content
```
""")

        def config = block.parseConfig()

        assertThat(config.language, equalTo("language"))

        assertThat(config.values.size(), equalTo( 0))
    }


    @Test
    void config_languageOneParam_configIsParsed() {
        MarkdownCodeBlockParser sut = new MarkdownCodeBlockParser()

        def block = sut.findFirst("""
```language key:value
block
content
```
""")

        def config = block.parseConfig()

        assertThat(config.language, equalTo("language"))

        assertThat(config.values, equalTo( ["key": "value" ]))
    }
    @Test
    void config_languageMultipleParams_configIsParsed() {
        MarkdownCodeBlockParser sut = new MarkdownCodeBlockParser()

        def block = sut.findFirst("""
```language key:value key2:value2
block
content
```
""")

        def config = block.parseConfig()

        assertThat(config.language, equalTo("language"))

        assertThat(config.values, equalTo( ["key": "value", "key2": "value2" ]))
    }
}