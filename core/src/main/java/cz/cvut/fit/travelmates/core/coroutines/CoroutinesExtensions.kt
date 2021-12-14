package cz.cvut.fit.travelmates.core.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

fun CoroutineScope.launchCatching(
    execute: suspend () -> Unit,
    catch: (e: Exception) -> Unit = {}
): Job {
    return launch {
        try {
            execute.invoke()
        } catch (e: Exception) {
            catch.invoke(e)
        }
    }
}