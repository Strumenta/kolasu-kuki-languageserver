package com.strumenta.kuki.ast

import KukiParser.*
import com.strumenta.kolasu.mapping.ParseTreeToASTTransformer
import com.strumenta.kolasu.mapping.translateList

class KukiParseTreeMapper : ParseTreeToASTTransformer() {
    init {
        registerNodeFactory(RecipeContext::class) { context ->
            Recipe(name = context.title.text, ingredients = translateList(context.ingredients), steps = translateList(context.steps))
        }
        registerNodeFactory(IngredientContext::class) { context ->
            Ingredient(name = context.name.text, amount = context.amount.text.toDouble(), unit = MeasureUnit.values().find { it.name == context.unit.text }!!)
        }
    }
}
