import com.strumenta.kuki.parser.KukiKolasuParser
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import java.io.File

class TestAST {
    @Test
    fun testAlmondCookies() {
        val code = File("../examples/Almond cookies.kuki").readText()
        val parser = KukiKolasuParser()

        val parsingResult = parser.parse(code)

        assertEquals(0, parsingResult.issues.size)
    }

    @Test
    fun testSpanishOmelet() {
        val code = File("../examples/Spanish omelet.kuki").readText()
        val parser = KukiKolasuParser()

        val parsingResult = parser.parse(code)

        assertEquals(0, parsingResult.issues.size)
    }

    @Test
    fun testPastaCarbonara() {
        val code = File("../examples/Spaghetti carbonara.kuki").readText()
        val parser = KukiKolasuParser()

        val parsingResult = parser.parse(code)

        assertEquals(0, parsingResult.issues.size)
    }
}