plugins {
    id("org.jetbrains.kotlin.jvm") version "1.8.22"
    id("antlr")
}

repositories {
    mavenCentral()
}

dependencies {
    antlr("org.antlr:antlr4:4.13.1")
    implementation("com.strumenta.kolasu:kolasu-core:1.5.31")
}

tasks.compileKotlin {
    dependsOn("generateGrammarSource")
}
