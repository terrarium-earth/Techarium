plugins {
    kotlin("jvm") version "1.6.+"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(gradleApi())
    implementation(group = "org.ow2.asm", name = "asm-tree", version = "9.3")
}
