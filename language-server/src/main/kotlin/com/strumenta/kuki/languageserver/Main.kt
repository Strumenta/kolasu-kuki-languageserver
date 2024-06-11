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

        fun addIngredientAt(position: com.strumenta.kolasu.model.Position?, isDeclaration: Boolean = false) {
            if (position != null) {
                tokens.add(SemanticToken(position, SemanticTokenType.VARIABLE, if (isDeclaration) listOf(SemanticTokenModifier.DECLARATION) else listOf(SemanticTokenModifier.READ_ONLY)))
            }
        }
        fun addUtensilAt(position: com.strumenta.kolasu.model.Position?, isDeclaration: Boolean = false) {
            if (position != null) {
                tokens.add(SemanticToken(position, SemanticTokenType.STRUCT, if (isDeclaration) listOf(SemanticTokenModifier.DECLARATION) else listOf(SemanticTokenModifier.READ_ONLY)))
            }
        }
        fun addTokenFor(item: ItemReference) {
            when (item.reference.referred?.parent) {
                is Ingredient -> addIngredientAt(item.position)
                is Utensil -> addUtensilAt(item.position)
                else -> addIngredientAt(item.position)
            }
        }

        val recipe = files[params.textDocument.uri]?.root ?: return CompletableFuture.completedFuture(encode(tokens))

        val titlePosition = recipe.name.position ?: return CompletableFuture.completedFuture(encode(tokens))
        tokens.add(SemanticToken(titlePosition, SemanticTokenType.TYPE, listOf()))

        for (ingredient in recipe.ingredients) {
            addIngredientAt(ingredient.declaration.position, isDeclaration = true)
        }
        for (utensil in recipe.utensils) {
            addUtensilAt(utensil.position, isDeclaration = true)
        }
        for (step in recipe.steps) {
            when (step) {
                is Creation -> {
                    step.items.forEach { addTokenFor(it) }
                    addIngredientAt(step.target.position)
                }
                is Spatial -> {
                    step.items.forEach { addTokenFor(it) }
                    addTokenFor(step.target)
                }
                is Singular -> {
                    step.items.forEach { addTokenFor(it) }
                }
                is Temperature -> {
                    step.items.forEach { addTokenFor(it) }
                }
                is Temporal -> {
                    step.items.forEach { addTokenFor(it) }
                }
            }
        }

        return CompletableFuture.completedFuture(encode(tokens))
    }
}