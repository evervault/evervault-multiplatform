buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.4.2")
    }
}

plugins {
    kotlin("jvm") version "1.8.21" apply false
    id("org.jetbrains.kotlin.android") version "1.8.20" apply false
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}
