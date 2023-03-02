import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version Versions.kotlin
    id("com.github.johnrengelman.shadow") version Versions.shadow
}

group = "com.zamolski.rainfallmeasurer"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}
val ktorVersion: String by project

dependencies {
    implementation(platform("com.google.cloud:libraries-bom:${Versions.googleCloud}"))
    implementation(platform("io.ktor:ktor-bom:${Versions.ktor}"))
    implementation("com.google.cloud:google-cloud-storage")
    implementation("io.ktor:ktor-client-core")
    implementation("io.ktor:ktor-client-cio")
    compileOnly("com.google.cloud.functions:functions-framework-api:${Versions.googleFunctions}")
    implementation("org.slf4j:slf4j-api:${Versions.slf4j}")
    implementation("org.slf4j:slf4j-simple:${Versions.slf4j}")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = Versions.jvm
}

tasks.withType<ShadowJar> {
    mergeServiceFiles()
}