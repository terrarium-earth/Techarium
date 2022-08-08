import earth.terrarium.ProcessClasses

val modName: String by project
val minecraftVersion: String by project
val fabricLoaderVersion: String by project
val fabricApiVersion: String by project
val geckolibVersion: String by project

base.archivesName.set("${modName}-fabric-${minecraftVersion}")

architectury {
    platformSetupLoomIde()
    fabric()
}

loom {
    @Suppress("UnstableApiUsage")
    splitEnvironmentSourceSets()
}

sourceSets {
    val commonSourceSets = project(":Common").sourceSets

    val commonMain = commonSourceSets.main.get()
    val commonClient = commonSourceSets["client"]

    main {
        java.srcDirs += commonMain.java.srcDirs
        resources.srcDirs += commonMain.resources.srcDirs
    }

    named("client") {
        java.srcDirs += commonClient.java.srcDirs
        resources.srcDirs += commonClient.resources.srcDirs
    }
}

dependencies {
    modImplementation(group = "net.fabricmc", name = "fabric-loader", version = fabricLoaderVersion)
    modImplementation(group = "net.fabricmc.fabric-api", name = "fabric-api", version = fabricApiVersion)
    modImplementation(group = "software.bernie.geckolib", name = "geckolib-fabric-1.19", version = geckolibVersion)

    compileOnly(project(path = ":Common", configuration = "apiElements"))
    "clientCompileOnly"(project(path = ":Common", configuration = "clientApiElements"))
}

tasks {
    val processJavaClasses by registering(ProcessClasses::class) {
        extensionPackages.add("com.techarium.techarium.fabric.extensions")

        dependsOn(compileJava)
    }

    val processJavaClientClasses by registering(ProcessClasses::class) {
        extensionPackages.add("com.techarium.techarium.fabric.client.extensions")
        classesDirectory.set(named<AbstractCompile>("compileClientJava").flatMap(AbstractCompile::getDestinationDirectory))

        dependsOn("compileClientJava")
    }

    classes {
        dependsOn(processJavaClasses)
    }

    named("clientClasses") {
        dependsOn(processJavaClientClasses)
    }

    processResources {
        inputs.property("version", version)

        filesMatching("fabric.mod.json") {
            expand(mapOf("version" to version))
        }
    }
}
