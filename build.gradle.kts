plugins {
    kotlin("jvm") version "1.9.21"
    java
    application
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.21")
    implementation("org.jcommander:jcommander:1.83")
    implementation("de.vandermeer:asciitable:0.3.2")
}

repositories {
    mavenCentral()
}

application {
    mainClass = "com.github.elimxim.MainKt"
    applicationName = "cvsort"
}