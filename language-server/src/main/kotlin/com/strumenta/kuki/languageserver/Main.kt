package com.strumenta.kuki.languageserver

import com.strumenta.kolasu.languageserver.CodeGenerator
import com.strumenta.kuki.parser.KukiKolasuParser
import com.strumenta.kolasu.languageserver.KolasuServer
import com.strumenta.kolasu.languageserver.SymbolResolver
import com.strumenta.kolasu.model.Node
import com.strumenta.kuki.ast.*
import org.eclipse.lsp4j.*
import org.eclipse.lsp4j.jsonrpc.messages.Either
import java.io.File
import java.net.URI
import java.nio.file.Files
import java.nio.file.Paths
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
        capabilities.setTextDocumentSync(TextDocumentSyncOptions().apply { openClose = true; change = TextDocumentSyncKind.Full; save = Either.forLeft(true) })
        capabilities.setDocumentSymbolProvider(true)
        capabilities.setDefinitionProvider(true)
        capabilities.setReferencesProvider(true)

        return CompletableFuture.completedFuture(InitializeResult(capabilities))
    }
}

class KukiCodeGenerator : CodeGenerator<Recipe> {
    override fun generate(tree: Recipe, uri: String) {
        for (amount in listOf(1, 2, 4)) {
            val file = File(URI(uri)).parent + File.separator + tree.name + File.separator + tree.name + " for $amount.txt"
            val code = generateRecipeFor(amount, tree)

            File(file).parentFile.mkdirs()
            File(file).writeText(code)
        }
    }

    private fun generateRecipeFor(amount: Int, recipe: Recipe): String {
        val code =
"""
${recipe.name}

INGREDIENTS
${recipe.ingredients.joinToString("\n") { "- ${it.declaration.name}: ${it.amount.toInt() * amount} ${it.unit?.name ?: ""}" }}

UTENSILS
${recipe.utensils.joinToString("\n") { "- ${it.declaration.name}" }}

STEPS
${recipe.steps.joinToString("\n") {
    when (it) {
        is Mix -> "${recipe.steps.indexOf(it) + 1}. ${it.name.replaceFirstChar { x -> x.uppercase() }} the ${code(it.items)} into ${it.target.name}."
        is Cut -> "${recipe.steps.indexOf(it) + 1}. ${it.name.replaceFirstChar { x -> x.uppercase() }} the ${code(it.items)} in ${it.target.name}."
        is Place -> "${recipe.steps.indexOf(it) + 1}. ${it.name.replaceFirstChar { x -> x.uppercase() }} the ${code(it.items)} in ${it.target.name}."
        is Heat -> "${recipe.steps.indexOf(it) + 1}. ${it.name.replaceFirstChar { x -> x.uppercase() }} the ${code(it.items)} to ${it.target} ${it.unit.name}."
        is Bake -> "${recipe.steps.indexOf(it) + 1}. ${it.name.replaceFirstChar { x -> x.uppercase() }} the ${code(it.items)} for ${it.target} ${it.unit.name}."
    }
}}
""".trimIndent()
        return code
    }

    private fun code(items: List<IngredientReference>): String {
        return if (items.size == 1) {
            items[0].reference.name
        } else {
            items.subList(0, items.size - 1).joinToString(", ") { it.reference.name } + " and " + items[items.size - 1].reference.name
        }
    }
}