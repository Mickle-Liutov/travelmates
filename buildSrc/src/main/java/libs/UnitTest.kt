package libs

import org.gradle.api.artifacts.dsl.DependencyHandler

object UnitTest {
    private const val JUNIT_VERSION = "4.13.2"
    private const val MOCKK_VERSION = "1.12.3"
    private const val ARCH_CORE_VERSION = "2.1.0"
    private const val COROUTINES_TEST_VERSION = "1.4.2"

    private val dependencies = listOf(
        "junit:junit:$JUNIT_VERSION",
        "androidx.arch.core:core-testing:$ARCH_CORE_VERSION",
        "io.mockk:mockk:$MOCKK_VERSION",
        "org.jetbrains.kotlinx:kotlinx-coroutines-test:$COROUTINES_TEST_VERSION"
    )

    fun DependencyHandler.unitTest(configurationName: String = "testImplementation") {
        dependencies.forEach {
            add(configurationName, it)
        }
    }
}