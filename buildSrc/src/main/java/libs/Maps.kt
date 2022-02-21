package libs

import org.gradle.api.artifacts.dsl.DependencyHandler

object Maps {

    private const val MAPS_VERSION = "18.0.2"

    private val dependencies = listOf(
        "com.google.maps.android:maps-ktx:3.2.1"
    )

    fun DependencyHandler.maps(configurationName: String = "implementation") {
        dependencies.forEach {
            add(configurationName, it)
        }
    }
}