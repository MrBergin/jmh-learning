import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.30"
    id("me.champeau.gradle.jmh") version "0.5.3"
}

group = "mr.bergin"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("io.arrow-kt:arrow-core:0.11.0")
    implementation("io.projectreactor:reactor-core:3.4.3")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

jmh {
    fork = 1
    warmupIterations = 1
    iterations = 1
}