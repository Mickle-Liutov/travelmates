package cz.cvut.fit.travelmates.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.travelmates.authapi.data.CredentialsDataSource
import cz.cvut.fit.travelmates.core.livedata.SingleLiveEvent
import cz.cvut.fit.travelmates.core.livedata.immutable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(private val credentialsDataSource: CredentialsDataSource) :
    ViewModel() {

    private val _eventNavigateAuth = SingleLiveEvent<Unit>()
    val eventNavigateAuth = _eventNavigateAuth.immutable()

    init {
        startCredentialsCheck()
    }

    private fun startCredentialsCheck() {
        viewModelScope.launch {
            credentialsDataSource.hasValidCredentials().collect { hasCredentials ->
                if (!hasCredentials) {
                    _eventNavigateAuth.call()
                }
            }
        }
    }

}