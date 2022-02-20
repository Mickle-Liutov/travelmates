package cz.cvut.fit.travelmates.trips.tripdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.travelmates.core.coroutines.launchCatching
import cz.cvut.fit.travelmates.core.livedata.SingleLiveEvent
import cz.cvut.fit.travelmates.core.livedata.immutable
import cz.cvut.fit.travelmates.core.views.ViewState
import cz.cvut.fit.travelmates.mainapi.trips.models.DetailedTrip
import cz.cvut.fit.travelmates.mainapi.trips.models.Request
import cz.cvut.fit.travelmates.mainapi.trips.models.UserType
import cz.cvut.fit.travelmates.trips.TripsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class TripDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val tripsRepository: TripsRepository
) : ViewModel() {

    private val args = TripDetailsFragmentArgs.fromSavedStateHandle(savedStateHandle)
    private val tripId = args.tripId

    private val _detailedTripOptional = MutableStateFlow<DetailedTrip?>(null)
    private val _detailedTrip = _detailedTripOptional.filterNotNull()
    val detailedTrip = _detailedTrip.asLiveData()

    val location = _detailedTrip.map {
        val location = it.location
        "Location: ${location.lat}; ${location.lon}"
    }.asLiveData()
    val members = _detailedTrip.map {
        listOf(it.owner) + it.members
    }.asLiveData()
    val equipment = _detailedTrip.map {
        it.pendingRequirements
    }.asLiveData()
    val isGuest = _detailedTrip.map {
        it.userType == UserType.GUEST
    }.asLiveData()
    val isOwner = _detailedTrip.map {
        it.userType == UserType.OWNER
    }.asLiveData()
    val requests = _detailedTrip.map {
        it.requests.orEmpty()
    }.asLiveData()

    private val viewState = MutableStateFlow(ViewState.LOADING)
    val contentVisible = viewState.map { it == ViewState.CONTENT }.asLiveData()
    val loadingVisible = viewState.map { it == ViewState.LOADING }.asLiveData()
    val errorVisible = viewState.map { it == ViewState.ERROR }.asLiveData() //TODO Observe

    private val _eventNavigateJoin = SingleLiveEvent<DetailedTrip>()
    val eventNavigateJoin = _eventNavigateJoin.immutable()

    private val _eventNavigateRequest = SingleLiveEvent<Request>()
    val eventNavigateRequest = _eventNavigateRequest.immutable()

    private val _eventNavigateBack = SingleLiveEvent<Unit>()
    val eventNavigateBack = _eventNavigateBack.immutable()

    init {
        loadDetails()
    }

    private fun loadDetails() {
        viewModelScope.launchCatching(execute = {
            viewState.value = ViewState.LOADING
            val detailedTrip = tripsRepository.getTripDetails(tripId)
            _detailedTripOptional.value = detailedTrip
            viewState.value = ViewState.CONTENT
        }, catch = {
            //TODO Handle
            Timber.e(it)
            viewState.value = ViewState.ERROR
        })
    }

    fun onJoinPressed() {
        val trip = _detailedTripOptional.value ?: return
        _eventNavigateJoin.value = trip
    }

    fun onReviewRequestPressed(request: Request) {
        _eventNavigateRequest.value = request
    }

    fun onBackPressed() {
        _eventNavigateBack.call()
    }

}