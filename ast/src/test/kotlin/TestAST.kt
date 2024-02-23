import com.strumenta.kuki.parser.KukiKolasuParser
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import java.io.File

class TestAST {
    @Test
    fun testSpecificFile() {
        val code = File("../examples/cookies.kuki").readText()
        val parser = KukiKolasuParser()
        val parsingResult = parser.parse(code)
        assertEquals(0, parsingResult.issues.size)
    }
}