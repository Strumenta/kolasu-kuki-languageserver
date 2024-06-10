package com.strumenta.kuki.ast

import com.strumenta.kolasu.model.Named
import com.strumenta.kolasu.model.Node
import com.strumenta.kolasu.model.ReferenceByName
import kotlin.String

data class Recipe(val name: Name, val yield: Int, val ingredients: List<Ingredient>, val utensils: List<Utensil>, val steps: List<Step>) : Node()

data class Ingredient(val declaration: Name, val amount: Number?, val unit: MeasureUnit?) : Node()
data class Utensil(val declaration: Name) : Node()

sealed class Step : Node(), Named

data class Creation(override val name: String, val items: List<ItemReference>, val target: Name) : Step()
data class Temporal(override val name: String, val items: List<ItemReference>, val target: Number, val unit: TimeUnit) : Step()
data class Temperature(override val name: String, val items: List<ItemReference>, val target: Number, val unit: TemperatureUnit) : Step()
data class Spatial(override val name: String, val items: List<ItemReference>, val target: ItemReference) : Step()
data class Singular(override val name: String, val items: List<ItemReference>) : Step()

data class ItemReference(val reference: ReferenceByName<Name>): Node()

enum class MeasureUnit { g, ml }
enum class TimeUnit { seconds, minutes, hours }
enum class TemperatureUnit { degrees, farenheit }

data class Name(override val name: String): Node(), Named
