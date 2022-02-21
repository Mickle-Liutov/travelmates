package libs

import org.gradle.api.artifacts.dsl.DependencyHandler

object Maps {

    private const val MAPS_VERSION = "3.2.1"

    private val dependencies = listOf(
        "com.google.maps.android:maps-ktx:$MAPS_VERSION"
    )

    fun DependencyHandler.maps(configurationName: String = "implementation") {
        dependencies.forEach {
            add(configurationName, it)
        }
    }
}