package com.strumenta.kuki.languageserver

import com.strumenta.kuki.parser.KukiKolasuParser
import com.strumenta.kolasu.languageserver.KolasuServer
import com.strumenta.kolasu.model.Node
import com.strumenta.kuki.ast.*
import com.strumenta.kuki.codegenerator.KukiCodeGenerator
import com.strumenta.kuki.semanticHighlighting.SemanticToken
import com.strumenta.kuki.semanticHighlighting.SemanticTokenModifier
import com.strumenta.kuki.semanticHighlighting.SemanticTokenType
import com.strumenta.kuki.semanticHighlighting.encode
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

        capabilities.semanticTokensProvider = SemanticTokensWithRegistrationOptions().apply {
            legend = SemanticTokensLegend(SemanticTokenType.values().map { it.legendName }, SemanticTokenModifier.values().map { it.legendName });
            full = Either.forLeft(true)
        }

        return CompletableFuture.completedFuture(InitializeResult(capabilities))
    }

    override fun semanticTokensFull(params: SemanticTokensParams): CompletableFuture<SemanticTokens> {
        val tokens = mutableListOf<SemanticToken>()

        val recipe = files[params.textDocument.uri]?.root ?: return CompletableFuture.completedFuture(encode(tokens))
        val p = recipe.name.position ?: return CompletableFuture.completedFuture(encode(tokens))
        tokens.add(SemanticToken(p, SemanticTokenType.TYPE, listOf()))
        return CompletableFuture.completedFuture(encode(tokens))
    }
}