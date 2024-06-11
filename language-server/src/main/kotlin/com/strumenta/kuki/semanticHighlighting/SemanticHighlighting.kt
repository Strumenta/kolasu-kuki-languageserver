package com.strumenta.kuki.semanticHighlighting

import com.strumenta.kolasu.model.Position
import org.eclipse.lsp4j.SemanticTokens

data class SemanticToken(val position: Position, val type: SemanticTokenType, val modifiers: List<SemanticTokenModifier>)

fun encode(tokens: List<SemanticToken>): SemanticTokens {
    var lastLine = 1 // ? To offset that Kolasu lines start at 1
    var lastColumn = 0
    val data = mutableListOf<Int>()
    for (token in tokens) {
        data.addAll(listOf(
            token.position.start.line - lastLine,
            if (token.position.start.line == lastLine) token.position.start.column - lastColumn else token.position.start.column,
            token.position.end.column - token.position.start.column, // ! assumes tokens are in a single line
            token.type.ordinal,
            token.modifiers.sumOf { it.bit }
        ))
        lastLine = token.position.start.line
        lastColumn = token.position.start.column
    }
    return SemanticTokens(data)
}

enum class SemanticTokenType (val legendName: String) {
    NAMESPACE("namespace"),
    CLASS("class"),
    ENUM("enum"),
    INTERFACE("interface"),
    STRUCT("struct"),
    TYPE_PARAMETER("typeParameter"),
    TYPE("type"),
    PARAMETER("parameter"),
    VARIABLE("variable"),
    PROPERTY("property"),
    ENUM_MEMBER("enumMember"),
    DECORATOR("decorator"),
    EVENT("event"),
    FUNCTION("function"),
    METHOD("method"),
    MACRO("macro"),
    LABEL("label"),
    COMMENT("comment"),
    STRING("string"),
    KEYWORD("keyword"),
    NUMBER("number"),
    REGULAR_EXPRESSION("regexp"),
    OPERATOR("operator")
}

enum class SemanticTokenModifier(val legendName: String, val bit: Int) {
    DECLARATION("declaration", 1),
    DEFINITION("definition", 2),
    READ_ONLY("readonly", 4),
    STATIC("static", 8),
    DEPRECATED("deprecated", 16),
    ABSTRACT("abstract", 32),
    ASYNCHRONOUS("async", 64),
    MODIFICATION("modification", 128),
    DOCUMENTATION("documentation", 256),
    DEFAULT_LIBRARY("defaultLibrary", 512)
}