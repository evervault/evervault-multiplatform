pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

rootProject.name = "evervault-multiplatform"
include("evervault-core")

include(":examples:shared")
include(":examples:android")
