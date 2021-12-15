package com.galih.matarakv2.data.model

import android.os.Parcelable
import android.util.Log
import com.galih.matarakv2.base.BaseModel
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Banner(
    override val id: String,
    val photo: String
): BaseModel(id), Parcelable {
    companion object {
        fun DocumentSnapshot.toBanner(): Banner? {
            return try {
                val photo = getString("photo")!!
                Banner(id, photo)
            } catch (e: Exception) {
                Log.e(TAG, "Error converting banner", e)
                FirebaseCrashlytics.getInstance().log("Error converting banner")
                FirebaseCrashlytics.getInstance().setCustomKey("bannerId", id)
                FirebaseCrashlytics.getInstance().recordException(e)
                null
            }
        }

        const val TAG = "banner"
    }
}