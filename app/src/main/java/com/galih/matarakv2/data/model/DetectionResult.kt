package com.galih.matarakv2.data.model

import android.os.Parcelable
import android.util.Log
import com.galih.matarakv2.base.BaseModel
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DetectionResult(
    override val id: String,
    val imageUrl: String,
    val title: String,
    val time: String,
    val confidence: Double
): BaseModel(id), Parcelable {
    companion object {
        fun DocumentSnapshot.toDetectionResult(): DetectionResult? {
            return try {
                val imageUrl = getString("imageUrl")!!
                val title = getString("title")!!
                val time = getString("time")!!
                val confidence = getDouble("confidence")!!
                DetectionResult(id, imageUrl, title, time, confidence)
            } catch (e: Exception) {
                Log.e(TAG, "Error converting detection result", e)
                FirebaseCrashlytics.getInstance().log("Error converting detection result")
                FirebaseCrashlytics.getInstance().setCustomKey("resultId", id)
                FirebaseCrashlytics.getInstance().recordException(e)
                null
            }
        }

        private const val TAG = "result"
    }
}
