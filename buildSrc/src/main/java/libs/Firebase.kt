package libs

import org.gradle.api.artifacts.dsl.DependencyHandler

object Firebase {

    private const val FIREBASE_VERSION = "29.1.0"
    private const val KOTLIN_EXTENSIONS = "1.4.1"

    private val dependencies = listOf(
        "com.google.firebase:firebase-analytics-ktx",
        "com.google.firebase:firebase-storage-ktx",
        "org.jetbrains.kotlinx:kotlinx-coroutines-play-services:$KOTLIN_EXTENSIONS"
    )

    fun DependencyHandler.firebase(configurationName: String = "implementation") {
        add("implementation", platform("com.google.firebase:firebase-bom:$FIREBASE_VERSION"))
        dependencies.forEach {
            add(configurationName, it)
        }
    }

}