import groovy.json.StringEscapeUtils
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    java
    idea
    kotlin("jvm") version "2.0.0"
    id("maven-publish")
    alias(libs.plugins.resourcefulgradle)
    alias(libs.plugins.moddev)
    alias(libs.plugins.ksp)
}

val modId = "techarium"

base {
    archivesName.set(libs.versions.minecraft.map { "$modId-$it" })
}

java.toolchain.languageVersion = JavaLanguageVersion.of(21)

neoForge {
    version = libs.versions.neoforge

    parchment.mappingsVersion = libs.versions.parchment
    parchment.minecraftVersion = libs.versions.minecraft

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
        register(modId) {
            sourceSet(sourceSets.main.get())
        }
    }

    unitTest {
        enable()

        testedMod = mods[modId]
    }
}

repositories {
    maven(url = "https://maven.neoforged.net/releases")
    maven(url = "https://maven.teamresourceful.com/repository/maven-public/")
    mavenLocal()
}

dependencies {
    compileOnly(ksp(project(":annotations"))!!)

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

    testImplementation(libs.junit)
    testImplementation(libs.testframework)
    testRuntimeOnly(libs.junitplatform)
}

java {
    withSourcesJar()
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

tasks.withType<Test> {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = "$modId-${libs.versions.minecraft.get()}"
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
            val version: String by project
            val changelog: String = file("changelog.md").readText(Charsets.UTF_8)
            val link: String? = System.getenv("RELEASE_URL")

            source.set(file("templates/embed.json.template"))
            injectedValues.set(mapOf(
                "minecraft" to libs.versions.minecraft.get(),
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