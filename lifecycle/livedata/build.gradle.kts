plugins {
    id("com.android.library")
    kotlin("android")
    id("io.gitlab.arturbosch.detekt") version "1.14.2"
    id("org.jetbrains.dokka") version "1.4.20"
    `maven-publish`
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

tasks {

    dokkaJavadoc {
        outputDirectory.set(buildDir.resolve("javadoc"))
        dokkaSourceSets {
            all {
                logger.quiet("Source set: $name")
            }
            named("main") {
                moduleName.set("Kotlin utilities")
                includes.from(files("packages.md"))
            }
        }
    }

    dokkaJekyll {
        outputDirectory.set(buildDir.resolve("jekyll"))
        dokkaSourceSets {
            named("main") {
                moduleName.set("Kotlin utilities")
                includes.from(files("packages.md"))
            }
        }
    }

    val javadocJar = create<Jar>("javadocJar") {
        dependsOn.add(dokkaJavadoc)
        archiveClassifier.set("javadoc")
        from(dokkaJavadoc)
    }

    val sourcesJar = create<Jar>("sourcesJar") {
        archiveClassifier.set("sources")
        from(android.sourceSets.named("main").get().java.srcDirs)
    }

    artifacts {
        archives(javadocJar)
        archives(sourcesJar)
    }
}

afterEvaluate {
    publishing {
        publications {
            register("libAar", MavenPublication::class) {
                from(components["release"])
                artifactId = "${project.parent?.name}-${project.name}"
                groupId = "${project.group}"
                version = "${project.version}"
                artifact(tasks["javadocJar"])
                artifact(tasks["sourcesJar"])
                val libDescription: String by project.rootProject
                val libUrl: String by project.rootProject
                val gitUrl: String by project.rootProject
                pom {
                    name.set("$libDescription: ${project.name.capitalize()}")
                    description.set(libDescription)
                    url.set(libUrl)
                    scm {
                        connection.set("scm:git:$gitUrl.git")
                        developerConnection.set("scm:git:$gitUrl.git")
                        url.set(gitUrl)
                    }
                    licenses {
                        license {
                            name.set("The Apache Software License, Version 2.0")
                            url.set("http://www.apache.org/licenses/LICENSE-2.0")
                        }
                    }
                    developers {
                        developer {
                            id.set("sczerwinski")
                            name.set("Slawomir Czerwinski")
                            email.set("slawomir@czerwinski.it")
                            url.set("https://czerwinski.it/")
                        }
                    }
                    issueManagement {
                        system.set("GitHub Issues")
                        url.set("$gitUrl/issues")
                    }
                    ciManagement {
                        system.set("GitHub Actions")
                        url.set("$gitUrl/actions")
                    }
                }
            }
        }

        repositories {
            maven {
                url = uri("$buildDir/maven")
            }
        }
    }
}
