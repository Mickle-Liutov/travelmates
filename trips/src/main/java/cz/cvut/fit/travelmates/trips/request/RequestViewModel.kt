package cz.cvut.fit.travelmates.trips.request

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.travelmates.core.coroutines.launchCatching
import cz.cvut.fit.travelmates.core.livedata.SingleLiveEvent
import cz.cvut.fit.travelmates.core.livedata.immutable
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

    private val _eventRejected = SingleLiveEvent<Unit>()
    val eventRejected = _eventRejected.immutable()

    private val _eventNavigateBack = SingleLiveEvent<Unit>()
    val eventNavigateBack = _eventNavigateBack.immutable()

    private val args = RequestFragmentArgs.fromSavedStateHandle(savedStateHandle)

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
        viewModelScope.launchCatching(execute = {
            requestsRepository.rejectRequest(args.request.id, "TODO reason", true) /*TODO Add*/
            _eventRejected.call()
            _eventNavigateBack.call()
        }, catch = {
            Timber.e(it)
            //TODO
        })
    }

    fun onBackPressed() {
        _eventNavigateBack.call()
    }
}