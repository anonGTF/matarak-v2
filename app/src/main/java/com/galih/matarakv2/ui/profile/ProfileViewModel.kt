package com.galih.matarakv2.ui.profile

import android.net.Uri
import com.galih.matarakv2.base.BaseViewModel
import com.galih.matarakv2.data.firebase.FirebaseUtils
import com.galih.matarakv2.data.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val firebase: FirebaseUtils
) : BaseViewModel() {

    fun getUserEmail() = firebase.getUserEmail()

    fun getUserId() = firebase.getUserId()

    fun getProfile() = callApiReturnLiveData(
        apiCall = { firebase.getProfile() }
    )

    fun uploadImageProfile(data: Uri) = callApiReturnLiveData(
        apiCall = { firebase.uploadProfileImage(data) }
    )

    fun saveProfile(name: String, birthDate: String, location: String, gender: Boolean, photoProfile: String)
    = callApiReturnLiveData(
        apiCall = {
            val user = User(getUserId(), name, birthDate, location, gender, photoProfile)
            firebase.saveProfile(user)
        }
    )

    fun logout() = callApiReturnLiveData(
        apiCall = { firebase.logout() }
    )

}