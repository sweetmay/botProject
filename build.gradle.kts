import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    kotlin("jvm") version "1.4.31"
    id ("org.jetbrains.kotlin.plugin.jpa") version "1.4.31"
}


group = "org.sweetmay"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

configurations.all {
    exclude("org.slf4j", "impl.StaticLoggerBinder")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation("junit", "junit", "4.12")
    implementation("org.telegram", "telegrambots", "5.0.1")
    implementation("org.slf4j", "slf4j-log4j12", "1.7.29")
    implementation("org.hibernate", "hibernate-core", "5.4.29.Final")
    implementation ("org.postgresql", "postgresql", "42.2.19")
}
