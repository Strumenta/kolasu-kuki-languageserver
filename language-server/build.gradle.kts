import java.nio.file.Paths
import java.nio.file.Files

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.8.22"
    id("com.strumenta.kolasu.language-server-plugin") version "1.0.5"
}

dependencies {
    testImplementation(kotlin("test"))
}

languageServer {
    packageDefinitionPath = Paths.get(projectDir.toString(), "src", "main", "resources", "package.json")
	 editor = "codium"
}

tasks.register("copyGrammarFile") {
    doLast {
        val sourcePath = Paths.get(projectDir.toString(), "src", "main", "resources", "syntax.json")
        val destinationPath = Paths.get(projectDir.toString(), "build", "vscode", "syntax.json")

        Files.createDirectories(destinationPath.parent)
        if (!Files.exists(destinationPath)) {
            Files.copy(sourcePath, destinationPath)
        }
    }
}

tasks.named("createVscodeExtension") {
    dependsOn("copyGrammarFile")
}