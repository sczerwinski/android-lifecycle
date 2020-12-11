buildscript {

    repositories {
        google()
        jcenter()
        gradlePluginPortal()
        mavenCentral()
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.21")
        classpath("com.android.tools.build:gradle:4.1.1")
    }
}

allprojects {

    val libGroupId: String by project
    val libVersion: String by project

    group = libGroupId
    version = libVersion

    repositories {
        google()
        jcenter()
        mavenCentral()
    }
}

plugins {
    id("org.jetbrains.changelog") version "0.6.2"
}
