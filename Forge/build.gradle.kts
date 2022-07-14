import net.fabricmc.loom.util.Constants

plugins {
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

architectury {
    platformSetupLoomIde()
    forge()
}

val mod_name: String by project
val minecraft_version: String by project
val forge_version: String by project
val geckolib_forge_version: String by project

base.archivesName.set("${mod_name}-forge-${minecraft_version}")

val common: Configuration by configurations.creating {
    isCanBeConsumed = false
    isCanBeResolved = false
}

val shadowCommon: Configuration by configurations.creating {
    isCanBeConsumed = false
}

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

configurations.compileClasspath {
    extendsFrom(common)
}

configurations.runtimeClasspath {
    extendsFrom(common)
}

dependencies {
    forge(group = "net.minecraftforge", name = "forge", version = "${minecraft_version}-${forge_version}")
    modImplementation(group = "software.bernie.geckolib", name = "geckolib-forge-1.19", version = geckolib_forge_version)

    common(project(path = ":Common", configuration = Constants.Configurations.NAMED_ELEMENTS)) { isTransitive = false }
    shadowCommon(project(path = ":Common", configuration = "transformProductionForge")) { isTransitive = false }
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
        dependsOn(shadowJar)
    }

    jar {
        archiveClassifier.set("dev")
    }
}
