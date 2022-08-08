val modName: String by project
val minecraftVersion: String by project
val geckolibVersion: String by project

base.archivesName.set("${modName}-common-${minecraftVersion}")

loom {
    @Suppress("UnstableApiUsage")
    splitEnvironmentSourceSets()
}

dependencies {
    modImplementation(group = "software.bernie.geckolib", name = "geckolib-fabric-1.19", version = geckolibVersion)
}

java {
    registerFeature("client") {
        usingSourceSet(sourceSets["client"])
    }
}

// None of this project needs to be processed, it's just here for IDEs to know how to handle stuff properly.
tasks.all {
    enabled = false
}
