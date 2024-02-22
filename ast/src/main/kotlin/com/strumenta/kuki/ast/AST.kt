package com.strumenta.kuki.ast

import com.strumenta.kolasu.model.Named
import com.strumenta.kolasu.model.Node
import kotlin.String

data class Recipe(override val name: String, val ingredients: List<Ingredient>, val steps: List<Step>) : Node(), Named

data class Ingredient(override val name: String, val amount: Number, val unit: MeasureUnit) : Node(), Named

sealed class Step : Node()

data class Mix(val items: List<String>, val target: String) : Step()
data class Cut(val items: List<String>, val target: String) : Step()
data class Place(val items: List<String>, val target: String) : Step()
data class Heat(val items: List<String>, val target: Number, val unit: TemperatureUnit) : Step()
data class Bake(val items: List<String>, val target: Number, val unit: TimeUnit) : Step()

enum class MeasureUnit { Grams, MilliLiters }
enum class TimeUnit { Second, Minute, Hour }
enum class TemperatureUnit { Celsius, Farenheit }
