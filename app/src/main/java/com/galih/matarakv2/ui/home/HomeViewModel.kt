package com.galih.matarakv2.ui.home

import com.galih.matarakv2.base.BaseViewModel
import com.galih.matarakv2.data.firebase.FirebaseUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val firebase: FirebaseUtils
): BaseViewModel() {

    fun getAllArticles() = callApiReturnLiveData(
        apiCall = { firebase.getArticles() }
    )

    fun getAllBanners() = callApiReturnLiveData(
        apiCall = { firebase.getBanners() }
    )

}