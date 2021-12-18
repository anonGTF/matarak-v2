package com.galih.matarakv2.ui.detection

import android.graphics.Bitmap
import com.galih.matarakv2.base.BaseViewModel
import com.galih.matarakv2.data.firebase.FirebaseUtils
import com.galih.matarakv2.data.model.DetectionResult
import com.galih.matarakv2.utils.Classifier
import com.galih.matarakv2.utils.Utils
import com.galih.matarakv2.utils.Utils.toString
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetectionViewModel @Inject constructor(
    private val firebase: FirebaseUtils,
    private val classifier: Classifier
) : BaseViewModel() {

    fun uploadImage(data: ByteArray) = callApiReturnLiveData(
        apiCall = { firebase.uploadImage(data) }
    )

    fun detectImage(data: Bitmap, downloadUri: String): DetectionResult {
        val detectionResult = classifier.recognizeImage(data)
        val confidence = detectionResult[0].confidence * 100
        return DetectionResult(
            "",
            downloadUri,
            detectionResult[0].title,
            Utils.getCurrentDateTime().toString("yyyyMMdd-HH:mm:ss"),
            confidence.toDouble()
        )
    }

    fun saveImage(data: DetectionResult) = callApiReturnLiveData(
        apiCall = { firebase.saveImage(data) }
    )

    fun getHistories() = callApiReturnLiveData(
        apiCall = { firebase.getHistories() }
    )

}