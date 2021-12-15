package com.galih.matarakv2.ui.profile

import com.galih.matarakv2.base.BaseViewModel
import com.galih.matarakv2.data.firebase.FirebaseUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val firebase: FirebaseUtils
) : BaseViewModel() {

    fun getUserEmail() = firebase.getUserEmail()

    fun getProfile() = callApiReturnLiveData(
        apiCall = { firebase.getProfile() }
    )

    fun logout() = callApiReturnLiveData(
        apiCall = { firebase.logout() }
    )

}