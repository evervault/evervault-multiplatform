import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    id("com.android.library")
}

android {
    namespace = "com.evervault.shared"
    compileSdk = 33

    defaultConfig {
        minSdk = 26
        targetSdk = 33

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

val ktorVersion = "2.3.1"

kotlin {
    android()

    val applePlatforms: List<KotlinNativeTarget> = listOf(

        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
        // swiftklib currently doesn't support below platforms in the latest release but support has been added in a recent pull request so next release should support it
//        macosX64(),
//        macosArm64(),
//        watchosX64(),
//        watchosArm32(),
//        watchosArm64(),
//        watchosSimulatorArm64(),
//        tvosX64(),
//        tvosArm64(),
//        tvosSimulatorArm64()
    )

    applePlatforms.forEach {
        it.binaries.framework {
            baseName = "shared"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":evervault-core"))

            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }


        val platformMain = applePlatforms.map { sourceSets.getByName("${it.name}Main") }
        val platformTest = applePlatforms.map { sourceSets.getByName("${it.name}Test") }

        val darwinMain by creating {
            dependsOn(commonMain)
        }
        val darwinTest by creating {
            dependsOn(commonTest)
        }

        platformMain.forEach {
            it.dependsOn(darwinMain)
        }

        platformTest.forEach {
            it.dependsOn(darwinTest)
        }
    }
}
