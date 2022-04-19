package cz.cvut.fit.travelmates.trips.request.review

import androidx.lifecycle.*
import cz.cvut.fit.travelmates.core.coroutines.launchCatching
import cz.cvut.fit.travelmates.core.livedata.SingleLiveEvent
import cz.cvut.fit.travelmates.core.livedata.immutable
import cz.cvut.fit.travelmates.core.views.ViewState
import cz.cvut.fit.travelmates.trips.request.RequestsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class RequestViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val requestsRepository: RequestsRepository
) : ViewModel() {

    //Show message that request was accepted
    private val _eventAccepted = SingleLiveEvent<Unit>()
    val eventAccepted = _eventAccepted.immutable()

    //Navigate to reject request screen
    private val _eventNavigateReject = SingleLiveEvent<Long>()
    val eventNavigateReject = _eventNavigateReject.immutable()

    //Navigate back
    private val _eventNavigateBack = SingleLiveEvent<Unit>()
    val eventNavigateBack = _eventNavigateBack.immutable()

    //Show error while accepting the request
    private val _eventAcceptError = SingleLiveEvent<Unit>()
    val eventAcceptError = _eventAcceptError.immutable()

    private val args = RequestFragmentArgs.fromSavedStateHandle(savedStateHandle)

    private val request = args.request

    //Request details exposed as LiveData
    val senderImage = liveData { emit(request.user.picture) }
    val senderName = liveData { emit(request.user.name) }
    val message = liveData { emit(request.message) }

    //ViewState for accepting join request
    private val viewState = MutableStateFlow(ViewState.CONTENT)
    val contentVisible = viewState.map { it == ViewState.CONTENT }.asLiveData()
    val loadingVisible = viewState.map { it == ViewState.LOADING }.asLiveData()

    fun onAcceptPressed() {
        viewModelScope.launchCatching(execute = {
            viewState.value = ViewState.LOADING
            requestsRepository.acceptRequest(args.request.id)
            //Show message and navigate back if successfull
            _eventAccepted.call()
            _eventNavigateBack.call()
        }, catch = {
            _eventAcceptError.call()
        }).invokeOnCompletion {
            viewState.value = ViewState.CONTENT
        }
    }

    fun onRejectPressed() {
        _eventNavigateReject.value = args.request.id
    }

    fun onBackPressed() {
        _eventNavigateBack.call()
    }
}