package cz.cvut.fit.travelmates.posts.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.travelmates.core.coroutines.launchCatching
import cz.cvut.fit.travelmates.core.livedata.SingleLiveEvent
import cz.cvut.fit.travelmates.core.livedata.immutable
import cz.cvut.fit.travelmates.core.views.ViewState
import cz.cvut.fit.travelmates.mainapi.posts.Post
import cz.cvut.fit.travelmates.posts.PostsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PostsViewModel @Inject constructor(
    private val postsRepository: PostsRepository
) : ViewModel() {

    private val viewState = MutableStateFlow(ViewState.LOADING)
    val contentVisible = viewState.map { it == ViewState.CONTENT }.asLiveData()
    val loadingVisible = viewState.map { it == ViewState.LOADING }.asLiveData()
    val errorVisible = viewState.map { it == ViewState.ERROR }.asLiveData() //TODO Observe

    private val _posts = MutableLiveData<List<Post>>()
    val posts = _posts.immutable()

    private val _eventNavigateAddPost = SingleLiveEvent<Unit>()
    val eventNavigateAddPost = _eventNavigateAddPost.immutable()

    init {
        loadPosts()
    }

    fun onRetryPressed() = loadPosts()

    private fun loadPosts() {
        viewModelScope.launchCatching(execute = {
            viewState.value = ViewState.LOADING
            val posts = postsRepository.getPosts()
            _posts.value = posts
            viewState.value = ViewState.CONTENT
        }, catch = {
            Timber.d(it)
            viewState.value = ViewState.ERROR
        })
    }

    fun onAddPressed() {
        _eventNavigateAddPost.call()
    }

}