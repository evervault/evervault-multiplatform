pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

rootProject.name = "evervault-multiplatform"
includeBuild("convention-plugins")
include("evervault-core")

include(":examples:shared")
include(":examples:android")
