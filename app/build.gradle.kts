import libs.AndroidCore.androidCore
import libs.AndroidTest.androidTest
import libs.Aws.aws
import libs.Desugaring.desugaring
import libs.Hilt.hilt
import libs.Navigation.navigation
import libs.Networking.networking
import libs.Splash.splash
import libs.Timber.timber
import libs.UnitTest.unitTest

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs.kotlin")
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
    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
}

dependencies {
    implementation(project(":authapi"))
    implementation(project(":auth"))
    implementation(project(":core"))
    implementation(project(":mainapi"))

    androidCore()
    aws()
    desugaring()
    hilt()
    splash()
    navigation()
    networking()
    timber()

    androidTest()
    unitTest()
}