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
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks.compileKotlin {
    dependsOn("generateGrammarSource")
}

tasks.test {
    useJUnitPlatform()
}

tasks {
    named("compileTestKotlin") {
        dependsOn("generateTestGrammarSource")
    }
}