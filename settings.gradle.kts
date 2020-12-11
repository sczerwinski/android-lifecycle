pluginManagement {
    repositories {
        google()
        jcenter()
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "android-lifecycle"

include(":lifecycle:livedata")
include(":lifecycle:livedata-test-junit5")
