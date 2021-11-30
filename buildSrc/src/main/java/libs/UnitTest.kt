package libs

import org.gradle.api.artifacts.dsl.DependencyHandler

object UnitTest {
    private const val JUNIT_VERSION = "4.13.2"

    private val dependencies = listOf(
        "junit:junit:$JUNIT_VERSION"
    )


    fun DependencyHandler.unitTest(configurationName: String = "testImplementation") {
        dependencies.forEach {
            add(configurationName, it)
        }
    }
}