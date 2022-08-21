import net.msrandom.postprocess.MixinClasses

architectury {
    platformSetupLoomIde()
}

val minecraftVersion: String by project
val forgeVersion: String by project
val geckolibVersion: String by project

val mixinJavaClasses by tasks.registering(MixinClasses::class) {
    configs.from(rootProject.file("compile.mixins.json"), rootProject.file("compile-client.mixins.json"))
    classpath.from(tasks.compileJava.map(AbstractCompile::getClasspath))

    dependsOn(tasks.compileJava)
}

loom {
    forge {
        dataGen {
            mod("techarium")
        }
    }
}

sourceSets {
    main {
        val commonSourceSets = projects.techariumCommon.dependencyProject.sourceSets

        val commonMain = commonSourceSets.main
        val commonClient = commonSourceSets.named("client")

        java.srcDirs(
            commonMain.map { it.java.srcDirs },
            commonClient.map { it.java.srcDirs },
        )

        resources.srcDirs(
            commonMain.map { it.resources.srcDirs },
            commonClient.map { it.resources.srcDirs },
        )

        // resources.srcDir("src/generated/resources")

        compiledBy(mixinJavaClasses)
    }
}

dependencies {
    forge(group = "net.minecraftforge", name = "forge", version = "${minecraftVersion}-${forgeVersion}")
    modImplementation(group = "software.bernie.geckolib", name = "geckolib-forge-1.19", version = geckolibVersion)

    compileOnly(projects.techariumCommon) { isTransitive = false }

    compileOnly(projects.techariumCommon) {
        targetConfiguration = "clientApiElements"
        isTransitive = false
    }
}

tasks.processResources {
    inputs.property("version", version)

    filesMatching("META-INF/mods.toml") {
        expand(mapOf("version" to version))
    }
}
