import earth.terrarium.ProcessClasses

architectury {
    platformSetupLoomIde()
}

val modName: String by project
val minecraftVersion: String by project
val forgeVersion: String by project
val geckolibVersion: String by project

base.archivesName.set("${modName}-forge-${minecraftVersion}")

sourceSets {
    main {
        val commonSourceSets = project(":Common").sourceSets

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

    compileOnly(project(path = ":Common", configuration = "apiElements"))
    compileOnly(project(path = ":Common", configuration = "clientApiElements"))
}

tasks {
    val processJavaClasses by registering(ProcessClasses::class) {
        extensionPackages.addAll(
            "com.techarium.techarium.forge.extensions",
            "com.techarium.techarium.forge.client.extensions",
        )

        dependsOn(compileJava)
    }

    classes {
        dependsOn(processJavaClasses)
    }

    processResources {
        inputs.property("version", version)

        filesMatching("META-INF/mods.toml") {
            expand(mapOf("version" to version))
        }
    }
}
