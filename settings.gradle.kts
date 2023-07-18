pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

rootProject.name = "evervault-core"
include("evervault-core")

include(":examples:shared")
include(":examples:android")
