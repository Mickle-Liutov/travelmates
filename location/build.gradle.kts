import libs.AndroidCore.androidCore
import libs.AndroidTest.androidTest
import libs.Coil.coil
import libs.Hilt.hilt
import libs.Maps.maps
import libs.Navigation.navigation
import libs.Networking.networking
import libs.UnitTest.unitTest

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    compileSdk = Config.COMPILE_SDK

    defaultConfig {
        minSdk = Config.MIN_SDK
        targetSdk = Config.TARGET_SDK

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
}

dependencies {
    implementation(project(":core"))

    androidCore()
    networking()
    hilt()
    navigation()
    maps()
    coil()

    androidTest()
    unitTest()
}