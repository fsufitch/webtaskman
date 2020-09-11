import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

application {
    mainClass.set("name.sufitchi.webtaskman_agent.WebTaskManAgent")
}

dependencies {
    // https://mvnrepository.com/artifact/com.dorkbox/SystemTray
    implementation("com.dorkbox", "SystemTray", "3.17")

}

plugins {
    application
    kotlin("jvm") version "1.4.10"
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
