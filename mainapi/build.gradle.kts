import libs.AndroidCore.androidCore
import libs.AndroidTest.androidTest
import libs.Desugaring.desugaring
import libs.Hilt.hilt
import libs.Networking.networking
import libs.Room.room
import libs.Timber.timber
import libs.UnitTest.unitTest

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("kotlin-parcelize")
}

android {
    compileSdk = Config.COMPILE_SDK

    defaultConfig {
        minSdk = Config.MIN_SDK
        targetSdk = Config.TARGET_SDK

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true

        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":authapi"))
    implementation(project(":location"))

    androidCore()
    hilt()
    room()
    timber()
    networking()
    desugaring()
    implementation("com.squareup.okhttp3:logging-interceptor:4.5.0")

    androidTest()
    unitTest()
}