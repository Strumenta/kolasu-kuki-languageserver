package com.strumenta.kuki.languageserver

import com.strumenta.kuki.parser.KukiKolasuParser
import com.strumenta.kolasu.languageserver.KolasuServer
import com.strumenta.kolasu.model.Node
import com.strumenta.kuki.ast.Recipe
import com.strumenta.kuki.ast.Step
import com.strumenta.kuki.ast.Utensil
import com.strumenta.kuki.semantics.KukiSymbolResolver
import org.eclipse.lsp4j.SymbolKind

fun main() {
    val server = KukiServer()
    server.startCommunication()
}

class KukiServer : KolasuServer<Recipe>(KukiKolasuParser(), "kuki", listOf("kuki"), KukiSymbolResolver()) {
    override fun symbolKindOf(node: Node): SymbolKind {
        return if (node is Step) {
            SymbolKind.Function
        } else if (node.parent is Utensil) {
            SymbolKind.Struct
        } else {
            SymbolKind.Variable
        }
    }
}