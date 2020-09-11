import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

application {
    mainClass.set("name.sufitchi.webtaskman_agent.WebTaskManAgent")
}

plugins {
    application
    kotlin("jvm") version "1.4.10"
}

repositories {
    mavenCentral()
}


tasks.withType<KotlinCompile>().configureEach {
    //kotlinOptions.suppressWarnings = true
    kotlinOptions.jvmTarget = "1.8"
}