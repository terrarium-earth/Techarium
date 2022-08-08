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

val commonSource: Directory = rootProject.layout.projectDirectory.dir("Common").dir("src")

sourceSets.main {
    val main = commonSource.dir(SourceSet.MAIN_SOURCE_SET_NAME)

    java.srcDir(main.dir("java"))
    resources.srcDir(main.dir("resources"))
}

sourceSets.named("client") {
    val client = commonSource.dir("client")

    java.srcDir(client.dir("java"))
    resources.srcDir(client.dir("resources"))
}

dependencies {
    modImplementation(group = "net.fabricmc", name = "fabric-loader", version = fabricLoaderVersion)
    modImplementation(group = "net.fabricmc.fabric-api", name = "fabric-api", version = fabricApiVersion)
    modImplementation(group = "software.bernie.geckolib", name = "geckolib-fabric-1.19", version = geckolibVersion)
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
