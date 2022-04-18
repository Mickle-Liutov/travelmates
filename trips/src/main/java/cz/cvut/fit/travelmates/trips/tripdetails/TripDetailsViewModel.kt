package cz.cvut.fit.travelmates.trips.tripdetails

import android.graphics.Bitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.travelmates.core.coroutines.launchCatching
import cz.cvut.fit.travelmates.core.livedata.SingleLiveEvent
import cz.cvut.fit.travelmates.core.livedata.immutable
import cz.cvut.fit.travelmates.core.views.ViewState
import cz.cvut.fit.travelmates.mainapi.trips.models.Request
import cz.cvut.fit.travelmates.mainapi.trips.models.Trip
import cz.cvut.fit.travelmates.trips.TripsRepository
import cz.cvut.fit.travelmates.trips.tripdetails.images.AddImageItem
import cz.cvut.fit.travelmates.trips.tripdetails.images.Image
import cz.cvut.fit.travelmates.trips.tripdetails.images.UploadImageUseCase
import cz.cvut.fit.travelmates.trips.tripdetails.members.TripParticipant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class TripDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val tripsRepository: TripsRepository,
    private val tripDetailsStateMapper: TripDetailsStateMapper,
    private val uploadImage: UploadImageUseCase
) : ViewModel() {

    private val args = TripDetailsFragmentArgs.fromSavedStateHandle(savedStateHandle)
    private val tripId = args.tripId

    private val _detailedTripOptional = MutableStateFlow<Trip?>(null)
    private val _detailedTrip = _detailedTripOptional.filterNotNull()
    val detailedTrip = _detailedTrip.asLiveData()

    private val screenStateFlow = _detailedTrip.map {
        tripDetailsStateMapper.getTripState(it)
    }
    val screenState = screenStateFlow.asLiveData()

    val location = _detailedTrip.map {
        it.location
    }.asLiveData()
    val members = _detailedTrip.map {
        listOf(TripParticipant(it.owner, true)) + it.members.map { TripParticipant(it, false) }
    }.asLiveData()
    val equipment = _detailedTrip.map {
        it.requirements
    }.asLiveData()

    private val requestsFlow = _detailedTrip.map {
        it.requests.orEmpty()
    }
    val requests = requestsFlow.asLiveData()

    val rejectedReason = _detailedTrip.map {
        it.currentUserRequest?.rejectionReason.orEmpty()
    }.asLiveData()
    val requestsTitleVisible = combine(screenStateFlow, requestsFlow) { state, requests ->
        state.joinRequestsVisible && requests.isNotEmpty()
    }.asLiveData()

    val images = _detailedTrip.map {
        it.images.map { Image(it) } + AddImageItem
    }.asLiveData()

    private val viewState = MutableStateFlow(ViewState.LOADING)
    val contentVisible = viewState.map { it == ViewState.CONTENT }.asLiveData()
    val loadingVisible = viewState.map { it == ViewState.LOADING }.asLiveData()
    val errorVisible = viewState.map { it == ViewState.ERROR }.asLiveData() //TODO Observe

    private val _eventNavigateJoin = SingleLiveEvent<Trip>()
    val eventNavigateJoin = _eventNavigateJoin.immutable()

    private val _eventNavigateRequest = SingleLiveEvent<Request>()
    val eventNavigateRequest = _eventNavigateRequest.immutable()

    private val _eventPickImage = SingleLiveEvent<Request>()
    val eventPickImage = _eventPickImage.immutable()

    private val _eventNavigateBack = SingleLiveEvent<Unit>()
    val eventNavigateBack = _eventNavigateBack.immutable()

    init {
        loadTrip()
    }

    private fun loadTrip() {
        viewModelScope.launchCatching(execute = {
            viewState.value = ViewState.LOADING
            val detailedTrip = tripsRepository.getTripDetails(tripId)
            _detailedTripOptional.value = detailedTrip
            viewState.value = ViewState.CONTENT
        }, catch = {
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

    fun onStopGatheringPressed() {
        viewModelScope.launchCatching(execute = {
            val trip = _detailedTripOptional.value ?: return@launchCatching
            tripsRepository.stopGatheringTrip(trip.id)
            loadTrip()
        }, catch = {
            //TODO
        })
    }

    fun onFinishTripPressed() {
        viewModelScope.launchCatching(execute = {
            val trip = _detailedTripOptional.value ?: return@launchCatching
            tripsRepository.finishTrip(trip.id)
            loadTrip()
        }, catch = {
            //TODO
        })
    }

    fun onUploadImagePressed() {
        _eventPickImage.call()
    }

    fun onImagePicked(image: Bitmap) {
        viewModelScope.launchCatching(execute = {
            val trip = _detailedTripOptional.value ?: return@launchCatching
            uploadImage.invoke(image, trip.id)
            loadTrip()
        }, catch = {
            //TODO
        })
    }

    fun onBackPressed() {
        _eventNavigateBack.call()
    }

}