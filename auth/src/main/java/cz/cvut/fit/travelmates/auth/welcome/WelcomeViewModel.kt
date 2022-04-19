package cz.cvut.fit.travelmates.auth.welcome

import androidx.lifecycle.ViewModel
import cz.cvut.fit.travelmates.core.livedata.SingleLiveEvent
import cz.cvut.fit.travelmates.core.livedata.immutable
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor() : ViewModel() {

    //Navigate to login
    private val _eventNavigateLogin = SingleLiveEvent<Unit>()
    val eventNavigateLogin = _eventNavigateLogin.immutable()

    //Navigate to register
    private val _eventNavigateRegister = SingleLiveEvent<Unit>()
    val eventNavigateRegister = _eventNavigateRegister.immutable()

    fun onLoginPressed() {
        _eventNavigateLogin.call()
    }

    fun onRegisterPressed() {
        _eventNavigateRegister.call()
    }

}