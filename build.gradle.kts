import groovy.json.StringEscapeUtils
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    java
    idea
    kotlin("jvm") version "2.0.0"
    id("maven-publish")
    id("com.teamresourceful.resourcefulgradle") version "0.0.+"
    id("net.neoforged.moddev") version "0.1.126"
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
    val neoforgeVersion: String by project
    val minecraftVersion: String by project

    val resourcefulConfigVersion: String by project
    val resourcefulConfigKtVersion: String by project
    val resourcefulLibVersion: String by project
    val resourcefulLibKtVersion: String by project
    val kotlinForForgeVersion: String by project

    implementation("com.teamresourceful.resourcefulconfig:resourcefulconfig-neoforge-${minecraftVersion}:${resourcefulConfigVersion}")
    implementation("com.teamresourceful.resourcefullib:resourcefullib-neoforge-${minecraftVersion}:${resourcefulLibVersion}")
    compileOnly("com.teamresourceful:bytecodecs:1.1.0")
    implementation("thedarkcolour:kotlinforforge-neoforge:${kotlinForForgeVersion}")

    val rlibKt = implementation("com.teamresourceful.resourcefullibkt:resourcefullibkt-neoforge-${minecraftVersion}:${resourcefulLibKtVersion}") {
        isTransitive = false
    }
    val rconfigKt = implementation("com.teamresourceful.resourcefulconfigkt:resourcefulconfigkt-neoforge-${minecraftVersion}:${resourcefulConfigKtVersion}") {
        isTransitive = false
    }

    "jarJar"(rlibKt)
    "jarJar"(rconfigKt)
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