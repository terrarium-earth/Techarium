plugins {
    idea
    kotlin("jvm")
    java
}

java.toolchain.languageVersion = JavaLanguageVersion.of(17)

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(libs.ksp)

    implementation(libs.ktpoet)
    implementation(libs.ksppoet)
}