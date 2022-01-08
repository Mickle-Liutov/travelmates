package libs

import org.gradle.api.artifacts.dsl.DependencyHandler

object Timber {
    private const val TIMBER_VERSION = "5.0.1"

    private val dependencies = listOf(
        "com.jakewharton.timber:timber:$TIMBER_VERSION",
    )

    fun DependencyHandler.timber(configurationName: String = "implementation") {
        dependencies.forEach {
            add(configurationName, it)
        }
    }
}