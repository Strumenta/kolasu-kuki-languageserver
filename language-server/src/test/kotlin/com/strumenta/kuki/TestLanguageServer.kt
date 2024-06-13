package com.strumenta.kuki

import com.google.gson.JsonObject
import com.strumenta.kolasu.languageserver.testing.TestKolasuServer
import com.strumenta.kuki.ast.Recipe
import com.strumenta.kuki.languageserver.KukiServer
import com.strumenta.kuki.parser.KukiKolasuParser
import org.eclipse.lsp4j.DidChangeConfigurationParams
import org.eclipse.lsp4j.InitializeParams
import org.eclipse.lsp4j.InitializedParams
import org.eclipse.lsp4j.Position
import org.eclipse.lsp4j.SemanticTokensParams
import org.eclipse.lsp4j.TextDocumentIdentifier
import org.eclipse.lsp4j.WorkspaceFolder
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class TestAST : TestKolasuServer<Recipe>(KukiKolasuParser(), language = "kuki", fileExtensions = listOf("kuki")) {

    private var example = Paths.get("..", "examples", "Almond cookies.kuki").toUri().toString()
    private val code = Paths.get("..", "examples", "Almond cookies.kuki").toFile().readText()

    override fun initializeServer() {
        server = KukiServer()
        expectDiagnostics(0)

        val workspace = workspacePath.toUri().toString()
        server.initialize(InitializeParams().apply { workspaceFolders = mutableListOf(WorkspaceFolder(workspace)) })
        server.initialized(InitializedParams())

        val configuration = JsonObject()
        configuration.add(language, JsonObject())
        server.didChangeConfiguration(DidChangeConfigurationParams(configuration))
    }

    @Test
    fun testSymbolNavigation() {
        expectDiagnostics(0)

        open(example, code)

        val definition = definition(example, Position(13, 15))
        assertNotNull(definition)
        assertEquals(3, definition.range.start.line)

        val references = references(example, Position(13, 15), true)
        assertNotNull(references)
        assertEquals(2, references.size)
    }

    @Test
    fun testOutline() {
        expectDiagnostics(0)

        open(example, code)

        val outline = outline(example)
        assertNotNull(outline)
        assertEquals(13, outline.children.size)
    }

    @Test
    fun testSemanticTokens() {
        expectDiagnostics(0)

        open(example, code)

        val semanticTokens = server.semanticTokensFull(SemanticTokensParams(TextDocumentIdentifier(example))).get()
        assertNotNull(semanticTokens)
        assertEquals(95, semanticTokens.data.size)
    }
}