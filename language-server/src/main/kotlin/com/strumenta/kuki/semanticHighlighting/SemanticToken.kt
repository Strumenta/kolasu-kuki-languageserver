package com.strumenta.kuki.semanticHighlighting

import org.eclipse.lsp4j.Position

data class SemanticToken (val position: Position, val type: SemanticTokenType, val modifiers: List<SemanticTokenModifier>)

enum class SemanticTokenType {
    NAMESPACE,
    CLASS,
    ENUM,
    INTERFACE,
    STRUCT,
    TYPE_PARAMETER,
    TYPE,
    PARAMETER,
    VARIABLE,
    PROPERTY,
    ENUM_MEMBER,
    DECORATOR,
    EVENT,
    FUNCTION,
    METHOD,
    MACRO,
    LABEL,
    COMMENT,
    STRING,
    KEYWORD,
    NUMBER,
    REGULAR_EXPRESSION,
    OPERATOR
}

enum class SemanticTokenModifier {
    DECLARATION,
    DEFINITION,
    READ_ONLY,
    STATIC,
    DEPRECATED,
    ABSTRACT,
    ASYNCHRONOUS,
    MODIFICATION,
    DOCUMENTATION,
    DEFAULT_LIBRARY
}