import net.msrandom.extensions.PostProcessClasses

architectury {
    platformSetupLoomIde()
}

val minecraftVersion: String by project
val forgeVersion: String by project
val geckolibVersion: String by project

classExtensions {
    registerForSourceSet(sourceSets.main.get(), "earth.terrarium.techarium.forge.extensions", "earth.terrarium.techarium.forge.client.extensions")
}

loom {
    forge {
        dataGen {
            mod("techarium")
        }
    }
}

sourceSets {
    main {
        val commonSourceSets = projects.techariumCommon.dependencyProject.sourceSets

        val commonMain = commonSourceSets.main
        val commonClient = commonSourceSets.named("client")

        java.srcDirs(
            commonMain.map { it.java.srcDirs },
            commonClient.map { it.java.srcDirs },
        )

        resources.srcDirs(
            commonMain.map { it.resources.srcDirs },
            commonClient.map { it.resources.srcDirs },
        )

        resources.srcDir("src/generated/resources")
    }
}

dependencies {
    forge(group = "net.minecraftforge", name = "forge", version = "${minecraftVersion}-${forgeVersion}")
    modImplementation(group = "software.bernie.geckolib", name = "geckolib-forge-1.19", version = geckolibVersion)

    compileOnly(projects.techariumCommon) { isTransitive = false }

    compileOnly(projects.techariumCommon) {
        targetConfiguration = "clientApiElements"
        isTransitive = false
    }
}

tasks.processResources {
    inputs.property("version", version)

    filesMatching("META-INF/mods.toml") {
        expand(mapOf("version" to version))
    }

    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}

tasks.sourcesJar {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}
