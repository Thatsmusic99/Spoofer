plugins {
    id("java")
}

group = "io.github.thatsmusic99"
version = "unspecified"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.19.3-R0.1-SNAPSHOT")
    compileOnly(project(":Spoofer-API"))
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}