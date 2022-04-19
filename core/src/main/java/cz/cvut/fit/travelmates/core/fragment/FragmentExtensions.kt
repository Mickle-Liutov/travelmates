package cz.cvut.fit.travelmates.core.fragment

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

/**
 * Helper function for getting navigation result
 */
fun <T> Fragment.getNavigationResult(key: String = "result") =
    findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<T>(key)

/**
 * Helper function for setting navigation result
 */
fun <T> Fragment.setNavigationResult(result: T, key: String = "result") {
    findNavController().previousBackStackEntry?.savedStateHandle?.set(key, result)
}