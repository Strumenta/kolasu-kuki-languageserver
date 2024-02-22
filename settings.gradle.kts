rootProject.name = "kuki"

pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
    }
}

include("ast")
include("language-server")