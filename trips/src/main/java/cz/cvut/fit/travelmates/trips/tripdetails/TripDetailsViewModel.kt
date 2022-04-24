package cz.cvut.fit.travelmates.trips.tripdetails

import android.graphics.Bitmap
import androidx.lifecycle.*
import cz.cvut.fit.travelmates.core.coroutines.launchCatching
import cz.cvut.fit.travelmates.core.livedata.SingleLiveEvent
import cz.cvut.fit.travelmates.core.livedata.immutable
import cz.cvut.fit.travelmates.core.views.ViewState
import cz.cvut.fit.travelmates.mainapi.trips.models.Request
import cz.cvut.fit.travelmates.mainapi.trips.models.Trip
import cz.cvut.fit.travelmates.mainapi.trips.models.TripMember
import cz.cvut.fit.travelmates.trips.TripsRepository
import cz.cvut.fit.travelmates.trips.tripdetails.images.AddImageItem
import cz.cvut.fit.travelmates.trips.tripdetails.images.Image
import cz.cvut.fit.travelmates.trips.tripdetails.images.UploadImageUseCase
import cz.cvut.fit.travelmates.trips.tripdetails.members.TripParticipant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TripDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val tripsRepository: TripsRepository,
    private val tripDetailsStateMapper: TripDetailsStateMapper,
    private val uploadImage: UploadImageUseCase
) : ViewModel(), DefaultLifecycleObserver {

    private val args = TripDetailsFragmentArgs.fromSavedStateHandle(savedStateHandle)
    private val tripId = args.tripId

    //Loaded trip
    private val _detailedTripOptional = MutableStateFlow<Trip?>(null)
    private val _detailedTrip = _detailedTripOptional.filterNotNull()
    val detailedTrip = _detailedTrip.asLiveData()

    //State of the screen, which decides which elements are shown
    //and which action are allowed
    private val screenStateFlow = _detailedTrip.map {
        tripDetailsStateMapper.getTripState(it)
    }
    val screenState = screenStateFlow.asLiveData()

    //Location of the trip
    val location = _detailedTrip.map {
        it.location
    }.asLiveData()

    //Members of the trip
    val members = _detailedTrip.map {
        listOf(TripParticipant(it.owner, true)) + it.members.map { TripParticipant(it, false) }
    }.asLiveData()

    //Equipment of the trip
    val equipment = _detailedTrip.map {
        it.requirements
    }.asLiveData()

    //Requests of the trip
    private val requestsFlow = _detailedTrip.map {
        it.requests.orEmpty()
    }
    val requests = requestsFlow.asLiveData()

    //Request rejection reason of the trip
    val rejectedReason = _detailedTrip.map {
        it.currentUserRequest?.rejectionReason.orEmpty()
    }.asLiveData()

    //Whether title of join requests section is visible
    val requestsTitleVisible = combine(screenStateFlow, requestsFlow) { state, requests ->
        state.joinRequestsVisible && requests.isNotEmpty()
    }.asLiveData()

    //Images of the trip
    val images = _detailedTrip.map {
        it.images.map { Image(it) } + AddImageItem
    }.asLiveData()

    //ViewState for loading trip details
    private val viewState = MutableStateFlow(ViewState.LOADING)
    val contentVisible = viewState.map { it == ViewState.CONTENT }.asLiveData()
    val loadingVisible = viewState.map { it == ViewState.LOADING }.asLiveData()
    val errorVisible = viewState.map { it == ViewState.ERROR }.asLiveData()

    //Navigate to join trip screen
    private val _eventNavigateJoin = SingleLiveEvent<Trip>()
    val eventNavigateJoin = _eventNavigateJoin.immutable()

    //Navigate to review request screen
    private val _eventNavigateRequest = SingleLiveEvent<Request>()
    val eventNavigateRequest = _eventNavigateRequest.immutable()

    //Show image picker
    private val _eventPickImage = SingleLiveEvent<Unit>()
    val eventPickImage = _eventPickImage.immutable()

    //Navigate to trip member screen
    private val _eventNavigateMember = SingleLiveEvent<TripMember>()
    val eventNavigateMember = _eventNavigateMember.immutable()

    //Show error while stopping gathering trip
    private val _eventStopGatheringError = SingleLiveEvent<Unit>()
    val eventStopGatheringError = _eventStopGatheringError.immutable()

    //Show error while finishing trip
    private val _eventFinishTripError = SingleLiveEvent<Unit>()
    val eventFinishTripError = _eventFinishTripError.immutable()

    //Show error while uploading image for a trip
    private val _eventUploadImageError = SingleLiveEvent<Unit>()
    val eventUploadImageError = _eventUploadImageError.immutable()

    //Navigate back
    private val _eventNavigateBack = SingleLiveEvent<Unit>()
    val eventNavigateBack = _eventNavigateBack.immutable()

    override fun onResume(owner: LifecycleOwner) {
        loadTrip()
    }

    fun onRetryPressed() = loadTrip()

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
            //Refresh the trip
            loadTrip()
        }, catch = {
            _eventStopGatheringError.call()
        })
    }

    fun onFinishTripPressed() {
        viewModelScope.launchCatching(execute = {
            val trip = _detailedTripOptional.value ?: return@launchCatching
            tripsRepository.finishTrip(trip.id)
            //Refresh the trip
            loadTrip()
        }, catch = {
            _eventFinishTripError.call()
        })
    }

    fun onUploadImagePressed() {
        _eventPickImage.call()
    }

    fun onImagePicked(image: Bitmap) {
        viewModelScope.launchCatching(execute = {
            val trip = _detailedTripOptional.value ?: return@launchCatching
            uploadImage.invoke(image, trip.id)
            //Refresh the trip
            loadTrip()
        }, catch = {
            _eventUploadImageError.call()
        })
    }

    fun onMemberPressed(member: TripMember) {
        viewModelScope.launch {
            val state = screenStateFlow.firstOrNull() ?: return@launch
            if (!state.canOpenMemberDetails) {
                //Block from opening member details
                return@launch
            }
            _eventNavigateMember.value = member
        }
    }

    fun onBackPressed() {
        _eventNavigateBack.call()
    }

}