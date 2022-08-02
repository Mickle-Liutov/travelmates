package libs

import org.gradle.api.artifacts.dsl.DependencyHandler

object AndroidCore {
    private const val CORE_VERSION = "1.7.0"
    private const val APPCOMPAT_VERSION = "1.4.0"
    private const val MATERIAL_VERSION = "1.4.0"
    private const val CONSTRAINTLAYOUT_VERSION = "2.1.2"
    private const val LIFECYCLE_VERSION = "2.4.0"

    private val dependencies = listOf(
        "androidx.core:core-ktx:$CORE_VERSION",
        "androidx.appcompat:appcompat:$APPCOMPAT_VERSION",
        "com.google.android.material:material:$MATERIAL_VERSION",
        "androidx.constraintlayout:constraintlayout:$CONSTRAINTLAYOUT_VERSION",
        "androidx.lifecycle:lifecycle-viewmodel-ktx:$LIFECYCLE_VERSION",
        "androidx.lifecycle:lifecycle-livedata-ktx:$LIFECYCLE_VERSION"
    )


    fun DependencyHandler.androidCore(configurationName: String = "implementation") {
        dependencies.forEach {
            add(configurationName, it)
        }
    }
}