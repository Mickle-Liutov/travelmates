import libs.AndroidCore.androidCore
import libs.AndroidTest.androidTest
import libs.Coil.coil
import libs.Desugaring.desugaring
import libs.Hilt.hilt
import libs.ImagePicker.imagePicker
import libs.Navigation.navigation
import libs.Networking.networking
import libs.Timber.timber
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
        isCoreLibraryDesugaringEnabled = true

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
    implementation(project(":location"))
    implementation(project(":mainapi"))
    implementation(project(":core"))
    implementation(project(":images"))

    androidCore()
    networking()
    hilt()
    navigation()
    timber()
    desugaring()
    coil()
    imagePicker()

    androidTest()
    unitTest()
}