plugins {
    id("java")
    id("maven-publish")
    id("io.github.thatsmusic99.java-conventions")
}

group = "io.github.thatsmusic99"
version = "1.0.0-ALPHA.1"
description = "Spoofer-API"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.19.3-R0.1-SNAPSHOT")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}