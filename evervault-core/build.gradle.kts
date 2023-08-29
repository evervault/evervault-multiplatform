import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization") version "1.8.21"
    id("io.github.ttypic.swiftklib") version "0.2.1"
    id("signing")
    id("convention.publication")
}

val ktorVersion = "2.3.1"

group = "com.evervault.sdk"
version = "1.0"

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation("org.jetbrains.kotlin:kotlin-stdlib-common")

                // ktor
                implementation("io.ktor:ktor-client-core:$ktorVersion")

                // JSON
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")

                implementation("com.google.code.gson:gson:2.8.7")
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("org.mockito.kotlin:mockito-kotlin:5.0.0")
                implementation("com.google.code.gson:gson:2.8.7")
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-okhttp:$ktorVersion")
                implementation("org.bouncycastle:bcprov-jdk15on:1.70")
            }
        }
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "11"
    }
}

tasks.withType<Javadoc> {
    (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
}
