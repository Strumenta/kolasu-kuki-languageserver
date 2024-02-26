package com.strumenta.kuki.mapping

import KukiParser.*
import com.strumenta.kolasu.mapping.ParseTreeToASTTransformer
import com.strumenta.kolasu.mapping.translateCasted
import com.strumenta.kolasu.mapping.translateList
import com.strumenta.kolasu.mapping.translateOnlyChild
import com.strumenta.kolasu.model.ReferenceByName
import com.strumenta.kuki.ast.*

class KukiParseTreeMapper : ParseTreeToASTTransformer() {
    init {
        registerNodeFactory(RecipeContext::class) { context -> Recipe(
            name = context.title.joinToString(" ") { it.text },
            yield = context.yield.text.toInt(),
            ingredients = translateList(context.ingredients),
            utensils = translateList(context.utensils),
            steps = translateList(context.steps))
        }
        registerNodeFactory(IngredientContext::class) { context -> Ingredient(
            declaration = translateCasted(context.itemDeclaration()),
            amount = context.amount?.text?.toDouble(),
            unit = translateEnum<MeasureUnit>(context.unit?.text))
        }
        registerNodeFactory(UtensilContext::class) { context -> Utensil(
            declaration = translateCasted(context.name))
        }
        registerNodeFactory(StepContext::class) { context -> translateOnlyChild(context) }
        registerNodeFactory(ActionContext::class) { context -> translateOnlyChild(context) }
        registerNodeFactory(CreationContext::class) { context -> Creation(
            name = context.name.text,
            items = context.items.items.map { translateCasted(it) },
            target = translateCasted(context.target))
        }
        registerNodeFactory(SpaceContext::class) { context -> Spatial(
            name = context.name.text,
            items = context.items.items.map { translateCasted(it) },
            target = translateCasted(context.target))
        }
        registerNodeFactory(TemperatureContext::class) { context -> Temperature(
            name = context.name.text,
            items = context.items.items.map { translateCasted(it) },
            target = context.amount.text.toDouble(),
            unit = translateEnum<TemperatureUnit>(context.unit.text)!!)
        }
        registerNodeFactory(TimeContext::class) { context -> Temporal(
            name = context.name.text,
            items = context.items.items.map { translateCasted(it) },
            target = context.amount.text.toDouble(),
            unit = translateEnum<TimeUnit>(context.unit.text)!!)
        }
        registerNodeFactory(SingularContext::class) { context -> Singular(
            name = context.name.text,
            items = context.items.items.map { translateCasted(it) }
        )}
        registerNodeFactory(ItemDeclarationContext::class) { context ->
            ItemDeclaration(context.ID().joinToString(" ").lowercase())
        }
        registerNodeFactory(ItemReferenceContext::class) { context ->
            ItemReference(ReferenceByName(name = context.ID().joinToString(" ").lowercase()))
        }
    }
}

inline fun <reified T : Enum<T>> translateEnum(name: String?): T? {
    return enumValues<T>().find { it.name == name }
}
