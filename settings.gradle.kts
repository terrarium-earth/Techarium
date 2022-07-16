pluginManagement {
    repositories {
        gradlePluginPortal()
        maven(url = "https://maven.fabricmc.net/")
        maven(url = "https://files.minecraftforge.net/maven/")
        maven(url = "https://maven.architectury.dev/")
    }
}

rootProject.name = "techarium"
include("Common", "Fabric", "Forge")
