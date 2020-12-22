plugins {
    id("com.android.library")
    kotlin("android")
    id("de.mannodermaus.android-junit5")
    id("io.gitlab.arturbosch.detekt") version "1.14.2"
    id("org.jetbrains.dokka")
    `maven-publish`
    signing
}

android {

    compileSdkVersion(30)

    defaultConfig {
        minSdkVersion(14)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "${project.version}"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation("org.junit.jupiter:junit-jupiter-api:5.7.0")

    api("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.4.2-native-mt")
    api("androidx.arch.core:core-testing:2.1.0")
    api("androidx.lifecycle:lifecycle-livedata-ktx:2.2.0")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0")
    testImplementation("io.mockk:mockk:1.10.2")
}

tasks {
    dokkaJavadoc { setUpJavadocTask(project) }
    dokkaJekyll { setUpJekyllTask(project) }

    artifacts {
        archives(createJavadocJar(dokkaJavadoc))
        archives(createSourcesJar(android.sourceSets.named("main").get().java.srcDirs))
    }
}

afterEvaluate {
    publishing {
        publications { registerAarPublication(project) }
        repositories { sonatype(project) }
    }
    signing { signAllMavenPublications(project, publishing) }
}
