@file:Suppress("UnstableApiUsage")

import net.fabricmc.loom.util.Constants
import net.fabricmc.loom.api.LoomGradleExtensionAPI

plugins {
    java
    id("architectury-plugin") version "3.4-SNAPSHOT"
    id("dev.architectury.loom") version "0.12.0-SNAPSHOT" apply false
}

val minecraftVersion: String by project
val authorName: String by project

architectury {
    minecraft = minecraftVersion
}

subprojects {
    apply(plugin = "dev.architectury.loom")

    val loom = the<LoomGradleExtensionAPI>()

    loom.silentMojangMappingsLicense()

    dependencies {
        Constants.Configurations.MINECRAFT(group = "com.mojan", name = "minecraft", version = minecraftVersion)

        Constants.Configurations.MAPPINGS(
            loom.layered {
                val parchmentVersion: String by project

                officialMojangMappings()
                parchment(create(group = "org.parchmentmc.data", name = "parchment-1.18.2", version = parchmentVersion, ext = "zip"))
            }
        )
    }

    tasks.jar {
        manifest {
            val modName: String by project

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

    // Disables Gradle's custom module metadata from being published to maven. The
    // metadata includes mapped dependencies which are not reasonably consumable by
    // other mod developers.
    tasks.withType<GenerateModuleMetadata> {
        enabled = false
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
