package com.strumenta.kuki.languageserver

import com.strumenta.kuki.parser.KukiKolasuParser
import com.strumenta.kolasu.languageserver.KolasuServer

fun main() {
    val parser = KukiKolasuParser()
    
    val server = KolasuServer(parser, "kuki", listOf("kuki"))
    server.startCommunication()
}