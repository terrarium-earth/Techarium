import net.msrandom.postprocess.PostProcessClasses

architectury {
    platformSetupLoomIde()
}

val minecraftVersion: String by project
val forgeVersion: String by project
val geckolibVersion: String by project

val processJavaClasses by tasks.registering(PostProcessClasses::class) {
    extensionPackages.addAll(
        "earth.terrarium.techarium.forge.extensions",
        "earth.terrarium.techarium.forge.client.extensions",
    )

    dependsOn(tasks.compileJava)
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
        val commonSourceSets = projects.common.dependencyProject.sourceSets

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

        compiledBy(processJavaClasses)
    }
}

dependencies {
    forge(group = "net.minecraftforge", name = "forge", version = "${minecraftVersion}-${forgeVersion}")
    modImplementation(group = "software.bernie.geckolib", name = "geckolib-forge-1.19", version = geckolibVersion)

    compileOnly(projects.common) { isTransitive = false }

    compileOnly(projects.common) {
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
