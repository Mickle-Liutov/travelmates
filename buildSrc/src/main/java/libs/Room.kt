package libs

import org.gradle.api.artifacts.dsl.DependencyHandler

object Room {
    private const val ROOM_VERSION = "2.3.0"

    private val dependencies = listOf(
        "androidx.room:room-runtime:$ROOM_VERSION",
        "androidx.room:room-ktx:$ROOM_VERSION"
    )

    fun DependencyHandler.room(configurationName: String = "implementation") {
        dependencies.forEach {
            add(configurationName, it)
        }
        add("kapt", "androidx.room:room-compiler:$ROOM_VERSION")
    }
}