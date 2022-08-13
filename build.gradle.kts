@file:Suppress("UnstableApiUsage")

import net.msrandom.postprocess.PostProcessClasses
import net.fabricmc.loom.util.Constants
import net.fabricmc.loom.api.LoomGradleExtensionAPI

plugins {
    java
    id("jvm-post-processing") version "0.1"
    id("architectury-plugin") version "3.4-SNAPSHOT"
    id("dev.architectury.loom") version "0.12.0-SNAPSHOT" apply false
}

val minecraftVersion: String by project
val authorName: String by project

architectury {
    minecraft = minecraftVersion
}

subprojects {
    apply(plugin = "jvm-post-processing")
    apply(plugin = "dev.architectury.loom")

    val loom = the<LoomGradleExtensionAPI>()

    loom.silentMojangMappingsLicense()

    dependencies {
        Constants.Configurations.MINECRAFT(group = "com.mojang", name = "minecraft", version = minecraftVersion)

        Constants.Configurations.MAPPINGS(
            loom.layered {
                val parchmentVersion: String by project

                officialMojangMappings()
                parchment(create(group = "org.parchmentmc.data", name = "parchment-1.18.2", version = parchmentVersion, ext = "zip"))
            }
        )
    }


    tasks.jar {
        val modName: String by project

        from("LICENSE") {
            rename { "${it}_${modName}" }
        }

        manifest {
            attributes(
                "Specification-Title" to modName,
                "Specification-Vendor" to authorName,
                "Specification-Version" to archiveVersion.get(),
                "Implementation-Title" to name,
                "Implementation-Version" to archiveVersion.get(),
                "Implementation-Vendor" to authorName,
                "Built-On-Java" to "${System.getProperty("java.vm.version")} (${System.getProperty("java.vm.vendor")})",
                "Build-On-Minecraft" to minecraftVersion
            )
        }
    }

    if (name != "Common") {
        sourceSets.main {
            java.destinationDirectory.set(layout.buildDirectory.dir("processedClasses").map { it.dir("java").dir(SourceSet.MAIN_SOURCE_SET_NAME) })
        }

        tasks.compileJava {
            destinationDirectory.set(layout.buildDirectory.dir("classes").map { it.dir("java").dir(SourceSet.MAIN_SOURCE_SET_NAME) })
        }

        tasks.withType<PostProcessClasses> {
            annotationType.convention("com.techarium.techarium.util.extensions.ExtensionFor")
            classesDirectory.convention(tasks.compileJava.flatMap(AbstractCompile::getDestinationDirectory))
            destinationDirectory.convention(sourceSets.main.flatMap { it.java.destinationDirectory })
        }

        // Disables Gradle's custom module metadata from being published to maven. The
        // metadata includes mapped dependencies which are not reasonably consumable by
        // other mod developers.
        tasks.withType<GenerateModuleMetadata> {
            enabled = false
        }
    }
}

allprojects {
    apply(plugin = "architectury-plugin")
    apply(plugin = "maven-publish")

    java {
        toolchain.languageVersion.set(JavaLanguageVersion.of(17))
        withSourcesJar()
    }

    repositories {
        mavenCentral()

        maven(url = "https://maven.parchmentmc.org")
        maven(url = "https://repo.spongepowered.org/repository/maven-public/")
        maven(url = "https://maven.blamejared.com")
        maven(url = "https://dl.cloudsmith.io/public/geckolib3/geckolib/maven/")
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
}
