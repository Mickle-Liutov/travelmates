package cz.cvut.fit.travelmates.home

import androidx.lifecycle.*
import cz.cvut.fit.travelmates.authapi.AuthRepository
import cz.cvut.fit.travelmates.core.coroutines.launchCatching
import cz.cvut.fit.travelmates.core.livedata.SingleLiveEvent
import cz.cvut.fit.travelmates.core.livedata.immutable
import cz.cvut.fit.travelmates.core.views.ViewState
import cz.cvut.fit.travelmates.mainapi.posts.Post
import cz.cvut.fit.travelmates.mainapi.trips.models.Trip
import cz.cvut.fit.travelmates.posts.PostsRepository
import cz.cvut.fit.travelmates.trips.TripsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val tripsRepository: TripsRepository,
    private val postsRepository: PostsRepository
) : ViewModel(), DefaultLifecycleObserver {

    private val _eventNavigateTrips = SingleLiveEvent<Unit>()
    val eventNavigateTrips = _eventNavigateTrips.immutable()

    private val _eventNavigatePosts = SingleLiveEvent<Unit>()
    val eventNavigatePosts = _eventNavigatePosts.immutable()

    private val _eventNavigateOrganize = SingleLiveEvent<Unit>()
    val eventNavigateOrganize = _eventNavigateOrganize.immutable()

    private val _eventNavigateAuth = SingleLiveEvent<Unit>()
    val eventNavigateAuth = _eventNavigateAuth.immutable()

    private val _eventNavigateTripDetails = SingleLiveEvent<Long>()
    val eventNavigateTripDetails = _eventNavigateTripDetails.immutable()

    private val _trips = MutableLiveData<List<Trip>>()
    val trips = _trips.immutable()

    private val _posts = MutableLiveData<List<Post>>()
    val posts = _posts.immutable()

    private val viewState = MutableStateFlow(ViewState.LOADING)
    val contentVisible = viewState.map { it == ViewState.CONTENT }.asLiveData()
    val loadingVisible = viewState.map { it == ViewState.LOADING }.asLiveData()
    val errorVisible = viewState.map { it == ViewState.ERROR }.asLiveData() /*TODO Observe*/

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        checkSession()
    }

    private fun checkSession() {
        viewModelScope.launch {
            val hasSession = authRepository.hasValidSession()
            if (!hasSession) {
                _eventNavigateAuth.call()
                return@launch
            }
            loadData()
        }
    }

    fun onSeeAllTripsPressed() {
        _eventNavigateTrips.call()
    }

    fun onSeeAllPostsPressed() {
        _eventNavigatePosts.call()
    }

    fun onOrganizePressed() {
        _eventNavigateOrganize.call()
    }

    fun onTripPressed(trip: Trip) {
        _eventNavigateTripDetails.value = trip.id
    }

    fun onRetryPressed() = loadData()

    private fun loadData() {
        viewModelScope.launchCatching(execute = {
            viewState.value = ViewState.LOADING
            val trips = viewModelScope.async {
                tripsRepository.getExploreTrips()
            }
            val posts = viewModelScope.async {
                postsRepository.getPosts()
            }
            _trips.value = trips.await()
            _posts.value = posts.await()
            viewState.value = ViewState.CONTENT
        }, catch = {
            viewState.value = ViewState.ERROR
        })
    }

}