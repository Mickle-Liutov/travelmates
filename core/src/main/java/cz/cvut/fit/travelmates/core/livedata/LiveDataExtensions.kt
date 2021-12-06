package cz.cvut.fit.travelmates.core.livedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

fun <T> MutableLiveData<T>.immutable(): LiveData<T> {
    return this
}