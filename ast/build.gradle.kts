plugins {
    id("org.jetbrains.kotlin.jvm") version "1.8.22"
    id("antlr")
}

repositories {
    mavenCentral()
}

dependencies {
    antlr("org.antlr:antlr4:4.13.1")
    implementation("com.strumenta.kolasu:kolasu-core:1.5.34")
    testImplementation(kotlin("test"))
}

tasks.compileKotlin {
    dependsOn("generateGrammarSource")
}

tasks.compileTestKotlin {
    dependsOn("generateTestGrammarSource")
}

tasks.test {
    useJUnitPlatform()
}
