package cz.cvut.fit.travelmates.home

import android.annotation.SuppressLint
import androidx.lifecycle.*
import cz.cvut.fit.travelmates.authapi.AuthRepository
import cz.cvut.fit.travelmates.core.coroutines.launchCatching
import cz.cvut.fit.travelmates.core.livedata.SingleLiveEvent
import cz.cvut.fit.travelmates.core.livedata.immutable
import cz.cvut.fit.travelmates.core.views.ViewState
import cz.cvut.fit.travelmates.home.posts.HomeAddPost
import cz.cvut.fit.travelmates.home.posts.HomePost
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

    //Navigates to Explore screen
    private val _eventNavigateTrips = SingleLiveEvent<Unit>()
    val eventNavigateTrips = _eventNavigateTrips.immutable()

    //Navigates to Posts screen
    private val _eventNavigatePosts = SingleLiveEvent<Unit>()
    val eventNavigatePosts = _eventNavigatePosts.immutable()

    //Navigates to Create trip screen
    private val _eventNavigateOrganize = SingleLiveEvent<Unit>()
    val eventNavigateOrganize = _eventNavigateOrganize.immutable()

    //Navigates to Welcome screen
    private val _eventNavigateAuth = SingleLiveEvent<Unit>()
    val eventNavigateAuth = _eventNavigateAuth.immutable()

    //Navigates to Trip details screen
    private val _eventNavigateTripDetails = SingleLiveEvent<Long>()
    val eventNavigateTripDetails = _eventNavigateTripDetails.immutable()

    //Navigates to add post screen
    private val _eventNavigateAddPost = SingleLiveEvent<Unit>()
    val eventNavigateAddPost = _eventNavigateAddPost.immutable()

    //Trips which should be displayed
    private val _trips = MutableLiveData<List<Trip>>()
    val trips = _trips.immutable()

    //Posts which should be displayed, including the "Add" button
    private val _posts = MutableLiveData<List<Post>>()
    val posts = _posts.map {
        listOf(HomeAddPost) + it.map {
            HomePost(it)
        }
    }

    private val viewState = MutableStateFlow(ViewState.LOADING)
    val contentVisible = viewState.map { it == ViewState.CONTENT }.asLiveData()
    val loadingVisible = viewState.map { it == ViewState.LOADING }.asLiveData()
    val errorVisible = viewState.map { it == ViewState.ERROR }.asLiveData()

    /**
     * Check session when app comes into foreground
     */
    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        checkSession()
    }

    /**
     * Checks is user has session, navigates to auth of not, loads data if yes
     */
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

    fun onAddPostPressed() {
        _eventNavigateAddPost.call()
    }

    fun onRetryPressed() = loadData()

    /**
     * Loads data of the screen, both trips and posts
     */
    private fun loadData() {
        viewModelScope.launchCatching(execute = {
            viewState.value = ViewState.LOADING
            //Using async to load posts and trips in parallel
            val trips = viewModelScope.async {
                tripsRepository.getExploreTrips()
            }
            val posts = viewModelScope.async {
                postsRepository.getPosts()
            }
            //False positive lint issues below
            @SuppressLint("NullSafeMutableLiveData")
            _trips.value = trips.await()
            @SuppressLint("NullSafeMutableLiveData")
            _posts.value = posts.await()
            viewState.value = ViewState.CONTENT
        }, catch = {
            viewState.value = ViewState.ERROR
        })
    }

}