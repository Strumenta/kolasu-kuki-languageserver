rootProject.name = "kuki"

include("ast", "language-server")

pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
    }
}
