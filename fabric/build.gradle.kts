import net.msrandom.postprocess.MixinClasses

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

val client: SourceSet by sourceSets.getting

val mixinJavaClasses by tasks.registering(MixinClasses::class) {
    configs.from(rootProject.file("compile.mixins.json"))
    classpath.from(tasks.compileJava.map(AbstractCompile::getClasspath))
    dependsOn(tasks.compileJava)
}

val compileClientJava = tasks.named<JavaCompile>(client.compileJavaTaskName) {
    destinationDirectory.set(layout.buildDirectory.dir("classes").map { it.dir("java").dir(client.name) })
    dependsOn(mixinJavaClasses)
}

val mixinClientJavaClasses by tasks.registering(MixinClasses::class) {
    configs.from(rootProject.file("compile-${client.name}.mixins.json"))
    classesDirectory.convention(compileClientJava.flatMap(AbstractCompile::getDestinationDirectory))
    destinationDirectory.convention(client.java.destinationDirectory)

    dependsOn(compileClientJava)
}

sourceSets {
    val commonSourceSets = projects.techariumCommon.dependencyProject.sourceSets

    main {
        val commonMain = commonSourceSets.main

        java.srcDir(commonMain.map { it.java.srcDirs })
        resources.srcDir(commonMain.map { it.resources.srcDirs })

        compiledBy(mixinJavaClasses)
    }

    client.apply {
        val commonClient = commonSourceSets.named(client.name)

        java.destinationDirectory.set(layout.buildDirectory.dir("processedClasses").map { it.dir("java").dir(client.name) })

        java.srcDir(commonClient.map { it.java.srcDirs })
        resources.srcDir(commonClient.map { it.resources.srcDirs })

        compiledBy(mixinClientJavaClasses)
    }
}

dependencies {
    modImplementation(group = "net.fabricmc", name = "fabric-loader", version = fabricLoaderVersion)
    modImplementation(group = "net.fabricmc.fabric-api", name = "fabric-api", version = fabricApiVersion)
    modImplementation(group = "software.bernie.geckolib", name = "geckolib-fabric-1.19", version = geckolibVersion)

    compileOnly(projects.techariumCommon) { isTransitive = false }

    client.compileOnlyConfigurationName(projects.techariumCommon) {
        targetConfiguration = client.apiElementsConfigurationName
        isTransitive = false
    }
}

tasks.processResources {
    inputs.property("version", version)

    filesMatching("fabric.mod.json") {
        expand(mapOf("version" to version))
    }

    client.output.resourcesDir?.let {
        from(it)
    }

    dependsOn(client.processResourcesTaskName)
}

tasks.jar {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}
