package cz.cvut.fit.travelmates.trips.request.reject

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
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
class RejectDialogViewModel @Inject constructor(
    private val requestsRepository: RequestsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val args = RejectDialogFragmentArgs.fromSavedStateHandle(savedStateHandle)

    //Show message that request was rejected
    private val _eventRejected = SingleLiveEvent<Unit>()
    val eventRejected = _eventRejected.immutable()

    //Pop backstack to trip details screen
    private val _eventPopReview = SingleLiveEvent<Unit>()
    val eventPopReview = _eventPopReview.immutable()

    //Show error while rejecting request
    private val _eventError = SingleLiveEvent<Unit>()
    val eventError = _eventError.immutable()

    //Navigate back
    private val _eventNavigateBack = SingleLiveEvent<Unit>()
    val eventNavigateBack = _eventNavigateBack.immutable()

    //Rejection reason, synchronized with input field
    val typedReason = MutableStateFlow("")

    //Block resend, synchronized with checkbox
    val blockResend = MutableStateFlow(false)

    //ViewState for reject request action
    private val viewState = MutableStateFlow(ViewState.CONTENT)
    val contentVisible = viewState.map { it == ViewState.CONTENT }.asLiveData()
    val loadingVisible = viewState.map { it == ViewState.LOADING }.asLiveData()

    fun onRejectPressed() {
        viewModelScope.launchCatching(execute = {
            viewState.value = ViewState.LOADING
            val allowResend = !blockResend.value
            requestsRepository.rejectRequest(args.requestId, typedReason.value, allowResend)
            //Show message and navigate if rejection was successful
            _eventRejected.call()
            _eventPopReview.call()
        }, catch = {
            _eventError.call()
        }).invokeOnCompletion {
            viewState.value = ViewState.CONTENT
        }
    }

    fun onCancelPressed() {
        _eventNavigateBack.call()
    }

}