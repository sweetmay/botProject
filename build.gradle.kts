plugins {
    java
    kotlin("jvm") version "1.4.30"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.4.30"
}


group = "org.sweetmay"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

configurations.all {
    exclude("org.slf4j", "impl.StaticLoggerBinder")
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation("junit", "junit", "4.12")
    implementation("org.telegram", "telegrambots", "5.0.1")
    implementation("org.slf4j", "slf4j-log4j12", "1.7.29")
    implementation("io.reactivex.rxjava3:rxjava:3.0.11-RC5")
    implementation("org.hibernate", "hibernate-core", "5.4.29.Final")
    implementation ("com.google.code.gson", "gson" , "2.8.6")
    implementation ("org.postgresql", "postgresql", "42.2.19")
}
