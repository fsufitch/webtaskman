import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

application {
    applicationName = "webtaskman-agent"
    mainClassName = "name.sufitchi.webtaskman_agent.WebTaskManAgent"
}

dependencies {
    // https://mvnrepository.com/artifact/com.dorkbox/SystemTray
    implementation("com.dorkbox", "SystemTray", "3.17")
    implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-core", "1.3.9")
    implementation("org.jetbrains.kotlinx", "kotlinx-serialization-core", "1.0.0-RC")
    implementation("commons-cli", "commons-cli", "1.4")
    implementation("org.jetbrains.kotlin", "kotlin-stdlib-jdk8", "1.4.10")
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
    kotlinOptions.jvmTarget = "11"
}
