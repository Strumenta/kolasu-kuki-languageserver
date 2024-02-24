package com.strumenta.kuki.semantics

import com.strumenta.kolasu.model.Node
import com.strumenta.kuki.ast.*

class KukiSymbolResolver : com.strumenta.kolasu.languageserver.SymbolResolver {
    private val declarations = mutableMapOf<String, ItemDeclaration>()
    private lateinit var recipe: Recipe

    override fun resolveSymbols(tree: Node?, uri: String) {
        if (tree == null) return
        declarations.clear()
        recipe = tree as Recipe

        for (ingredient in recipe.ingredients) {
            declarations[ingredient.declaration.name.lowercase()] = ingredient.declaration
        }

        for (utensil in recipe.utensils) {
            declarations[utensil.declaration.name.lowercase()] = utensil.declaration
        }

        for (step in recipe.steps) {
            when (step) {
                is Mix -> {
                    for (item in step.items) {
                        item.reference.referred = declarations[item.reference.name]
                    }
                    declarations[step.target.name.lowercase()] = step.target
                }

                is Cut -> {
                    for (item in step.items) {
                        item.reference.referred = declarations[item.reference.name]
                    }
                    declarations[step.target.name.lowercase()] = step.target
                }

                is Place -> {
                    for (item in step.items) {
                        item.reference.referred = declarations[item.reference.name]
                    }
                    declarations[step.target.name.lowercase()] = step.target
                }

                is Heat -> {
                    for (item in step.items) {
                        item.reference.referred = declarations[item.reference.name]
                    }
                }

                is Bake -> {
                    for (item in step.items) {
                        item.reference.referred = declarations[item.reference.name]
                    }
                }
            }
        }
    }
}

