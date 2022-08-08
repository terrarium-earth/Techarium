import earth.terrarium.ProcessClasses

architectury {
    platformSetupLoomIde()
    forge()
}

val modName: String by project
val minecraftVersion: String by project
val forgeVersion: String by project
val geckolibVersion: String by project

base.archivesName.set("${modName}-forge-${minecraftVersion}")

sourceSets.main {
    val source = rootProject.layout.projectDirectory.dir("Common").dir("src")
    val main = source.dir(SourceSet.MAIN_SOURCE_SET_NAME)
    val client = source.dir("client")

    java.srcDir(main.dir("java"))
    java.srcDir(client.dir("java"))

    resources.srcDir("src/generated/resources")
    resources.srcDir(main.dir("resources"))
    resources.srcDir(client.dir("resources"))
}

dependencies {
    forge(group = "net.minecraftforge", name = "forge", version = "${minecraftVersion}-${forgeVersion}")
    modImplementation(group = "software.bernie.geckolib", name = "geckolib-forge-1.19", version = geckolibVersion)
}

tasks {
    val processJavaClasses by registering(ProcessClasses::class) {
        extensionPackages.addAll(
            "com.techarium.techarium.forge.extensions",
            "com.techarium.techarium.forge.client.extensions",
        )

        dependsOn(compileJava)
    }

    classes {
        dependsOn(processJavaClasses)
    }

    processResources {
        inputs.property("version", version)

        filesMatching("META-INF/mods.toml") {
            expand(mapOf("version" to version))
        }
    }
}
