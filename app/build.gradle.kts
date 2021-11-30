import libs.AndroidCore.androidCore
import libs.AndroidTest.androidTest
import libs.Aws.aws
import libs.Desugaring.desugaring
import libs.UnitTest.unitTest

plugins {
    id("com.android.application")
    id("kotlin-android")
}

android {
    compileSdk = Config.COMPILE_SDK

    defaultConfig {
        applicationId = "cz.cvut.fit.travelmates"
        minSdk = Config.MIN_SDK
        targetSdk = Config.TARGET_SDK
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
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
    androidCore()
    aws()
    desugaring()

    androidTest()
    unitTest()
}