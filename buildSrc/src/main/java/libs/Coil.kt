package libs

import org.gradle.api.artifacts.dsl.DependencyHandler

object Coil {

    private const val COIL_VERSION = "1.4.0"
    private const val FIREBASE_COIL_VERSION = "0.3.0"

    private val dependencies = listOf(
        "io.coil-kt:coil:$COIL_VERSION",
        "com.github.rosariopfernandes:firecoil:$FIREBASE_COIL_VERSION"
    )

    fun DependencyHandler.coil(configurationName: String = "implementation") {
        dependencies.forEach {
            add(configurationName, it)
        }
    }
}