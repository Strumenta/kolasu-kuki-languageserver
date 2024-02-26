package com.strumenta.kuki.ast

import com.strumenta.kolasu.model.Named
import com.strumenta.kolasu.model.Node
import com.strumenta.kolasu.model.ReferenceByName
import kotlin.String

data class Recipe(override val name: String, val yield: Int, val ingredients: List<Ingredient>, val utensils: List<Utensil>, val steps: List<Step>) : Node(), Named

data class Ingredient(val declaration: ItemDeclaration, val amount: Number?, val unit: MeasureUnit?) : Node()
data class Utensil(val declaration: ItemDeclaration) : Node()

sealed class Step : Node(), Named

data class Creation(override val name: String, val items: List<ItemReference>, val target: ItemDeclaration) : Step()
data class Temporal(override val name: String, val items: List<ItemReference>, val target: Number, val unit: TimeUnit) : Step()
data class Temperature(override val name: String, val items: List<ItemReference>, val target: Number, val unit: TemperatureUnit) : Step()
data class Spatial(override val name: String, val items: List<ItemReference>, val target: ItemReference) : Step()
data class Singular(override val name: String, val items: List<ItemReference>) : Step()

data class ItemDeclaration(override val name: String): Node(), Named
data class ItemReference(val reference: ReferenceByName<ItemDeclaration>): Node()

enum class MeasureUnit { g, ml }
enum class TimeUnit { seconds, minutes, hours }
enum class TemperatureUnit { degrees, farenheit }
