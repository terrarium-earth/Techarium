val geckolibVersion: String by project
val resourcefulLibVersion: String by project
val botariumVersion: String by project
val minecraftVersion: String by project

loom {
    @Suppress("UnstableApiUsage")
    splitEnvironmentSourceSets()
}

dependencies {
    compileOnly(group = "net.msrandom", name = "class-extension-annotations", version = "1.0")
    modImplementation(group = "software.bernie.geckolib", name = "geckolib-fabric-1.19", version = geckolibVersion)
    modImplementation(group = "earth.terrarium", name = "botarium-common-$minecraftVersion", version = botariumVersion)
    modImplementation(group = "com.teamresourceful.resourcefullib", name = "resourcefullib-common-1.19.1", version = resourcefulLibVersion)
}

val client: SourceSet by sourceSets.getting

java {
    registerFeature(client.name) {
        usingSourceSet(client)
    }
}
