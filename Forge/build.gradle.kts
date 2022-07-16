import net.fabricmc.loom.util.Constants

plugins {
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

architectury {
    platformSetupLoomIde()
    forge()
}

val modName: String by project
val minecraftVersion: String by project
val forgeVersion: String by project
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

base.archivesName.set("${modName}-forge-${minecraftVersion}")

loom {
    accessWidenerPath.set(project(":Common").loom.accessWidenerPath)

    forge {
        convertAccessWideners.set(true)
        extraAccessWideners.add(loom.accessWidenerPath.get().asFile.name)
    }
}

sourceSets.main {
    resources.srcDir("src/generated/resources")
}

configurations.compileClasspath { extendsFrom(common) }
configurations.runtimeClasspath { extendsFrom(common) }

dependencies {
    forge(group = "net.minecraftforge", name = "forge", version = "${minecraftVersion}-${forgeVersion}")
    modImplementation(group = "software.bernie.geckolib", name = "geckolib-forge-1.19", version = geckolibVersion)

    common(project(path = ":Common", configuration = Constants.Configurations.NAMED_ELEMENTS)) { isTransitive = false }
    shadowCommon(project(path = ":Common", configuration = "transformProductionForge")) { isTransitive = false }
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

        filesMatching("META-INF/mods.toml") {
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
    }

    sourcesJar {
        from(provider { commonSources.map(::zipTree) })
        dependsOn(commonSources)
    }
}
