architectury {
    forge()
}

loom {
    forge {
        mixinConfig("techarium-common.mixins.json")
        mixinConfig("techarium.mixins.json")
    }
    runs {
        create("data") {
            data()
            programArgs("--all", "--mod", "techarium")
            programArgs("--output", project(":common").file("src/main/generated/resources").absolutePath)
            programArgs("--existing", project(":common").file("src/main/resources").absolutePath)
        }
    }
}

val common: Configuration by configurations.creating {
    configurations.compileClasspath.get().extendsFrom(this)
    configurations.runtimeClasspath.get().extendsFrom(this)
    configurations["developmentForge"].extendsFrom(this)
}

dependencies {
    common(project(":common", configuration = "namedElements")) {
        isTransitive = false
    }
    shadowCommon(project(path = ":common", configuration = "transformProductionForge")) {
        isTransitive = false
    }

    val minecraftVersion: String by project
    val forgeVersion: String by project
    val reiVersion: String by project

    forge(group = "net.minecraftforge", name = "forge", version = "$minecraftVersion-$forgeVersion")
    modCompileOnly(group = "me.shedaniel", name = "RoughlyEnoughItems-api-forge", version = reiVersion)
    modLocalRuntime(group = "me.shedaniel", name = "RoughlyEnoughItems-forge", version = reiVersion)

    modLocalRuntime(group = "maven.modrinth", name = "jade", version = "2iRQrBk4")

    modLocalRuntime(group = "maven.modrinth", name = "mekanism", version = "10.4.2.16")

    forgeRuntimeLibrary("com.teamresourceful:yabn:1.0.3")
    forgeRuntimeLibrary("com.teamresourceful:bytecodecs:1.0.2")
}
