package com.strumenta.kuki.ast

import com.strumenta.kolasu.model.Named
import com.strumenta.kolasu.model.Node
import com.strumenta.kolasu.model.ReferenceByName
import kotlin.String

data class Recipe(override val name: String, val ingredients: List<Ingredient>, val utensils: List<Utensil>, val steps: List<Step>) : Node(), Named

data class Ingredient(val declaration: ItemDeclaration, val amount: Number, val unit: MeasureUnit?) : Node()
data class Utensil(val declaration: ItemDeclaration) : Node()

sealed class Step : Node(), Named

data class Mix(override val name: String, val items: List<IngredientReference>, val target: ItemDeclaration) : Step()
data class Cut(override val name: String, val items: List<IngredientReference>, val target: ItemDeclaration) : Step()
data class Place(override val name: String, val items: List<IngredientReference>, val target: ItemDeclaration) : Step()
data class Heat(override val name: String, val items: List<IngredientReference>, val target: Number, val unit: TemperatureUnit) : Step()
data class Bake(override val name: String, val items: List<IngredientReference>, val target: Number, val unit: TimeUnit) : Step()

data class IngredientReference(val reference: ReferenceByName<ItemDeclaration>): Node()
data class ItemDeclaration(override val name: String): Node(), Named

enum class MeasureUnit { g, ml }
enum class TimeUnit { seconds, minutes, hours }
enum class TemperatureUnit { degrees, Farenheit }
