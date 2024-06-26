buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.4.2")
    }
}
group = "com.evervault.sdk"
version = "1.2"
plugins {
    kotlin("jvm") version "1.8.21" apply false
    id("org.jetbrains.kotlin.android") version "1.8.20" apply false
    id("io.github.gradle-nexus.publish-plugin") version "1.1.0"
}

allprojects {
    repositories {
        mavenLocal()
        google()
        mavenCentral()
    }
}

val ossrhUsername: String? by project
val ossrhPassword: String? by project

nexusPublishing {
    repositories {
        sonatype {
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))

            username.set(ossrhUsername)
            password.set(ossrhPassword)
        }
    }
}
