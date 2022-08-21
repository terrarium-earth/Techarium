enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "techarium"

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven(url = "https://maven.fabricmc.net/")
        maven(url = "https://files.minecraftforge.net/maven/")
        maven(url = "https://maven.architectury.dev/")
        maven(url = "https://maven.msrandom.net/repository/root/")
    }
}

includeNamed("common", "fabric", "forge")

fun includeNamed(vararg projectPaths: String) {
    include(*projectPaths)

    for (path in projectPaths) {
        val project = project(":$path")
        project.name = "${project.parent?.name}-$path"
    }
}
