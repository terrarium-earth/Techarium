import net.fabricmc.loom.util.Constants

plugins {
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

val modName: String by project
val minecraftVersion: String by project
val fabricLoaderVersion: String by project
val fabricApiVersion: String by project
val geckolibVersion: String by project

val common: Configuration by configurations.creating {
    isCanBeConsumed = false
    isCanBeResolved = false
}

val shadowCommon: Configuration by configurations.creating {
    isCanBeConsumed = false
}

val commonSources: Configuration by configurations.creating {
    isCanBeConsumed = false
}

base.archivesName.set("${modName}-fabric-${minecraftVersion}")

architectury {
    platformSetupLoomIde()
    fabric()
}

loom {
    accessWidenerPath.set(file("src/main/resources/techarium_fabric.accesswidener"))

    @Suppress("UnstableApiUsage")
    splitEnvironmentSourceSets()
}

configurations.compileClasspath { extendsFrom(common) }
configurations.runtimeClasspath { extendsFrom(common) }

dependencies {
    modImplementation(group = "net.fabricmc", name = "fabric-loader", version = fabricLoaderVersion)
    modImplementation(group = "net.fabricmc.fabric-api", name = "fabric-api", version = fabricApiVersion)
    modImplementation(group = "software.bernie.geckolib", name = "geckolib-fabric-1.19", version = geckolibVersion)

    common(project(path = ":Common", configuration = Constants.Configurations.NAMED_ELEMENTS)) { isTransitive = false }
    shadowCommon(project(path = ":Common", configuration = "transformProductionFabric")) { isTransitive = false }
    commonSources(project(path = ":Common", configuration = JavaPlugin.SOURCES_ELEMENTS_CONFIGURATION_NAME))
}

components.getByName("java") {
    this as AdhocComponentWithVariants

    withVariantsFromConfiguration(configurations.shadowRuntimeElements.get()) {
        skip()
    }
}

tasks {
    processResources {
        inputs.property("version", version)

        filesMatching("fabric.mod.json") {
            expand(mapOf("version" to version))
        }
    }

    shadowJar {
        configurations = listOf(shadowCommon)
        archiveClassifier.set("dev-shadow")
    }

    remapJar {
        inputFile.set(shadowJar.flatMap(Jar::getArchiveFile))
    }

    jar {
        archiveClassifier.set("dev")

        from("LICENSE") {
            rename { "${it}_${modName}" }
        }
    }

    sourcesJar {
        from(provider { commonSources.map(::zipTree) })
        dependsOn(commonSources)
    }
}
