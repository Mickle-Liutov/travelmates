package libs

import org.gradle.api.artifacts.dsl.DependencyHandler

object Aws {

    private const val COGNITO_VERSION = "1.28.1"
    private const val AWS_CORE_VERSION = "0.12.1"

    private val dependencies = listOf(
        "com.amplifyframework:aws-auth-cognito:$COGNITO_VERSION",
        "com.amplifyframework:core-kotlin:$AWS_CORE_VERSION"
    )

    fun DependencyHandler.aws(configurationName: String = "implementation") {
        dependencies.forEach {
            add(configurationName, it)
        }
    }

}