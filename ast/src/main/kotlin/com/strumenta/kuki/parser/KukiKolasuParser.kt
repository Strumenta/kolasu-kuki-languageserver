package com.strumenta.kuki.parser

import KukiLexer
import KukiParser
import KukiParser.*
import com.strumenta.kuki.mapping.KukiParseTreeMapper
import com.strumenta.kuki.ast.Recipe
import com.strumenta.kolasu.model.Source
import com.strumenta.kolasu.parsing.ANTLRTokenFactory
import com.strumenta.kolasu.parsing.KolasuANTLRToken
import com.strumenta.kolasu.parsing.KolasuParser
import com.strumenta.kolasu.validation.Issue
import com.strumenta.kuki.semantics.resolveSymbols
import org.antlr.v4.runtime.CharStream
import org.antlr.v4.runtime.Lexer
import org.antlr.v4.runtime.TokenStream

class KukiKolasuParser : KolasuParser<Recipe, KukiParser, RecipeContext, KolasuANTLRToken>(ANTLRTokenFactory()) {

    override fun createANTLRLexer(charStream: CharStream): Lexer {
        return KukiLexer(charStream)
    }

    override fun createANTLRParser(tokenStream: TokenStream): KukiParser {
        return KukiParser(tokenStream)
    }

    override fun parseTreeToAst(parseTreeRoot: RecipeContext, considerPosition: Boolean, issues: MutableList<Issue>, source: Source?): Recipe? {
        val mapper = KukiParseTreeMapper()
        val astRoot = mapper.transform(parseTreeRoot) as? Recipe
        issues.addAll(mapper.issues)
        return astRoot
    }

    override fun postProcessAst(recipe: Recipe, issues: MutableList<Issue>): Recipe {
        resolveSymbols(recipe, issues)
        return recipe
    }
}
