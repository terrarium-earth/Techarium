val fabricLoaderVersion: String by project
val fabricApiVersion: String by project
val geckolibVersion: String by project
val resourcefulLibVersion: String by project
val botariumVersion: String by project

architectury {
    platformSetupLoomIde()
}

loom {
    @Suppress("UnstableApiUsage")
    splitEnvironmentSourceSets()
}

val client: SourceSet by sourceSets.getting

classExtensions {
    registerForSourceSet(sourceSets.main.get(), "earth.terrarium.techarium.fabric.extensions")
    registerForSourceSet(client, "earth.terrarium.techarium.fabric.client.extensions")
}

sourceSets {
    val commonSourceSets = projects.techariumCommon.dependencyProject.sourceSets

    main {
        val commonMain = commonSourceSets.main

        java.srcDir(commonMain.map { it.java.srcDirs })
        resources.srcDir(commonMain.map { it.resources.srcDirs })
    }

    client.apply {
        val commonClient = commonSourceSets.named(client.name)

        java.destinationDirectory.set(layout.buildDirectory.dir("processedClasses").map { it.dir("java").dir(client.name) })

        java.srcDir(commonClient.map { it.java.srcDirs })
        resources.srcDir(commonClient.map { it.resources.srcDirs })
    }
}

dependencies {
    modImplementation(group = "net.fabricmc", name = "fabric-loader", version = fabricLoaderVersion)
    modImplementation(group = "net.fabricmc.fabric-api", name = "fabric-api", version = fabricApiVersion)
    modImplementation(group = "software.bernie.geckolib", name = "geckolib-fabric-1.19", version = geckolibVersion)
    include(modImplementation(group = "earth.terrarium", name = "botarium-fabric-1.19.2", version = botariumVersion))
    include(modImplementation(group = "com.teamresourceful.resourcefullib", name = "resourcefullib-fabric-1.19.1", version = resourcefulLibVersion))

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
