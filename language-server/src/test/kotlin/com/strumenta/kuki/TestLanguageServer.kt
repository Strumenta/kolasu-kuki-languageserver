import com.strumenta.kolasu.languageserver.testing.TestKolasuServer
import com.strumenta.kuki.ast.Recipe
import com.strumenta.kuki.parser.KukiKolasuParser
import org.eclipse.lsp4j.Position
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths

private val cwd = File("").absoluteFile
private val examplesDirectory = File(cwd.parent, "examples")
private val File.uriString
    get() = this.toPath().toUri().toString()

class TestAST : TestKolasuServer<Recipe>(KukiKolasuParser(), language = "kuki", fileExtensions = listOf("kuki")) {
    private var example = File(examplesDirectory, "Almond cookies.kuki").uriString
    private val code = File(examplesDirectory, "Almond cookies.kuki").readText()

    @Test
    fun testLanguageServer() {
        expectDiagnostics(0)

        open(example, code)

        val outline = outline(example)!!

        val recipe = outline.children.first()

        assertEquals(12, recipe.children.size)

        assertEquals("Named tree", outline.name)
        assertEquals("almond", recipe.children.first().name)

        // TODO FIX ME
        val definition = definition(example, Position(14, 11))
        assertNotNull(definition)
        val references = references(example, Position(11, 15), true)
        assertNotNull(references)
    }
}