val modName: String by project
val minecraftVersion: String by project
val geckolibVersion: String by project

base.archivesName.set("${modName}-common-${minecraftVersion}")

architectury {
    common("forge", "fabric")
}

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
