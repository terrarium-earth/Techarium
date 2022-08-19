import net.msrandom.postprocess.PostProcessClasses

val fabricLoaderVersion: String by project
val fabricApiVersion: String by project
val geckolibVersion: String by project

architectury {
    platformSetupLoomIde()
}

loom {
    @Suppress("UnstableApiUsage")
    splitEnvironmentSourceSets()
}

val processJavaClasses by tasks.registering(PostProcessClasses::class) {
    extensionPackages.add("earth.terrarium.techarium.fabric.extensions")
    dependsOn(tasks.compileJava)
}

val compileClientJava = tasks.named<JavaCompile>("compileClientJava") {
    destinationDirectory.set(layout.buildDirectory.dir("classes").map { it.dir("java").dir("client") })
    dependsOn(processJavaClasses)
}

val processJavaClientClasses by tasks.registering(PostProcessClasses::class) {
    extensionPackages.add("earth.terrarium.techarium.fabric.client.extensions")
    classesDirectory.convention(compileClientJava.flatMap(AbstractCompile::getDestinationDirectory))
    destinationDirectory.convention(sourceSets.named("client").flatMap { it.java.destinationDirectory })

    dependsOn(compileClientJava)
}

sourceSets {
    val commonSourceSets = projects.common.dependencyProject.sourceSets

    main {
        val commonMain = commonSourceSets.main

        java.srcDir(commonMain.map { it.java.srcDirs })
        resources.srcDir(commonMain.map { it.resources.srcDirs })

        compiledBy(processJavaClasses)
    }

    named("client") {
        val commonClient = commonSourceSets.named("client")

        java.destinationDirectory.set(layout.buildDirectory.dir("processedClasses").map { it.dir("java").dir("client") })

        java.srcDir(commonClient.map { it.java.srcDirs })
        resources.srcDir(commonClient.map { it.resources.srcDirs })

        compiledBy(processJavaClientClasses)
    }
}

dependencies {
    modImplementation(group = "net.fabricmc", name = "fabric-loader", version = fabricLoaderVersion)
    modImplementation(group = "net.fabricmc.fabric-api", name = "fabric-api", version = fabricApiVersion)
    modImplementation(group = "software.bernie.geckolib", name = "geckolib-fabric-1.19", version = geckolibVersion)

    compileOnly(projects.common) { isTransitive = false }

    "clientCompileOnly"(projects.common) {
        targetConfiguration = "clientApiElements"
        isTransitive = false
    }
}

tasks.processResources {
    inputs.property("version", version)

    filesMatching("fabric.mod.json") {
        expand(mapOf("version" to version))
    }
}
