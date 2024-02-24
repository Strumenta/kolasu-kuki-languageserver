package com.strumenta.kuki.semantics

import com.strumenta.kolasu.model.Node
import com.strumenta.kolasu.semantics.*
import com.strumenta.kuki.ast.*
import com.strumenta.kolasu.traversing.findAncestorOfType
import com.strumenta.kolasu.validation.Issue
import kotlin.reflect.KClass

fun kukiSemantics(issues: MutableList<Issue> = mutableListOf()) = semantics(issues) {
    symbolResolver {
        // resolution rules
        scopeFor(IngredientReference::reference) {
            scope {
                it.findAncestorOfType(Recipe::class.java)?.ingredients?.forEach{ define(it.declaration) }
            }
        }
        // construction rules (scope-wide representations for nodes)
        scopeFrom(Ingredient::class) {
            scope {
                define(it.declaration)
            }
        }
    }
}
