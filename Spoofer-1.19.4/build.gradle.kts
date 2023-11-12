plugins {
    id("io.github.thatsmusic99.java-conventions")
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
    paperDevBundle("1.19.4-R0.1-SNAPSHOT")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}