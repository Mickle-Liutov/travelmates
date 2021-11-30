package libs

import org.gradle.api.artifacts.dsl.DependencyHandler

object Desugaring {
    private const val DESUGARING_VERSION = "1.1.5"

    private val dependencies = listOf(
        "com.android.tools:desugar_jdk_libs:$DESUGARING_VERSION"
    )

    fun DependencyHandler.desugaring(configurationName: String = "coreLibraryDesugaring") {
        dependencies.forEach {
            add(configurationName, it)
        }
    }
}