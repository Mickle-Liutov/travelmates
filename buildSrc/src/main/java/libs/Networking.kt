package libs

import org.gradle.api.artifacts.dsl.DependencyHandler

object Networking {
    private const val NETWORKING_VERSION = "2.9.0"

    private val dependencies = listOf(
        "com.squareup.retrofit2:retrofit:$NETWORKING_VERSION",
        "com.squareup.retrofit2:converter-moshi:$NETWORKING_VERSION"
    )

    fun DependencyHandler.networking(configurationName: String = "implementation") {
        dependencies.forEach {
            add(configurationName, it)
        }
    }
}