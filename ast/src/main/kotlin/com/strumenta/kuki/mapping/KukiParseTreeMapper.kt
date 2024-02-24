package com.strumenta.kuki.mapping

import KukiParser.*
import com.strumenta.kolasu.mapping.ParseTreeToASTTransformer
import com.strumenta.kolasu.mapping.translateCasted
import com.strumenta.kolasu.mapping.translateList
import com.strumenta.kolasu.mapping.translateOnlyChild
import com.strumenta.kolasu.model.ReferenceByName
import com.strumenta.kolasu.parsing.withParseTreeNode
import com.strumenta.kuki.ast.*
import kotlin.reflect.KClass

class KukiParseTreeMapper : ParseTreeToASTTransformer() {
    init {
        registerNodeFactory(RecipeContext::class) { context ->
            Recipe(name = context.title.text, ingredients = translateList(context.ingredients), utensils = translateList(context.utensils), steps = translateList(context.steps))
        }
        registerNodeFactory(IngredientContext::class) { context ->
            Ingredient(declaration = translateCasted(context.item_declaration()), amount = context.amount.text.toDouble(), unit = translateEnum<MeasureUnit>(context.unit?.text))
        }
        registerNodeFactory(UtensilContext::class) { context ->
            Utensil(declaration = translateCasted(context.name))
        }
        registerNodeFactory(StepContext::class) { context ->
            translateOnlyChild(context)
        }
        registerNodeFactory(ActionContext::class) { context ->
            translateOnlyChild(context)
        }
        registerNodeFactory(MixContext::class) { context ->
            Mix(name = "mix", items = translateList(context.items), target = translateCasted(context.target))
        }
        registerNodeFactory(CutContext::class) { context ->
            Cut(name = "cut", items = translateList(context.items), target = translateCasted(context.target))
        }
        registerNodeFactory(PlaceContext::class) { context ->
            Place(name = "place", items = translateList(context.items), target = translateCasted(context.target))
        }
        registerNodeFactory(HeatContext::class) { context ->
            Heat(name = "heat", items = translateList(context.items), target = context.amount.text.toDouble(), unit = translateEnum<TemperatureUnit>(context.unit.text)!!)
        }
        registerNodeFactory(BakeContext::class) { context ->
            Bake(name = "bake", items = translateList(context.items), target = context.amount.text.toDouble(), unit = translateEnum<TimeUnit>(context.unit.text)!!)
        }
        registerNodeFactory(Item_declarationContext::class) { context ->
            ItemDeclaration(context.text)
        }
        registerNodeFactory(ReferenceContext::class) { context ->
            IngredientReference(ReferenceByName(name = context.text))
        }
    }
}

inline fun <reified T : Enum<T>> translateEnum(name: String?): T? {
    return enumValues<T>().find { it.name == name }
}
