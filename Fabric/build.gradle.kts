import earth.terrarium.ProcessClasses

val modName: String by project
val minecraftVersion: String by project
val fabricLoaderVersion: String by project
val fabricApiVersion: String by project
val geckolibVersion: String by project

base.archivesName.set("${modName}-fabric-${minecraftVersion}")

architectury {
    platformSetupLoomIde()
}

loom {
    @Suppress("UnstableApiUsage")
    splitEnvironmentSourceSets()
}

sourceSets {
    val commonSourceSets = project(":Common").sourceSets

    main {
        val commonMain = commonSourceSets.main

        java.srcDir(commonMain.map { it.java.srcDirs })
        resources.srcDir(commonMain.map { it.resources.srcDirs })
    }

    named("client") {
        val commonClient = commonSourceSets.named("client")

        java.srcDir(commonClient.map { it.java.srcDirs })
        resources.srcDir(commonClient.map { it.resources.srcDirs })
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
