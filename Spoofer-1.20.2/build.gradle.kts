plugins {
    id("io.github.thatsmusic99.java-conventions")
    // id("xyz.jpenilla.run-paper") version "2.1.0"
    id("io.papermc.paperweight.userdev") version "1.3.8"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "io.github.thatsmusic99"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":Spoofer-Core"))
    implementation(project(":Spoofer-API"))
    paperDevBundle("1.20.2-R0.1-SNAPSHOT")
}

tasks {
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}