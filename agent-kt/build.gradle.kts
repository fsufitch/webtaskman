import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

application {
    mainClassName = "name.sufitchi.webtaskman_agent.WebTaskManAgent"
}

dependencies {
    // https://mvnrepository.com/artifact/com.dorkbox/SystemTray
    implementation("com.dorkbox", "SystemTray", "3.17")
    implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-core", "1.3.9")
    implementation("org.jetbrains.kotlinx", "kotlinx-serialization-core", "1.0.0-RC")
}

plugins {
    application
    kotlin("jvm") version "1.4.10"
    kotlin("plugin.serialization") version "1.4.10"
    id("org.openjfx.javafxplugin") version "0.0.9"
}

repositories {
    mavenCentral()
}

javafx {
    version = "14"
    modules( "javafx.controls" )
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = "1.8"
}
