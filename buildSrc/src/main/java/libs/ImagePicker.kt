package libs

import org.gradle.api.artifacts.dsl.DependencyHandler

object ImagePicker {
    private const val IMAGE_PICKER_VERSION = "2.1"

    private val dependencies = listOf(
        "com.github.dhaval2404:imagepicker:$IMAGE_PICKER_VERSION"
    )

    fun DependencyHandler.imagePicker(configurationName: String = "implementation") {
        dependencies.forEach {
            add(configurationName, it)
        }
    }

}