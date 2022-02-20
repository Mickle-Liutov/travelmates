package libs

import org.gradle.api.artifacts.dsl.DependencyHandler

object Navigation {
    private const val NAVIGATION_VERSION = "2.4.1"

    private val dependencies = listOf(
        "androidx.navigation:navigation-fragment-ktx:$NAVIGATION_VERSION",
        "androidx.navigation:navigation-ui-ktx:$NAVIGATION_VERSION"
    )

    fun DependencyHandler.navigation(configurationName: String = "implementation") {
        dependencies.forEach {
            add(configurationName, it)
        }
    }
}