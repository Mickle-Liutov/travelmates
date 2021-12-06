import libs.AndroidCore.androidCore
import libs.AndroidTest.androidTest
import libs.UnitTest.unitTest

plugins {
    id("com.android.library")
    id("kotlin-android")
}

android {
    compileSdk = Config.COMPILE_SDK

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    androidCore()

    androidTest()
    unitTest()
}