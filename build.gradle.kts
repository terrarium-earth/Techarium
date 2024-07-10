import groovy.json.StringEscapeUtils
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    java
    idea
    kotlin("jvm") version "2.0.0"
    id("maven-publish")
    alias(libs.plugins.resourcefulgradle)
    alias(libs.plugins.moddev)
}

val minecraftVersion: String by project
val modId = "techarium"

base {
    archivesName.set("$modId-$minecraftVersion")
}

java.toolchain.languageVersion = JavaLanguageVersion.of(21)

neoForge {
    val minecraftVersion: String by project
    val neoforgeVersion: String by project
    val parchmentVersion: String by project

    version = neoforgeVersion

    parchment.mappingsVersion = parchmentVersion
    parchment.minecraftVersion = minecraftVersion

    runs {
        register("client") {
            client()
        }
        register("server") {
            server()
            programArgument("--nogui")
        }
    }

    mods {
        register("techarium") {
            sourceSet(sourceSets.main.get())
        }
    }
}

repositories {
    maven(url = "https://maven.neoforged.net/releases")
    maven(url = "https://maven.teamresourceful.com/repository/maven-public/")
    mavenLocal()
}

dependencies {
    implementation(libs.resourcefulconfig)

    implementation(libs.resourcefullib)
    compileOnly(libs.bytecodecs)
    compileOnly(libs.yabn)

    implementation(libs.kotlinforforge)
    implementation(libs.geckolib)

    implementation(libs.resourcefullibkt) { isTransitive = false }
    implementation(libs.resourcefulconfigkt) { isTransitive = false }

    "jarJar"(libs.resourcefullibkt)
    "jarJar"(libs.resourcefulconfigkt)
}

java {
    withSourcesJar()
}

tasks.jar {
    archiveClassifier.set("dev")
}

tasks.processResources {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    filesMatching(listOf("META-INF/neoforge.mods.toml")) {
        expand("version" to project.version)
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_21)
        freeCompilerArgs.add("-Xjvm-default=all")
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = "$modId-$minecraftVersion"
            from(components["java"])

            pom {
                name.set("Techarium")
                url.set("https://github.com/terrarium-earth/$modId")

                scm {
                    connection.set("git:https://github.com/terrarium-earth/$modId.git")
                    developerConnection.set("git:https://github.com/terrarium-earth/$modId.git")
                    url.set("https://github.com/terrarium-earth/$modId")
                }
            }
        }
    }
    repositories {
        maven {
            setUrl("https://maven.teamresourceful.com/repository/terrarium/")
            credentials {
                username = System.getenv("MAVEN_USER")
                password = System.getenv("MAVEN_PASS")
            }
        }
    }
}

resourcefulGradle {
    templates {
        register("embed") {
            val minecraftVersion: String by project
            val version: String by project
            val changelog: String = file("changelog.md").readText(Charsets.UTF_8)
            val link: String? = System.getenv("RELEASE_URL")

            source.set(file("templates/embed.json.template"))
            injectedValues.set(mapOf(
                "minecraft" to minecraftVersion,
                "version" to version,
                "changelog" to StringEscapeUtils.escapeJava(changelog),
                "link" to link
            ))
        }
    }
}

idea {
    module {
        isDownloadJavadoc = true
        isDownloadSources = true

        excludeDirs.add(file("run"))
    }
}