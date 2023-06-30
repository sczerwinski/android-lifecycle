plugins {
    id("com.android.library")
    kotlin("android")
    id("io.gitlab.arturbosch.detekt") version "1.22.0"
    id("org.jetbrains.dokka")
    `maven-publish`
    signing
}

android {

    compileSdk = 33

    namespace = "it.czerwinski.android.lifecycle.livedata.test.junit4"

    defaultConfig {
        minSdk = 14
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
    implementation("junit:junit:4.13.2")

    api(project(":lifecycle:livedata-test-common"))

    api("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.2")
    api("androidx.arch.core:core-testing:2.2.0")
}

detekt {
    config = files("../../config/detekt/detekt.yml")
    buildUponDefaultConfig  = true
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
    tasks {
        getByName("generateMetadataFileForLibAarPublication")
            .dependsOn(getByName("sourcesJar"))
    }
}
