package com.strumenta.kuki.codegenerator

import com.strumenta.kolasu.languageserver.CodeGenerator
import com.strumenta.kuki.ast.*
import java.io.File
import java.net.URI

class KukiCodeGenerator : CodeGenerator<Recipe> {
    override fun generate(tree: Recipe, uri: String) {
        for (amount in listOf(1, 2, 4)) {
            val file = File(URI(uri)).parent + File.separator + "Recipe book" + File.separator + tree.name + File.separator + tree.name + " for $amount.txt"
            val code = generateRecipeFor(amount, tree)

            File(file).parentFile.mkdirs()
            File(file).writeText(code)
        }
    }

    private fun generateRecipeFor(amount: Int, recipe: Recipe): String {
        val multiplier = amount.toFloat() / recipe.yield
        val code =
            """
${recipe.name}

INGREDIENTS for $amount
${recipe.ingredients.joinToString("\n") { "- ${it.declaration.name}${if (it.amount != null) ": ${it.amount!!.toInt() * multiplier}" else ""}${it.unit?.name ?: ""}" }}

UTENSILS
${recipe.utensils.joinToString("\n") { "- ${it.declaration.name}" }}

STEPS
${recipe.steps.joinToString("\n") {
                when (it) {
                    is Creation -> "${recipe.steps.indexOf(it) + 1}. ${it.name.replaceFirstChar { x -> x.uppercase() }} the ${code(it.items)} into ${it.target.name}."
                    is Spatial -> "${recipe.steps.indexOf(it) + 1}. ${it.name.replaceFirstChar { x -> x.uppercase() }} the ${code(it.items)} in ${it.target.reference.name}."
                    is Temperature -> "${recipe.steps.indexOf(it) + 1}. ${it.name.replaceFirstChar { x -> x.uppercase() }} the ${code(it.items)} to ${it.target} ${it.unit.name}."
                    is Temporal -> "${recipe.steps.indexOf(it) + 1}. ${it.name.replaceFirstChar { x -> x.uppercase() }} the ${code(it.items)} for ${it.target} ${it.unit.name}."
                    is Singular -> "${recipe.steps.indexOf(it) + 1}. ${it.name.replaceFirstChar { x -> x.uppercase() }} the ${code(it.items)}"
                }
            }}
""".trimIndent()
        return code
    }

    private fun code(items: List<ItemReference>): String {
        return if (items.size == 1) {
            items[0].reference.name
        } else {
            items.subList(0, items.size - 1).joinToString(", ") { it.reference.name } + " and " + items[items.size - 1].reference.name
        }
    }
}