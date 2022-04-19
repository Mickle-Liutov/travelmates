package cz.cvut.fit.travelmates.core.livedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/**
 * Helper to get LiveData from MutableLiveData
 */
fun <T> MutableLiveData<T>.immutable(): LiveData<T> {
    return this
}