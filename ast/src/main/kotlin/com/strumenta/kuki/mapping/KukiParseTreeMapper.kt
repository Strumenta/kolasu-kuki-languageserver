package com.strumenta.kuki.mapping

import KukiParser.*
import com.strumenta.kolasu.mapping.ParseTreeToASTTransformer
import com.strumenta.kolasu.mapping.translateList
import com.strumenta.kolasu.mapping.translateOnlyChild
import com.strumenta.kuki.ast.*

class KukiParseTreeMapper : ParseTreeToASTTransformer() {
    init {
        registerNodeFactory(RecipeContext::class) { context ->
            Recipe(name = context.title.text, ingredients = translateList(context.ingredients), steps = translateList(context.steps))
        }
        registerNodeFactory(IngredientContext::class) { context ->
            Ingredient(name = context.name.text, amount = context.amount.text.toDouble(), unit = MeasureUnit.values().find { it.name == context.unit.text }!!)
        }
        registerNodeFactory(StepContext::class) { context ->
            translateOnlyChild(context)
        }
        registerNodeFactory(ActionContext::class) { context ->
            translateOnlyChild(context)
        }
        registerNodeFactory(MixContext::class) { context ->
            Mix(items = context.items.map { it.text }, target = context.target.text)
        }
        registerNodeFactory(CutContext::class) { context ->
            Cut(items = context.items.map { it.text }, target = context.target.text)
        }
        registerNodeFactory(PlaceContext::class) { context ->
            Place(items = context.items.map { it.text }, target = context.target.text)
        }
        registerNodeFactory(HeatContext::class) { context ->
            Heat(items = context.items.map { it.text }, target = context.amount.text.toDouble(), unit = TemperatureUnit.values().find { it.name == context.unit.text }!!)
        }
        registerNodeFactory(BakeContext::class) { context ->
            Bake(items = context.items.map { it.text }, target = context.amount.text.toDouble(), unit = TimeUnit.values().find { it.name == context.unit.text }!!)
        }
    }
}
