pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "android-lifecycle"

include(":lifecycle:livedata")
include(":lifecycle:livedata-test-common")
include(":lifecycle:livedata-test-junit4")
include(":lifecycle:livedata-test-junit5")
