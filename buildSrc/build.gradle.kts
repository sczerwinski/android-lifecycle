plugins {
    `kotlin-dsl`
}

buildscript {

    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.20")
    }
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation("com.android.tools.build:gradle:7.4.2")
    implementation("com.android.tools.build:gradle-api:7.4.2")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.6.20")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.20")
    implementation("org.jetbrains.dokka:dokka-core:1.6.10")
    implementation("org.jetbrains.dokka:dokka-gradle-plugin:1.6.10")
}
