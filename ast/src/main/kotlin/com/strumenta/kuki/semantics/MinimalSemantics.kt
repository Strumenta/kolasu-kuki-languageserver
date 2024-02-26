package com.strumenta.kuki.semantics

import com.strumenta.kolasu.validation.Issue
import com.strumenta.kolasu.validation.IssueSeverity
import com.strumenta.kuki.ast.*

fun resolveSymbols(recipe: Recipe, issues: MutableList<Issue>) {
    val declarations = mutableMapOf<String, ItemDeclaration>()
    val referencedDeclarations = mutableSetOf<String>()

    fun checkReferences (items: List<ItemReference>) {
        for (item in items) {
            val declaration = declarations[item.reference.name]
            if (declaration == null) {
                issues.add(Issue.semantic(item.reference.name + " is undeclared", position = item.position))
            } else {
                item.reference.referred = declaration
                referencedDeclarations.add(item.reference.name)
            }
        }
    }

    fun define (name: String, item: ItemDeclaration) {
        if (declarations[name] == null) {
            declarations[name] = item
        } else {
            issues.add(Issue.semantic("$name already declared", position = item.position))
        }
    }

    for (ingredient: Any in recipe.ingredients) {
        if (ingredient is Ingredient) {
            define(ingredient.declaration.name.lowercase(), ingredient.declaration)
        }
    }

    for (utensil: Any in recipe.utensils) {
        if (utensil is Utensil) {
            define(utensil.declaration.name.lowercase(), utensil.declaration)
        }
    }

    for (step: Any in recipe.steps) {
        if (step is Step) {
            when (step) {
                is Creation -> {
                    checkReferences(step.items)
                    define(step.target.name.lowercase(), step.target)
                }

                is Spatial -> {
                    checkReferences(step.items)
                    checkReferences(listOf(step.target))
                }

                is Temperature -> {
                    checkReferences(step.items)
                }

                is Temporal -> {
                    checkReferences(step.items)
                }

                is Singular -> {
                    checkReferences(step.items)
                }
            }
        }
    }

    for ((name, declaration) in declarations.entries) {
        if (!referencedDeclarations.contains(name)) {
            issues.add(Issue.semantic("$name is not used", IssueSeverity.INFO, declaration.position))
        }
    }
}

