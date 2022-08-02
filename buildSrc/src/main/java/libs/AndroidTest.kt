package libs

import org.gradle.api.artifacts.dsl.DependencyHandler

object AndroidTest {
    private const val ANDROID_JUNIT_VERSION = "1.1.3"
    private const val ESPRESSO_VERSION = "3.4.0"

    private val dependencies = listOf(
        "androidx.test.ext:junit:$ANDROID_JUNIT_VERSION",
        "androidx.test.espresso:espresso-core:$ESPRESSO_VERSION"
    )

    fun DependencyHandler.androidTest(configurationName: String = "androidTestImplementation") {
        dependencies.forEach {
            add(configurationName, it)
        }
    }
}