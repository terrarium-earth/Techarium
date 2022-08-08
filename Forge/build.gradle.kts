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
    val commonSourceSets = project(":Common").sourceSets

    val commonMain = commonSourceSets.main.get()
    val commonClient = commonSourceSets["client"]

    main {
        java.srcDirs += commonMain.java.srcDirs
        java.srcDirs += commonClient.java.srcDirs

        resources.srcDirs += commonMain.resources.srcDirs
        resources.srcDirs += commonClient.resources.srcDirs

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
