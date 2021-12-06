package libs

import org.gradle.api.artifacts.dsl.DependencyHandler

object Splash {
    private const val SPLASH_VERSION = "1.0.0-alpha01"

    private val dependencies = listOf(
        "androidx.core:core-splashscreen:$SPLASH_VERSION"
    )

    fun DependencyHandler.splash(configurationName: String = "implementation") {
        dependencies.forEach {
            add(configurationName, it)
        }
    }
}