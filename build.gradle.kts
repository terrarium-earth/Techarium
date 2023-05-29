@file:Suppress("UnstableApiUsage")

import net.fabricmc.loom.api.LoomGradleExtensionAPI
import net.fabricmc.loom.util.Constants

plugins {
    java
    id("jvm-class-extensions") version "1.0"
    id("architectury-plugin") version "3.4-SNAPSHOT"
    id("dev.architectury.loom") version "0.12.0-SNAPSHOT" apply false
}

val modVersion: String by project
val minecraftVersion: String by project

architectury {
    minecraft = minecraftVersion
}

allprojects {
    apply(plugin = "java")
    apply(plugin = "architectury-plugin")
    apply(plugin = "maven-publish")

    version = "$modVersion-$minecraftVersion"

    java {
        toolchain.languageVersion.set(JavaLanguageVersion.of(17))
        withSourcesJar()
    }

    repositories {
        mavenCentral()
        maven(url = "https://nexus.resourcefulbees.com/repository/maven-public/")
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
}

subprojects {
    apply(plugin = "jvm-class-extensions")
    apply(plugin = "dev.architectury.loom")

    val loom = the<LoomGradleExtensionAPI>()

    loom.silentMojangMappingsLicense()

    repositories {
        maven(url = "https://maven.msrandom.net/repository/root/")
        maven(url = "https://cursemaven.com")
    }

    dependencies {
        Constants.Configurations.MINECRAFT(group = "com.mojang", name = "minecraft", version = minecraftVersion)

        Constants.Configurations.MAPPINGS(
                loom.layered {
                    val parchmentVersion: String by project

                    officialMojangMappings()
                    parchment(create(group = "org.parchmentmc.data", name = "parchment-$minecraftVersion", version = parchmentVersion, ext = "zip"))
                }
        )
    }

    tasks.jar {
        val authorName: String by project

        from("LICENSE") {
            rename { "${it}_${rootProject.name}" }
        }

        manifest {
            attributes(
                    "Specification-Title" to rootProject.name,
                    "Specification-Vendor" to authorName,
                    "Specification-Version" to project.version,
                    "Implementation-Title" to name,
                    "Implementation-Version" to project.version,
                    "Implementation-Vendor" to authorName,
                    "Built-On-Java" to "${System.getProperty("java.vm.version")} (${System.getProperty("java.vm.vendor")})",
                    "Build-On-Minecraft" to minecraftVersion
            )
        }
    }

    // Disables Gradle's custom module metadata from being published to maven. The
    // metadata includes mapped dependencies which are not reasonably consumable by
    // other mod developers.
    tasks.withType<GenerateModuleMetadata> {
        enabled = false
    }
}
