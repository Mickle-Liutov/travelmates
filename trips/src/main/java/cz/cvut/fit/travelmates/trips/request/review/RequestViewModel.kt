package cz.cvut.fit.travelmates.trips.request.review

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.travelmates.core.coroutines.launchCatching
import cz.cvut.fit.travelmates.core.livedata.SingleLiveEvent
import cz.cvut.fit.travelmates.core.livedata.immutable
import cz.cvut.fit.travelmates.trips.request.RequestsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RequestViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val requestsRepository: RequestsRepository
) : ViewModel() {

    private val _eventAccepted = SingleLiveEvent<Unit>()
    val eventAccepted = _eventAccepted.immutable()

    private val _eventNavigateReject = SingleLiveEvent<Long>()
    val eventNavigateReject = _eventNavigateReject.immutable()

    private val _eventNavigateBack = SingleLiveEvent<Unit>()
    val eventNavigateBack = _eventNavigateBack.immutable()

    private val args = RequestFragmentArgs.fromSavedStateHandle(savedStateHandle)
    private val request = args.request
    val senderImage = liveData { emit(request.user.picture) }
    val senderName = liveData { emit(request.user.name) }
    val message = liveData { emit(request.message) }

    fun onAcceptPressed() {
        viewModelScope.launchCatching(execute = {
            requestsRepository.acceptRequest(args.request.id)
            _eventAccepted.call()
            _eventNavigateBack.call()
        }, catch = {
            Timber.e(it)
            //TODO
        })
    }

    fun onRejectPressed() {
        _eventNavigateReject.value = args.request.id
    }

    fun onBackPressed() {
        _eventNavigateBack.call()
    }
}