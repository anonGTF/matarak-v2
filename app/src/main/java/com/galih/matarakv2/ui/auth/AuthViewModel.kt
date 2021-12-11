package com.galih.matarakv2.ui.auth

import com.galih.matarakv2.base.BaseViewModel
import com.galih.matarakv2.data.firebase.FirebaseUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val firebase: FirebaseUtils
) : BaseViewModel() {

    fun login(email: String, password: String) = callApiReturnLiveData(
        apiCall = { firebase.login(email, password) }
    )

    fun register(name: String, email: String, password: String) = callApiReturnLiveData(
        apiCall = { firebase.register(name, email, password) }
    )

    fun validateIsLoggedIn(): Boolean = firebase.auth.currentUser != null

}