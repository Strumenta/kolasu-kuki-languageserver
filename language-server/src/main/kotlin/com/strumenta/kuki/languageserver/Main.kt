package com.strumenta.kuki.languageserver

import com.strumenta.kuki.parser.KukiKolasuParser
import com.strumenta.kolasu.languageserver.KolasuServer
import com.strumenta.kolasu.model.Node
import com.strumenta.kuki.ast.*
import com.strumenta.kuki.codegenerator.KukiCodeGenerator
import org.eclipse.lsp4j.*
import org.eclipse.lsp4j.jsonrpc.messages.Either
import org.eclipse.lsp4j.jsonrpc.messages.Either3
import java.util.concurrent.CompletableFuture

fun main() {
    val server = KukiServer()
    server.startCommunication()
}

class KukiServer : KolasuServer<Recipe>(KukiKolasuParser(), "kuki", listOf("kuki"), generator = KukiCodeGenerator()) {
    override fun symbolKindOf(node: Node): SymbolKind {
        return if (node is Step) {
            SymbolKind.Function
        } else if (node.parent is Utensil) {
            SymbolKind.Struct
        } else {
            SymbolKind.Variable
        }
    }

    override fun initialize(params: InitializeParams?): CompletableFuture<InitializeResult> {
        val workspaceFolders = params?.workspaceFolders
        if (workspaceFolders != null) {
            for (folder in workspaceFolders) {
                folders.add(folder.uri)
            }
        }

        val capabilities = ServerCapabilities()
        capabilities.workspace = WorkspaceServerCapabilities(WorkspaceFoldersOptions().apply { supported = true; changeNotifications = Either.forLeft("didChangeWorkspaceFoldersRegistration") })
        capabilities.setTextDocumentSync(TextDocumentSyncOptions().apply {
            openClose = true
            change = TextDocumentSyncKind.Full
            save = Either.forLeft(true)
        })
        capabilities.setDocumentSymbolProvider(true)
        capabilities.setDefinitionProvider(true)
        capabilities.setReferencesProvider(true)

        val semanticHighlighting = SemanticTokensWithRegistrationOptions()
        semanticHighlighting.legend = SemanticTokensLegend(listOf("type", "class", "interface", "enum", "function", "variable"), listOf());
        semanticHighlighting.full = Either.forLeft(true)
        capabilities.semanticTokensProvider = semanticHighlighting

        return CompletableFuture.completedFuture(InitializeResult(capabilities))
    }

    override fun semanticTokensFull(params: SemanticTokensParams): CompletableFuture<SemanticTokens> {

        val parsingResult = this.files.get(params.textDocument.uri)
        val data = listOf(0, 0, 5, 4, 0,  1, 6, 3, 2, 0)
        return CompletableFuture.completedFuture(SemanticTokens(data))
    }
}

