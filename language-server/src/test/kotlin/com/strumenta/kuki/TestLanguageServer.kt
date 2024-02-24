import com.strumenta.kolasu.languageserver.testing.TestKolasuServer
import com.strumenta.kuki.ast.Recipe
import com.strumenta.kuki.languageserver.KukiCodeGenerator
import com.strumenta.kuki.parser.KukiKolasuParser
import org.eclipse.lsp4j.Position
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import java.io.File
import java.nio.file.Paths

class TestAST : TestKolasuServer<Recipe>(KukiKolasuParser(), language = "kuki", fileExtensions = listOf("kuki")) {
    private var example = Paths.get("..", "examples", "cookies.kuki").toUri().toString()
    private val code = File(Paths.get("..", "examples", "cookies.kuki").toUri()).readText()
    @Test
    fun testLanguageServer() {
        expectDiagnostics(0)

        open(example, code)

        val outline = outline(example)!!

        val recipe = outline.children.first()

        assertEquals(9, recipe.children.size)

        assertEquals("Named tree", outline.name)
        assertEquals("Almond", recipe.children.first().name)

        val definition = definition(example, Position(11, 15))
        assertNotNull(definition)
        val references = references(example, Position(11, 15), true)
        assertNotNull(references)
    }
}